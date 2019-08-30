This file contains instructions on how to discover correlated sets from data.

WoDiscovery.jar is the executable with all dependencies attached.

valid dataset formats:
	-arff
	-xarf (https://bitbucket.org/realKD/realkd/wiki/model/data/xarf)
	-csv (with or without header)

arguments:
	Obligatory
		-DATASET  (dataset filename)
		-OUTPUTFOLDER  (output folder)
	Optional
		-K		(number of results the user wants, default is 1)
		-BINS   (number of bins for equal-frequency discretization, default is 5)
		-OPT    (bounding function to use. Values are MON, TIGHTERMON. Default is TIGHTERMON )
		For greedy only
			-BEAMWIDTH (the size of the beam, default is 1)
			-OPT    (beam has an additional option which is NONE, for no pruning at all)
		For Branch-and-bound  only
			-ALPHA   (alpha-approximation to use, default is 1 which yields the optimal solution)

The code creates an output file in the -OUTPUTFOLDER folder, with the dataset name, alpha used, k for top-k and a timestamp of the run. The file contains various statistics of the search, and the top-k patterns discovered.  Every pattern contains the entropy of the target, the mutual information, the expected mutual information under the null, the reliable fraction of information score, and the uncorrected reliable fraction of information score. 

For data attributes that are not categoric, equal-frequency discretization is applied.

Examples
		Branch and Bound:
			A command for the example dataset abalone.arff located in this folder: 

				nohup java -cp  WoDiscovery.jar discovering.WoOPUS -DATASET abalone.arff -OUTPUTFOLDER exampleOutput/ -K 5 -ALPHA 1 -OPT TIGHTERMON &	


		Greedy:	
			If Branch and Bound is slow, one can try the greedy algorithm that is not optimal, but in practice yields close to optimal solutions fast. Example command:

				nohup java -cp  WoDiscovery.jar discovering.WoBeam -DATASET abalone.arff -OUTPUTFOLDER exampleOutput/ -K 5 -OPT MON &


The output containts various statistics, such as nodes pruned and nodes created.
			











