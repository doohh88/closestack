package com.doohh.example;

import org.datavec.image.loader.CifarLoader;
import org.deeplearning4j.datasets.iterator.MultipleEpochsIterator;
import org.deeplearning4j.datasets.iterator.impl.CifarDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import example.CUtil;

public class AlexNetExample {
	private static final Logger log = LoggerFactory.getLogger(AlexNetExample.class);
	protected static int HEIGHT = 224;
	protected static int WIDTH = 224;
	protected static int CHANNELS = 3;
	protected static int numTrainExamples = 2000;// CifarLoader.NUM_TRAIN_IMAGES;
	protected static int numTestExamples = 2000; // CifarLoader.NUM_TEST_IMAGES;
	protected static int numLabels = CifarLoader.NUM_LABELS;

	public static void main(String[] args) throws Exception {
		CUtil.load("libopenblas");
		//LibUtils.loadLibrary("libopenblas");

		int nChannels = 3;
		int outputNum = 10;
		int batchSize = 40;
		int nEpochs = 1;
		int iterations = 1;
		int seed = 123;
		int normalizeValue = 255;

		log.info("Load data....");
		long dataLoadTime = System.currentTimeMillis();
		/*
		 * MultipleEpochsIterator cifar = new MultipleEpochsIterator(nEpochs,
		 * new CifarDataSetIterator(batchSize, numTrainExamples, new
		 * int[]{HEIGHT, WIDTH, CHANNELS}, numLabels, null, normalizeValue,
		 * true)); MultipleEpochsIterator cifarTest = new
		 * MultipleEpochsIterator(1, new CifarDataSetIterator(batchSize,
		 * numTestExamples, new int[] {HEIGHT, WIDTH, CHANNELS}, normalizeValue,
		 * false));
		 */
		MultipleEpochsIterator cifar = new MultipleEpochsIterator(nEpochs, new CifarDataSetIterator(batchSize,
				numTrainExamples, new int[] { HEIGHT, WIDTH, CHANNELS }, numLabels, true));
		MultipleEpochsIterator cifarTest = new MultipleEpochsIterator(1,
				new CifarDataSetIterator(batchSize, numTestExamples, new int[] { HEIGHT, WIDTH, CHANNELS }, false));
		dataLoadTime = System.currentTimeMillis() - dataLoadTime;
		System.out.println("dataLoadTime : " + dataLoadTime);

		log.info("Build model....");
		AlexNet alexNet = new AlexNet(HEIGHT, WIDTH, CHANNELS, outputNum, seed, iterations);
		MultiLayerNetwork model = alexNet.init();

		log.info("Train model....");
		long trainTime = System.currentTimeMillis();
		model.setListeners(new ScoreIterationListener(1));
		for (int i = 0; i < nEpochs; i++) {
			model.fit(cifar);
			log.info("*** Completed epoch {} ***", i);

			log.info("Evaluate model....");
			Evaluation eval = new Evaluation(outputNum);
			while (cifarTest.hasNext()) {
				DataSet ds = cifarTest.next();
				INDArray output = model.output(ds.getFeatureMatrix(), false);
				eval.eval(ds.getLabels(), output);
			}
			log.info(eval.stats());
			cifarTest.reset();
		}
		log.info("****************Example finished********************");
		trainTime = System.currentTimeMillis() - trainTime;
		System.out.println("trainTime : " + trainTime);
	}
}
