package cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import evolution.Evolver;

public class CLIParser {
	public static void readArgs(String[] args) {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the options
		Options options = new Options();
		
		options.addOption("e", "evolve", false, "Evolve (Sideeffect: No GUI");
		options.addOption("r", "read-old", false, "Read old generations and continue");
		options.addOption("g", "read-gen", true, "Read the N'th generation-file (int)");
		options.addOption("s", "hidden-size", true, "Size of the hidden layer (int)");
		options.addOption("m", "chance", true, "Chance of a mutation (double)");
		options.addOption("i", "intensity", true, "Intensity of mutation (double)");
		options.addOption("I", "infinity", false, "Simulation never terminates (ignores terminal fitness and terminal generation)");
		options.addOption("c", "children", true, "Number of children (int)");
		options.addOption("E", "elitists", true, "Number of elitists (int)");
		options.addOption("tf", "terminal-fitness", true, "Terminal fitness (int)");
		options.addOption("tg", "terminal-generation", true, "Terminal generation (int)");
		options.addOption("h", "help", false, "Show this help message");
		options.addOption("si", "save-interval", true, "Save every N'th generation to file (int)");
		options.addOption("w", "weight", true, "Initial weight (double)");
		options.addOption("b", "bias", true, "Initial bias (double)");
		
		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );

		    // validate that block-size has been set
		    if (line.hasOption("e")) {
		    	Evolver.evolve = true;
		    }
		    if (line.hasOption("r")) {
		    	Evolver.readOld = true;
		    }
		    if (line.hasOption("I")) {
		    	Evolver.infinity = true;
		    }
		    if (line.hasOption("g")) {
		    	Evolver.readGen = Integer.parseInt(line.getOptionValue("g"));
		    }
		    if (line.hasOption("s")) {
		    	Evolver.hiddenSize = Integer.parseInt(line.getOptionValue("s"));
		    }
		    if (line.hasOption("m")) {
		    	Evolver.chanceOfMutation = Double.parseDouble(line.getOptionValue("m"));
		    }
		    if (line.hasOption("i")) {
		    	Evolver.intensity = Double.parseDouble(line.getOptionValue("i"));
		    }
		    if (line.hasOption("c")) {
		    	Evolver.sizeOfGeneration = Integer.parseInt(line.getOptionValue("c"));
		    }
		    if (line.hasOption("E")) {
		    	Evolver.elitists = Integer.parseInt(line.getOptionValue("E"));
		    }
		    if (line.hasOption("tf")) {
		    	Evolver.terminalFitness = Integer.parseInt(line.getOptionValue("tf"));
		    }
		    if (line.hasOption("tg")) {
		    	Evolver.terminalGeneration = Integer.parseInt(line.getOptionValue("tg"));
		    }
		    if (line.hasOption("si")) {
		    	Evolver.saveInterval = Integer.parseInt(line.getOptionValue("si"));
		    }
		    if (line.hasOption("w")) {
		    	Evolver.initialWeight = Double.parseDouble(line.getOptionValue("w"));
		    }
		    if (line.hasOption("b")) {
		    	Evolver.initialBias = Double.parseDouble(line.getOptionValue("b"));
		    }
		    if (line.hasOption("h")) {
		    	// automatically generate and print the help statement
		    	HelpFormatter formatter = new HelpFormatter();
		    	formatter.printHelp( "PacmanANN", options );
		    	System.exit(0);
		    }
		}
		catch( ParseException exp ) {
		    System.out.println( "Unexpected exception:" + exp.getMessage() );
		}
	}
}
