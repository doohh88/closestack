package com.doohh.example;

import java.util.concurrent.TimeUnit;

import org.datavec.image.loader.CifarLoader;
import org.deeplearning4j.datasets.iterator.impl.CifarDataSetIterator;
import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.EarlyStoppingResult;
import org.deeplearning4j.earlystopping.saver.LocalFileModelSaver;
import org.deeplearning4j.earlystopping.scorecalc.DataSetLossCalculator;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.termination.MaxTimeIterationTerminationCondition;
import org.deeplearning4j.earlystopping.trainer.EarlyStoppingTrainer;
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
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CifarEarlyStoppingEx {
    private static final Logger log = LoggerFactory.getLogger(CifarEarlyStoppingEx.class);
    @Option(name="--batchSize",usage="batchSize",aliases = "-b")
    int batchSize = 500;
    @Option(name="--nEpochs",usage="nEpochs",aliases = "-e")
    int nEpochs = 1;
    @Option(name="--numTrain",usage="numTrain",aliases = "-n")
    int numTrain = CifarLoader.NUM_TRAIN_IMAGES;
    @Option(name="--numTest",usage="numTest",aliases = "-t")
    int numTest = CifarLoader.NUM_TEST_IMAGES;

    
	private void pareArgs(String[] args) {
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
	
	private void run(String[] args) {
		// TODO Auto-generated method stub
		this.pareArgs(args);
		
		int nChannels = 3;
	    int outputNum = 10;
	    int iterations = 10;
	    int splitTrainNum = (int) (batchSize*.8);
	    int seed = 123;
	    int listenerFreq = iterations/5;
		String saveDirectory = System.getProperty("user.dir") + "/log/";			    
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
        
        
        
        EarlyStoppingConfiguration esConf = new EarlyStoppingConfiguration.Builder()
        		.epochTerminationConditions(new MaxEpochsTerminationCondition(30))
        		.iterationTerminationConditions(new MaxTimeIterationTerminationCondition(20, TimeUnit.MINUTES))
        		.scoreCalculator(new DataSetLossCalculator(test, true))
                .evaluateEveryNEpochs(1)
        		.modelSaver(new LocalFileModelSaver(saveDirectory))
        		.build();

        EarlyStoppingTrainer trainer = new EarlyStoppingTrainer(esConf,conf,train);

        //Conduct early stopping training:
        EarlyStoppingResult result = trainer.fit();

        //Print out the results:
        System.out.println("Termination reason: " + result.getTerminationReason());
        System.out.println("Termination details: " + result.getTerminationDetails());
        System.out.println("Total epochs: " + result.getTotalEpochs());
        System.out.println("Best epoch number: " + result.getBestModelEpoch());
        System.out.println("Score at best epoch: " + result.getBestModelScore());

        //Get the best model:
        MultiLayerNetwork bestModel = (MultiLayerNetwork) result.getBestModel();
        
        Evaluation eval = new Evaluation(outputNum);
		test.reset();
		while (test.hasNext()) {
			DataSet testSet = test.next();
			// log.info("{}", testSet.get(0));
			INDArray output = bestModel.output(testSet.getFeatureMatrix(), false);
			eval.eval(testSet.getLabels(), output);
		}
		log.info(eval.stats());
        
	}
	
	public static void main(String[] args) { new CifarEarlyStoppingEx().run(args);	}
}
