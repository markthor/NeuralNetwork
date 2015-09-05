package cli;

import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import evolution.Evolver;
import evolution.SelectionCriteria;
import evolution.SpawnCriteria;

// This class handles command line arguments. It assumes, that all default values have already been set.
public class CLIParser {
	private static ArrayList<String> usedArgs;
	private static ArrayList<String> unusedArgs;
	private static ArrayList<String> ignoredArgs;
	
	private static final String[] ARG0  = {"h", "help", "Show this help message"};
	private static final String[] ARG1  = {"e", "evolve", "Evolve (Sideeffect: No GUI)"};
	private static final String[] ARG2  = {"r", "read-old", "Read old generations and continue"};
	private static final String[] ARG3  = {"I", "infinity", "Simulation never terminates (ignores terminal fitness and terminal generation)"};
	private static final String[] ARG4  = {"s", "hidden-size", "Size of the hidden layer (int)"};
	private static final String[] ARG5  = {"m", "mutation-probability", "Probability of mutation (double)"};
	private static final String[] ARG6  = {"i", "intensity", "Intensity of mutation (double)"};
	private static final String[] ARG7  = {"g", "read-gen", "Read the N'th generation-file (default value: latest gen) (int)"};
	private static final String[] ARG8  = {"c", "crossover-probability", "Probability of a cross-over (double)"};
	private static final String[] ARG9  = {"S", "generation-size", "Size of each generation (int)"};
	private static final String[] ARG10 = {"E", "elitists", "Number of elitists per generation (int)"};
	private static final String[] ARG11 = {"tf", "terminal-fitness", "Terminate program at N fitness (int)"};
	private static final String[] ARG12 = {"tg", "terminal-generation", "Terminate program after N generations (int)"};
	private static final String[] ARG13 = {"si", "save-interval", "Save every N'th generation to file (int)"};
	private static final String[] ARG14 = {"p", "parents", "Number of parents used for seeding each generation (int)"};
	private static final String[] ARG15 = {"C", "child-evaluations", "Number of evaluations per child (Increase to reduce RNG-noise) (int)"};
	private static final String[] ARG16 = {"sel", "selection", "Criteria for selecting seeds for new generations. Options: byRank, ByFitness, Fittest"};
	private static final String[] ARG17 = {"sc", "spawn", "Criteria for spawning seeds for new generations. Options: mutation, crossover"};
	private static final String[] ARG18 = {"w", "weight", "Initial weight for initialization of each synapsis (double)"};
	private static final String[] ARG19 = {"b", "bias", "Initial bias for initialization of each synapsis (double)"};
	
	
	public static void readArgs(String[] args) {
		Options options = getOptions();
		parse(options, args);
		
		//System.out.println("Specified args: ");
		//printArgs(getUsedArgs());
		//System.out.println("Unspecified args: ");
		//printArgs(getUnusedArgs());
		
	}
	
	private static Options getOptions() {
		Options options = new Options();
		
		options.addOption(ARG0[0], ARG0[1], false, ARG0[2]);
		options.addOption(ARG1[0], ARG1[1], false, ARG1[2]);
		options.addOption(ARG2[0], ARG2[1], false, ARG2[2]);
		options.addOption(ARG3[0], ARG3[1], false, ARG3[2]);
		options.addOption(ARG4[0], ARG4[1], true, ARG4[2]);
		options.addOption(ARG5[0], ARG5[1], true, ARG5[2]);
		options.addOption(ARG6[0], ARG6[1], true, ARG6[2]);
		options.addOption(ARG7[0], ARG7[1], true, ARG7[2]);
		options.addOption(ARG8[0], ARG8[1], true, ARG8[2]);
		options.addOption(ARG9[0], ARG9[1], true, ARG9[2]);
		options.addOption(ARG10[0], ARG10[1], true, ARG10[2]);
		options.addOption(ARG11[0], ARG11[1], true, ARG11[2]);
		options.addOption(ARG12[0], ARG12[1], true, ARG12[2]);
		options.addOption(ARG13[0], ARG13[1], true, ARG13[2]);
		options.addOption(ARG14[0], ARG14[1], true, ARG14[2]);
		options.addOption(ARG15[0], ARG15[1], true, ARG15[2]);
		options.addOption(ARG16[0], ARG16[1], true, ARG16[2]);
		options.addOption(ARG17[0], ARG17[1], true, ARG17[2]);
		options.addOption(ARG18[0], ARG18[1], true, ARG18[2]);
		options.addOption(ARG19[0], ARG19[1], true, ARG19[2]);
		
		return options;
	}
	
	private static void parse(Options options, String[] args) {
		usedArgs = new ArrayList<String>();
		unusedArgs = new ArrayList<String>();
		
		CommandLineParser parser = new DefaultParser();
		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );
		    
		    if (line.hasOption(ARG0[0])) {
		    	// automatically generate and print the help statement
		    	HelpFormatter formatter = new HelpFormatter();
		    	formatter.printHelp( "PacmanANN", options );
		    	System.exit(0);
		    }
		    if (line.hasOption(ARG1[0])) {
		    	Evolver.evolve = true;
		    	usedArgs.add(ARG1[0]);
		    }
		    if (line.hasOption(ARG2[0])) {
		    	Evolver.readOld = true;
		    	usedArgs.add(ARG2[0]);
		    }
		    if (line.hasOption(ARG3[0])) {
		    	Evolver.infinity = true;
		    	usedArgs.add(ARG3[0]);
		    }
		    if (line.hasOption(ARG4[0])) {
		    	Evolver.hiddenSize = Integer.parseInt(line.getOptionValue(ARG4[0]));
		    	usedArgs.add(ARG4[0]);
		    }
		    if (line.hasOption(ARG5[0])) {
		    	Evolver.mutationProbability = Double.parseDouble(line.getOptionValue(ARG5[0]));
		    	usedArgs.add(ARG5[0]);
		    }
		    if (line.hasOption(ARG6[0])) {
		    	Evolver.intensity = Double.parseDouble(line.getOptionValue(ARG6[0]));
		    	usedArgs.add(ARG6[0]);
		    }
		    if (line.hasOption(ARG7[0])) {
		    	Evolver.readGen = Integer.parseInt(line.getOptionValue(ARG7[0]));
		    	usedArgs.add(ARG7[0]);
		    }
		    if (line.hasOption(ARG8[0])) {
		    	Evolver.crossoverProbability = Double.parseDouble(line.getOptionValue(ARG8[0]));
		    	usedArgs.add(ARG8[0]);
		    }
		    if (line.hasOption(ARG9[0])) {
		    	Evolver.sizeOfGeneration = Integer.parseInt(line.getOptionValue(ARG9[0]));
		    	usedArgs.add(ARG9[0]);
		    }
		    if (line.hasOption(ARG10[0])) {
		    	Evolver.elitists = Integer.parseInt(line.getOptionValue(ARG10[0]));
		    	usedArgs.add(ARG10[0]);
		    }
		    if (line.hasOption(ARG11[0])) {
		    	Evolver.terminalFitness = Integer.parseInt(line.getOptionValue(ARG11[0]));
		    	usedArgs.add(ARG11[0]);
		    }
		    if (line.hasOption(ARG12[0])) {
		    	Evolver.terminalGeneration = Integer.parseInt(line.getOptionValue(ARG12[0]));
		    	usedArgs.add(ARG12[0]);
		    }
		    if (line.hasOption(ARG13[0])) {
		    	Evolver.saveInterval = Integer.parseInt(line.getOptionValue(ARG13[0]));
		    	usedArgs.add(ARG13[0]);
		    }
		    if (line.hasOption(ARG14[0])) {
		    	Evolver.parents = Integer.parseInt(line.getOptionValue(ARG14[0]));
		    	usedArgs.add(ARG14[0]);
		    }
		    if (line.hasOption(ARG15[0])) {
		    	Evolver.numberOfEvaluationsPerChild = Integer.parseInt(line.getOptionValue(ARG15[0]));
		    	usedArgs.add(ARG15[0]);
		    }
		    if (line.hasOption(ARG16[0])) {
		    	switch (line.getOptionValue(ARG16[0])) {
		    	case "byRank": 		Evolver.selection = SelectionCriteria.StochasticallyBasedOnRank; break;
		    	case "byFitness": 	Evolver.selection = SelectionCriteria.StochasticallyBasedOnFitness; break;
		    	case "Fittest": 	Evolver.selection = SelectionCriteria.Fittest; break;
		    	default: 			System.out.println("\"" + line.getOptionValue(ARG1[16]) + "\" is not a valid argument for -" + ARG16[0]); 
		    						System.exit(1); break;
		    	}
		    	usedArgs.add(ARG16[0]);
		    }
		    if (line.hasOption(ARG17[0])) {
		    	switch (line.getOptionValue(ARG17[0])) {
		    	case "mutation": 	Evolver.spawn = SpawnCriteria.Mutation; break;
		    	case "crossover": 	Evolver.spawn = SpawnCriteria.Crossover;; break;
		    	default: 			System.out.println("\"" + line.getOptionValue(ARG17[0]) + "\" is not a valid argument for -" + ARG17[0]); 
		    						System.exit(1); break;
		    	}
		    	usedArgs.add(ARG17[0]);
		    }
		    if (line.hasOption(ARG18[0])) {
		    	Evolver.initialWeight = Double.parseDouble(line.getOptionValue(ARG18[0]));
		    	usedArgs.add(ARG18[0]);
		    }
		    if (line.hasOption(ARG19[0])) {
		    	Evolver.initialBias = Double.parseDouble(line.getOptionValue(ARG19[0]));
		    	usedArgs.add(ARG19[0]);
		    }
		}
		catch( ParseException exp ) {
			System.out.println("The program encountered an error trying to parse your input...");
			System.out.println( "Unexpected exception:" + exp.getMessage() );
			System.exit(1);
		}
	}
	
	private static ArrayList<String> getUsedArgs() {
		return usedArgs;
	}
	
	private static ArrayList<String> getUnusedArgs() {
		unusedArgs = new ArrayList<String>();
		
		if (!usedArgs.contains(ARG1[0])) {
			unusedArgs.add(ARG1[0]);
		}
		if (!usedArgs.contains(ARG2[0])) {
			unusedArgs.add(ARG2[0]);
		}
		if (!usedArgs.contains(ARG3[0])) {
			unusedArgs.add(ARG3[0]);
		}
		if (!usedArgs.contains(ARG4[0])) {
			unusedArgs.add(ARG4[0]);
		}
		if (!usedArgs.contains(ARG5[0])) {
			unusedArgs.add(ARG5[0]);
		}
		if (!usedArgs.contains(ARG6[0])) {
			unusedArgs.add(ARG6[0]);
		}
		if (!usedArgs.contains(ARG7[0])) {
			unusedArgs.add(ARG7[0]);
		}
		if (!usedArgs.contains(ARG8[0])) {
			unusedArgs.add(ARG8[0]);
		}
		if (!usedArgs.contains(ARG9[0])) {
			unusedArgs.add(ARG9[0]);
		}
		if (!usedArgs.contains(ARG10[0])) {
			unusedArgs.add(ARG10[0]);
		}
		if (!usedArgs.contains(ARG11[0])) {
			unusedArgs.add(ARG11[0]);
		}
		if (!usedArgs.contains(ARG12[0])) {
			unusedArgs.add(ARG12[0]);
		}
		if (!usedArgs.contains(ARG13[0])) {
			unusedArgs.add(ARG13[0]);
		}
		if (!usedArgs.contains(ARG14[0])) {
			unusedArgs.add(ARG14[0]);
		}
		if (!usedArgs.contains(ARG15[0])) {
			unusedArgs.add(ARG15[0]);
		}
		if (!usedArgs.contains(ARG16[0])) {
			unusedArgs.add(ARG16[0]);
		}
		if (!usedArgs.contains(ARG17[0])) {
			unusedArgs.add(ARG17[0]);
		}
		if (!usedArgs.contains(ARG18[0])) {
			unusedArgs.add(ARG18[0]);
		}
		if (!usedArgs.contains(ARG19[0])) {
			unusedArgs.add(ARG19[0]);
		}
		
		return unusedArgs;
	}
	
	private static void printArgs(ArrayList<String> argList) {
		/*
		 * The second string begins after 40 characters. The dash means that the
		 * first string is left-justified.
		 */
		String format = "%-40s%s%n";
		
		if (argList.contains(ARG1[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.evolve);
		}
		if (argList.contains(ARG2[0])) {
			System.out.printf(format, "-" + ARG2[0], Evolver.evolve);
		}
		if (argList.contains(ARG3[0])) {
			System.out.printf(format, "-" + ARG3[0], Evolver.evolve);
		}
		if (argList.contains(ARG4[0])) {
			System.out.printf(format, "-" + ARG4[0], Evolver.evolve);
		}
		if (argList.contains(ARG5[0])) {
			System.out.printf(format, "-" + ARG5[0], Evolver.evolve);
		}
		if (argList.contains(ARG6[0])) {
			System.out.printf(format, "-" + ARG6[0], Evolver.evolve);
		}
		if (argList.contains(ARG7[0])) {
			System.out.printf(format, "-" + ARG7[0], Evolver.evolve);
		}
		if (argList.contains(ARG8[0])) {
			System.out.printf(format, "-" + ARG8[0], Evolver.evolve);
		}
		if (argList.contains(ARG9[0])) {
			System.out.printf(format, "-" + ARG9[0], Evolver.evolve);
		}
	/*	if (argList.contains(ARG10[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG11[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG12[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG13[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG14[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG15[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG16[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG17[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG18[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}
		if (argList.contains(ARG19[0])) {
			System.out.printf(format, "-" + ARG1[0], Evolver.);
		}*/
		
	}
}
