package com.doohh.nn;

import org.deeplearning4j.datasets.iterator.AsyncDataSetIterator;
import org.deeplearning4j.datasets.iterator.MultipleEpochsIterator;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.Solver;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.heartbeat.Heartbeat;
import org.nd4j.linalg.heartbeat.reports.Environment;
import org.nd4j.linalg.heartbeat.reports.Event;
import org.nd4j.linalg.heartbeat.reports.Task;
import org.nd4j.linalg.heartbeat.utils.EnvironmentUtils;
import org.nd4j.linalg.heartbeat.utils.TaskUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistMultiLayerNetwork extends MultiLayerNetwork {
	private static final Logger log = LoggerFactory.getLogger(DistMultiLayerNetwork.class);

	
	public DistMultiLayerNetwork(MultiLayerConfiguration conf) {
		super(conf);
	}

	@Override
    public void fit(DataSetIterator iterator) {
        DataSetIterator iter;
        // we're wrapping all iterators into AsyncDataSetIterator to provide background prefetch
        if (!(iterator instanceof AsyncDataSetIterator || iterator instanceof ListDataSetIterator || iterator instanceof MultipleEpochsIterator)) {
            iter = new AsyncDataSetIterator(iterator, 2);
        } else iter = iterator;

        if (layerWiseConfigurations.isPretrain()) {
            pretrain(iter);
            iter.reset();
            while (iter.hasNext()) {
                DataSet next = iter.next();
                if (next.getFeatureMatrix() == null || next.getLabels() == null)
                    break;
                setInput(next.getFeatureMatrix());
                setLabels(next.getLabels());
                finetune();
            }

        }
        if (layerWiseConfigurations.isBackprop()) {
            if(layerWiseConfigurations.isPretrain())
                iter.reset();
            update(TaskUtils.buildTask(iter));
            iter.reset();
            while (iter.hasNext()) {
                //receive param from master
                
                //set param
            	            	
                DataSet next = iter.next();
                if (next.getFeatureMatrix() == null || next.getLabels() == null)
                    break;

                boolean hasMaskArrays = next.hasMaskArrays();

                if(layerWiseConfigurations.getBackpropType() == BackpropType.TruncatedBPTT) {
                    doTruncatedBPTT(next.getFeatureMatrix(),next.getLabels(),next.getFeaturesMaskArray(),next.getLabelsMaskArray());
                }
                else {
                    if(hasMaskArrays) setLayerMaskArrays(next.getFeaturesMaskArray(), next.getLabelsMaskArray());
                    setInput(next.getFeatureMatrix());
                    setLabels(next.getLabels());
                    if( solver == null ){
                        solver = new Solver.Builder()
                                .configure(conf())
                                .listeners(getListeners())
                                .model(this).build();
                    }
                    solver.optimize();
                }
                //send gradient to master
                
                //log.info("gradient : {}", this.flattenedGradients.getFloat(new int[]{0, 0}));
                if(hasMaskArrays) clearLayerMaskArrays();
            }
        }
    }
	
	private void update(Task task) {
        if (!initDone) {
            initDone = true;
            Heartbeat heartbeat = Heartbeat.getInstance();
            task = ModelSerializer.taskByModel(this);
            Environment env = EnvironmentUtils.buildEnvironment();
            heartbeat.reportEvent(Event.STANDALONE, env, task);
        }
    }
}
