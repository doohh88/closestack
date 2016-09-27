package com.doohh.example;

import org.datavec.image.loader.CifarLoader;
import org.deeplearning4j.datasets.iterator.impl.CifarDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.conf.layers.setup.ConvolutionLayerSetup;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doohh.nn.DistMultiLayerNetwork;

public class CifarDistEx {
private static final Logger log = LoggerFactory.getLogger(CifarDistEx.class);
	
	@Option(name="--batchSize",usage="batchSize",aliases = "-b")
    int batchSize = 500;
    @Option(name="--nEpochs",usage="nEpochs",aliases = "-e")
    int nEpochs = 10;
    @Option(name="--numTrain",usage="numTrain",aliases = "-tr")
    int numTrain = CifarLoader.NUM_TRAIN_IMAGES;
    @Option(name="--numTest",usage="numTest",aliases = "-te")
    int numTest = CifarLoader.NUM_TEST_IMAGES;

    
	private void parseArgs(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            log.info("batchSize : {}", batchSize);
            log.info("nEpochs: {}", nEpochs);
        } catch (CmdLineException e) {
            // handling of wrong arguments
        	log.info(e.getMessage());
            parser.printUsage(System.err);
        }

	}
	
	void run(String[] args) {
		// TODO Auto-generated method stub
		this.parseArgs(args);
		
		int nChannels = 3;
	    int outputNum = 10;
	    int iterations = 10;
	    int splitTrainNum = (int) (batchSize*.8);
	    int seed = 123;
	    int listenerFreq = iterations/5;
		
        DataSetIterator train = new CifarDataSetIterator(batchSize, numTrain, true);
        DataSetIterator test = new CifarDataSetIterator(batchSize, numTest, false);
               
        //setup the network
        MultiLayerConfiguration.Builder builder = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(iterations).regularization(true)
                .l1(1e-1).l2(2e-4).useDropConnect(true)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer) // TODO confirm this is required
                .miniBatch(true)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .list(6)
                .layer(0, new ConvolutionLayer.Builder(5, 5)
                        .nOut(5).dropOut(0.5)
                        .stride(2, 2)
                        .weightInit(WeightInit.XAVIER)
                        .activation("relu")
                        .build())
                .layer(1, new SubsamplingLayer
                        .Builder(SubsamplingLayer.PoolingType.MAX, new int[]{2, 2})
                        .build())
                .layer(2, new ConvolutionLayer.Builder(3, 3)
                        .nOut(10).dropOut(0.5)
                        .stride(2, 2)
                        .weightInit(WeightInit.XAVIER)
                        .activation("relu")
                        .build())
                .layer(3, new SubsamplingLayer
                        .Builder(SubsamplingLayer.PoolingType.MAX, new int[]{2, 2})
                        .build())
                .layer(4, new DenseLayer.Builder().nOut(100).activation("relu")
                        .build())
                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(outputNum)
                        .weightInit(WeightInit.XAVIER)
                        .activation("softmax")
                        .build())
                .backprop(true).pretrain(false);

        new ConvolutionLayerSetup(builder,32,32,nChannels);
        MultiLayerConfiguration conf = builder.build();
        MultiLayerNetwork network = new DistMultiLayerNetwork(conf);
        network.init();
        network.setListeners(new ScoreIterationListener(listenerFreq));
		        
        for (int i = 0; i < nEpochs; i++) {
			network.fit(train);
			
			Evaluation eval = new Evaluation(outputNum);
			test.reset();
			while (test.hasNext()) {
				DataSet testSet = test.next();
				// log.info("{}", testSet.get(0));
				INDArray output = network.output(testSet.getFeatureMatrix(), false);
				eval.eval(testSet.getLabels(), output);
			}
			log.info(eval.stats());
		}
	}
	
	public static void main(String[] args) { new CifarDistEx().run(args);	}
}
