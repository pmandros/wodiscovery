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

package de.mpiinf.wodiscovery.singleexp;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import de.unibonn.realkd.algorithms.branchbound.BranchAndBound.TraverseOrder;
import de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch;
import de.unibonn.realkd.algorithms.correlated.BNBCorrelatedPatternSearch.OptimisticEstimatorOption;
import de.unibonn.realkd.common.workspace.Workspace;
import de.unibonn.realkd.common.workspace.Workspaces;
import de.unibonn.realkd.data.table.DataTable;
import de.unibonn.realkd.data.table.DataTables;
import de.unibonn.realkd.data.table.DiscreteDataTable;
import de.unibonn.realkd.data.xarf.XarfImport;
import de.unibonn.realkd.patterns.Pattern;
import de.unibonn.realkd.patterns.correlated.CorrelationPattern;
import utils.Utilities;

/**
 * A class for discovery using branch-and-bound search
 * 
 * 
 * @author Panagiotis Mandros
 *
 */
public class WoBNB {
	public static void main(String[] args) throws Exception {
		String dataset = Utilities.dataset(args);
		String outputFolder = Utilities.outputFolder(args);
		int k = Utilities.numResults(args);
		TraverseOrder traverseOption = Utilities.traverseOrderOption(args);
		OptimisticEstimatorOption optOption = Utilities.optBNB(args);
		double alpha = Utilities.alpha(args);
		int numBins = Utilities.numBins(args);

		Path pathToOutput = Paths.get(outputFolder);
		if (!Files.exists(pathToOutput)) {
			Files.createDirectory(pathToOutput);
			System.out.println("Did not find the output folder. Created one instead.");
		}
		String timeStamp = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date());

		Workspace workspace = Workspaces.workspace();
		XarfImport builder = XarfImport.xarfImport(dataset);
		DataTable dataTable = builder.get();
		DiscreteDataTable dataDiscreteTable = DataTables.discretization(dataTable,
				DataTables.equalFrequencyDiscretization(numBins));
		workspace.add(dataDiscreteTable);

		BNBCorrelatedPatternSearch correlatedPatternSearch = new BNBCorrelatedPatternSearch(workspace);

		correlatedPatternSearch.topK(k);
		correlatedPatternSearch.alpha(alpha);
		correlatedPatternSearch.optimisticOption(optOption);
		correlatedPatternSearch.traverseOrderOption(traverseOption);

		String datasetName = dataTable.caption();
		String experimentResultsFile = outputFolder + File.separator + datasetName + "_" + traverseOption.toString()
				+ "_" + optOption.toString() + "_a-" + alpha + "_topk-" + k + "_" + timeStamp + ".txt";
		Collection<CorrelationPattern> resultPatterns = correlatedPatternSearch.call();
		long time = correlatedPatternSearch.runningTime();
		int nodes = correlatedPatternSearch.nodesCreated();
		int nodesDiscardedPotential = correlatedPatternSearch.nodesDiscarded();
		int nodesDiscardedPruning = correlatedPatternSearch.nodesDiscardedPruningRules();
		int max_depth = correlatedPatternSearch.maxAttainedDepth();
		int solution_depth = correlatedPatternSearch.bestDepth();
		int boundary_max_size = correlatedPatternSearch.maxAttainedBoundarySize();

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(experimentResultsFile))) {
			writer.write("time: " + time / 1000.0);
			writer.write("\n");
			writer.write("Alpha used: " + alpha);
			writer.write("\n");
			writer.write(
					"Search space: " + (Math.pow(2, dataTable.numberOfAttributes()) - dataTable.numberOfAttributes()));
			writer.write("\n");
			writer.write("Nodes created: " + nodes);
			writer.write("\n");
			writer.write("Nodes discarded potential: " + nodesDiscardedPotential);
			writer.write("\n");
			writer.write("Nodes discarded rules: " + nodesDiscardedPruning);
			writer.write("\n");
			writer.write("Pruned search space percentage: " + (100 - (1.0 * 100 * nodes)
					/ (Math.pow(2, dataTable.numberOfAttributes()) - dataTable.numberOfAttributes())));
			writer.write("\n");
			writer.write("Max depth: " + max_depth);
			writer.write("\n");
			writer.write("Solution depth: " + solution_depth);
			writer.write("\n");
			writer.write("Max boundary size: " + boundary_max_size);
			writer.write("\n");
			writer.write("Dependencies: ");
			writer.write("\n");

			for (Pattern<?> pattern : resultPatterns) {
				writer.write(pattern.toString());
				writer.write("\n");
				System.out.println(pattern);
			}
			System.out.println(time / 1000.0);

		}
	}
}
