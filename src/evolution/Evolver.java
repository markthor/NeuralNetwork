package evolution;

import io.IOManager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import network.Network;
import pacman.Executor;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import adapter.BlamBlamNeuralNetworkController;
import adapter.EvaluationNeuralNetworkController;
import adapter.NeuralNetworkController;
import cli.CLIParser;


public class Evolver {

	public static boolean evolve;
	public static boolean readOld;
	public static boolean infinity;
	public static int readGen;
	
	// Evolution parameters
	public static int hiddenSize;
	public static double mutationProbability;
	public static double intensity;
	public static double crossoverProbability;
	public static int sizeOfGeneration;
	public static int elitists;
	public static int parents;
	public static int terminalFitness;
	public static int terminalGeneration;
	public static int numberOfGenerations;
	public static int saveInterval;
	public static int numberOfGenomesToSave;
	public static int numberOfEvaluationsPerChild;
	public static double initialWeight;
	public static double initialBias;
	
	public static SelectionCriteria selection;
	public static SpawnCriteria spawn;
	
	public static NeuralNetworkController controller;
	public static Species species;
	public static Controller<EnumMap<GHOST,MOVE>> ghostController;
	
	/**
	 * The main method. Several options are listed - simply remove comments to use the option you want.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		setDefaultArgs();
		CLIParser.readArgs(args);
		startSimulation();
	}
	
	private static void setDefaultArgs() {

		evolve = true;
		readOld = true;
		infinity = true;
		readGen = 0;
		
		// Evolution parameters
		hiddenSize = 8;
		mutationProbability = 0.08;
		intensity = 0.2;
		crossoverProbability = 0.2;
		sizeOfGeneration = 35;
		elitists = 5;
		parents = 2;
		terminalFitness = 1300;
		terminalGeneration = 500;
		saveInterval = 100;
		numberOfGenomesToSave = 5;
		initialWeight = 0.0;
		initialBias = 0.0;
		numberOfEvaluationsPerChild = 3;
		
		selection = SelectionCriteria.StochasticallyBasedOnRank;
		spawn = SpawnCriteria.Mutation;

//		species = new Species(16, hiddenSize, 4);
//		controller = new BlamBlamNeuralNetworkController(null);
		species = new Species(14, hiddenSize, 4);
		controller = new EvaluationNeuralNetworkController(null);
		ghostController = new StarterGhosts();
	}
	
	private static void startSimulation() {
		Executor exec=new Executor();
		if (!evolve) {
			numberOfGenerations = 1;

			Genome genome;
			if(readOld) {
				if(readGen != 0) {
					genome = IOManager.readMultipleGenomes(readGen).get(0);
				}
				genome = IOManager.readGenomesFromLatestGeneration().get(0);
			} else {
				genome = new Genome(species, 0, 0);
			}
			
			Network network = new Network(genome, species);
			List<Network> networks = new ArrayList<Network>();
			networks.add(network);
			Generation generation = new Generation(0, networks);

			controller.setNetwork(network);

			controller.setCurrentGeneration(generation);
			
			exec.runGameTimed(controller,ghostController,true);
		} else {
			evolutionLoop();
		}
	}
	
	private static void evolutionLoop() {
		System.out.println("Beginning evolution...");
		numberOfGenerations = 1;
		Genome currentGenome;
		Generation currentGeneration;
		Network currentNetwork;
		currentGenome = new Genome(species, initialWeight, initialBias);
		currentNetwork = new Network(currentGenome, species);
		currentGeneration = new Generation(numberOfGenerations, currentNetwork, sizeOfGeneration, mutationProbability, intensity);
		currentGeneration.evenAllFitnessValues();
		
		boolean firstRun = true;
		do {
			if(readOld && firstRun) {
				if(readGen == 0) {
					numberOfGenerations = IOManager.getLatestGenerationNumber();
					currentGeneration = new Generation(numberOfGenerations, Network.genomesToNetwork(IOManager.readGenomesFromLatestGeneration(), species));
				} else {
					numberOfGenerations = readGen;
					currentGeneration = new Generation(numberOfGenerations, Network.genomesToNetwork(IOManager.readMultipleGenomes(numberOfGenerations), species));
				}
			} else {
				currentGeneration = new Generation(numberOfGenerations, sizeOfGeneration, elitists, parents, currentGeneration, selection, spawn, mutationProbability, intensity, crossoverProbability);
			}
			
			evaluateGeneration(currentGeneration, controller);
			
			if(numberOfGenerations % saveInterval == 0) currentGeneration.saveGeneration();
			
			printStatusToTerminal(currentGeneration);
			numberOfGenerations++;
			firstRun = false;
		} while(keepEvolving(currentGeneration));
		currentGeneration.saveGeneration();
	}
	
	private static void evaluateGeneration(Generation currentGeneration, NeuralNetworkController controller) {
		controller.setCurrentGeneration(currentGeneration);
		for(int i = 0; i < currentGeneration.getSize(); i++) {
			controller.setNetwork(currentGeneration.getNetwork(i));
			controller.resetNumberOfTries();
			for(int j = 0; j < numberOfEvaluationsPerChild; j++) {
				Executor exec = new Executor();
				exec.runGame(controller,ghostController,false, 0);
				controller.incrementNumberOfTries();
			}
		}
	}
	
	private static void printStatusToTerminal(Generation currentGeneration) {
		System.out.println("Generation: " + currentGeneration.getNumber());
		System.out.println("Average fitness: " + currentGeneration.averageFitness());
		System.out.println("Highest fitness: " + currentGeneration.highestFitness());
	}
	
	private static boolean keepEvolving(Generation generation) {
		if(infinity) return true;
		if(generation.highestFitness() < terminalFitness && generation.getNumber() < terminalGeneration) return true;
		return false;
	}
}
