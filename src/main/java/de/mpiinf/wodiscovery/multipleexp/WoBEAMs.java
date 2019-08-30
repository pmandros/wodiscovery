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

package de.mpiinf.wodiscovery.multipleexp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch;
import de.unibonn.realkd.algorithms.correlated.BeamCorrelatedPatternSearch.OptimisticEstimatorOption;
import de.unibonn.realkd.common.workspace.Workspace;
import de.unibonn.realkd.common.workspace.Workspaces;
import de.unibonn.realkd.data.table.DataTable;
import de.unibonn.realkd.data.xarf.XarfImport;
import de.unibonn.realkd.patterns.Pattern;
import de.unibonn.realkd.patterns.correlated.CorrelationPattern;
import utils.Utilities;

/**
 * A class for running multiple experiments using OPUS branch-and-bound search
 * 
 * @author Panagiotis Mandros
 *
 */
public class WoBEAMs {

	public static void main(String[] args) throws Exception {
		String input = Utilities.input(args);
		String outputFolder = Utilities.outputFolder(args);

		Path pathToOutput = Paths.get(outputFolder);
		if (!Files.exists(pathToOutput)) {

			Files.createDirectory(pathToOutput);
			System.out.println("Did not find the output folder. Created one instead.");
		}
		try {
			List<String> lines = Files.readAllLines(Paths.get(input));
			for (String s : lines) {
				String datasetPerExperiment = Utilities.dataset(s.split(" "));
				int k = Utilities.numResults(args);
				int beamWidth = Utilities.beamWidth(args);
				OptimisticEstimatorOption optOption = Utilities.optGreedy(args);
				int numBins = Utilities.numBins(args);

				String timeStamp = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date());

				Workspace workspace = Workspaces.workspace();
				XarfImport builder = XarfImport.xarfImport(datasetPerExperiment);
				DataTable dataTable = builder.get();
//				DiscreteDataTable dataDiscreteTable = DataTables.discretization(dataTable,
//						DataTables.equalFrequencyDiscretization(numBins));
				workspace.add(dataTable);

				BeamCorrelatedPatternSearch correlatedPatternSearch = new BeamCorrelatedPatternSearch(workspace);

				correlatedPatternSearch.topK(k);
				correlatedPatternSearch.optimisticOption(optOption);
				correlatedPatternSearch.beamWidth(beamWidth);

				String datasetName = dataTable.caption();
				String experimentResultsFile = outputFolder + File.separator + datasetName + "_" + optOption.toString()
						+ "_topk-" + k + "_" + timeStamp + ".txt";
				Collection<CorrelationPattern> resultPatterns = correlatedPatternSearch.call();
				long time = correlatedPatternSearch.runningTime();
				int nodes = correlatedPatternSearch.nodesCreated();
				int nodesDiscardedPotential = correlatedPatternSearch.nodesDiscarded();
				int max_depth = correlatedPatternSearch.maxAttainedDepth();
				int solution_depth = correlatedPatternSearch.bestDepth();
				int boundary_max_size = correlatedPatternSearch.maxAttainedBoundarySize();

				try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(experimentResultsFile))) {
					writer.write("time: " + time / 1000.0);
					writer.write("\n");
					writer.write("Beam width used: " + beamWidth);
					writer.write("\n");
					writer.write("Search space: "
							+ (Math.pow(2, dataTable.numberOfAttributes()) - dataTable.numberOfAttributes()));
					writer.write("\n");
					writer.write("Nodes created: " + nodes);
					writer.write("\n");
					writer.write("Nodes discarded potential: " + nodesDiscardedPotential);
					writer.write("\n");
					writer.write("Nodes discarded rules: " + 0);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
