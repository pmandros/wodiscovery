/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-16 The Contributors of the realKD Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package utils;

import de.unibonn.realkd.algorithms.branchbound.BranchAndBound;

/**
 * A class to provide utility functions
 * 
 * 
 * @author Panagiotis Mandros
 */
public class Utilities {

	// default values for experiment parameters
	public static final int NUM_BINS = 2;
	public static final int MAX_DEPTH = 10000;
	public static final int NUM_RESULTS = 1;
	public static final int TARGET = 0;
	public static final de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch.OptimisticEstimatorOption OPT_BNB = de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch.OptimisticEstimatorOption.CHAIN;
	public static final de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch.OptimisticEstimatorOption OPT_GREEDY = de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch.OptimisticEstimatorOption.CHAIN;

	public static final double ALPHA = 1;
	public static final int BEAM_WIDTH = 5;
	public static final BranchAndBound.TraverseOrder TRAVERSE_ORDER = BranchAndBound.TraverseOrder.BESTFSPOTENTIAL;

	public static int numBins(String args[]) {
		int numBins = NUM_BINS;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-BINS")) {
				numBins = Integer.parseInt(args[i + 1]);
				if (numBins <= 1) {
					throw new IllegalArgumentException(
							"Illegal value for number of bins for discretization. Should be greater than 1");
				}
				break;
			}
		}
		return numBins;
	}

	public static int numResults(String args[]) {
		int k = NUM_RESULTS;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-K")) {
				k = Integer.parseInt(args[i + 1]);
				if (k < 1) {
					throw new IllegalArgumentException(
							"Illegal value for number of results. Should be greater or equal to 1");
				}
				break;
			}
		}
		return k;
	}

	public static int target(String args[]) {
		int target = TARGET;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-TARGET")) {
				target = Integer.parseInt(args[i + 1]);
				if (target < 1) {
					throw new IllegalArgumentException(
							"Illegal value target attribute index. Should be between 1 and total number of attributes");
				}
				break;
			}
		}
		return target;
	}

	public static de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch.OptimisticEstimatorOption optBNB(
			String args[]) {
		de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch.OptimisticEstimatorOption optOption = OPT_BNB;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-OPT")) {
				String optEstimatorToStr = (args[i + 1]);
				if (optEstimatorToStr.equals("MON")) {
					optOption = de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch.OptimisticEstimatorOption.MON;
				} else if (optEstimatorToStr.equals("NONE")) {
					optOption = de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch.OptimisticEstimatorOption.NONE;
				} else if (optEstimatorToStr.equals("CHAIN")) {
					optOption = de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch.OptimisticEstimatorOption.CHAIN;
				} else {
					throw new IllegalArgumentException(
							"Wrong optimistic estimator argument. Valid options are NONE, MON, and CHAIN");
				}
				break;
			}
		}
		return optOption;
	}

	public static de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch.OptimisticEstimatorOption optGreedy(
			String args[]) {
		de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch.OptimisticEstimatorOption optOption = OPT_GREEDY;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-OPT")) {
				String optEstimatorToStr = (args[i + 1]);
				if (optEstimatorToStr.equals("MON")) {
					optOption = de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch.OptimisticEstimatorOption.MON;
				} else if (optEstimatorToStr.equals("NONE")) {
					optOption = de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch.OptimisticEstimatorOption.NONE;
				} else if (optEstimatorToStr.equals("CHAIN")) {
					optOption = de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch.OptimisticEstimatorOption.CHAIN;
				} else {
					throw new IllegalArgumentException(
							"Wrong optimistic estimator argument. Valid options are NONE, MON, and CHAIN");
				}
				break;
			}
		}
		return optOption;
	}

	public static int beamWidth(String args[]) {
		int beamWidth = BEAM_WIDTH;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-BEAMWIDTH")) {
				beamWidth = Integer.parseInt(args[i + 1]);
				if (beamWidth < 1) {
					throw new IllegalArgumentException("Illegal value for beam width. Should be greater or equal to 1");
				}
				break;
			}
		}
		return beamWidth;
	}

	public static int maxDepth(String args[]) {
		int maxDepth = MAX_DEPTH;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-MAXDEPTH")) {
				maxDepth = Integer.parseInt(args[i + 1]);
				if (maxDepth < 1) {
					throw new IllegalArgumentException("Illegal value for max depth. Should be greater or equal to 1");
				}
				break;
			}
		}
		return maxDepth;
	}

	public static double alpha(String args[]) {
		double alpha = ALPHA;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-ALPHA")) {
				alpha = Double.parseDouble(args[i + 1]);
				if (alpha <= 0 && alpha > 1) {
					throw new IllegalArgumentException("Illegal value for alpha. Range should be (0,1]");
				}
				break;
			}
		}
		return alpha;
	}

	public static String dataset(String args[]) {
		String dataset = "";
		int i;
		int total = args.length - 1;
		boolean found = false;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-DATASET")) {
				dataset = args[i + 1];
				found = true;
				break;
			}
		}
		if (found == false || dataset.isEmpty()) {
			throw new IllegalStateException("Missing -DATASET");
		} else {
			return dataset;
		}
	}

	public static String outputFolder(String args[]) {
		String outputFolder = "";
		int i;
		int total = args.length - 1;
		boolean found = false;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-OUTPUTFOLDER")) {
				outputFolder = args[i + 1];
				found = true;
				break;
			}
		}

		if (found == false || outputFolder.isEmpty()) {
			throw new IllegalStateException("Missing -OUTPUTFOLDER");
		} else {
			return outputFolder;
		}
	}

	public static String input(String args[]) {
		String input = "";
		int i;
		int total = args.length - 1;
		boolean found = false;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-INPUT")) {
				input = args[i + 1];
				found = true;
				break;
			}
		}

		if (found == false || input.isEmpty()) {
			throw new IllegalStateException("Missing -INPUT");
		} else {
			return input;
		}
	}

	//
	public static BranchAndBound.TraverseOrder traverseOrderOption(String args[]) {
		BranchAndBound.TraverseOrder travOption = TRAVERSE_ORDER;
		int i;
		int total = args.length - 1;
		for (i = 0; i < total; i++) {
			if (args[i].equals("-TRAVERSEORDER")) {
				String travOptionString = (args[i + 1]);
				if (travOptionString.equals("FIFO")) {
					travOption = BranchAndBound.TraverseOrder.FIFO;
				} else if (travOptionString.equals("BESTFSPOTENTIAL")) {
					travOption = BranchAndBound.TraverseOrder.BESTFSPOTENTIAL;
				} else if (travOptionString.equals("BREADTHFSPOTENTIAL")) {
					travOption = BranchAndBound.TraverseOrder.BREADTHFSPOTENTIAL;
				} else if (travOptionString.equals("BESTFSVALUE")) {
					travOption = BranchAndBound.TraverseOrder.BESTFSVALUE;
				} else if (travOptionString.equals("DFS")) {
					travOption = BranchAndBound.TraverseOrder.DFS;
				}

				else {
					throw new IllegalArgumentException(
							"Wrong traverse order option argument. Valid options are FIFO, BESTFSPOTENTIAL, BESTFSVALUE, BREADTHFSPOTENTIAL");
				}
				break;
			}
		}
		return travOption;
	}

}
