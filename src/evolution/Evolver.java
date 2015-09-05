package evolution;

import io.IOManager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import network.Network;
import pacman.Executor;
import pacman.controllers.Controller;
import pacman.controllers.examples.AggressiveGhosts;
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
	public static double chanceOfMutation;
	public static double intensity;
	public static int children;
	public static int childrenPerParent;
	public static int elitists;
	public static int terminalFitness;
	public static int terminalGeneration;
	public static int numberOfGenerations;
	public static int saveInterval;
	public static double initialWeight;
	public static double initialBias;
	
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
		evolve = false;

		readOld = true;

		infinity = true;
		readGen = 0;
		
		// Evolution parameters
		hiddenSize = 12;
		chanceOfMutation = 0.0517;
		intensity = 0.443;
		children = 100;
		childrenPerParent = 2;
		elitists = 40;
		terminalFitness = 1300;
		terminalGeneration = 500;
		saveInterval = 100;
		initialWeight = 0.0;
		initialBias = 0.0;
		

		species = new Species(10, hiddenSize, 4);
		controller = new BlamBlamNeuralNetworkController(null);
//		species = new Species(12, hiddenSize, 4);
//		controller = new EvaluationNeuralNetworkController(null);
		ghostController = new AggressiveGhosts();
	}
	
	private static void startSimulation() {
		Executor exec=new Executor();
		if (!evolve) {
			numberOfGenerations = 1;

			Genome genome;
			if(readOld) {
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
		numberOfGenerations = 1;
		Genome currentGenome;
		Generation currentGeneration;
		Network currentNetwork;
		if (readOld) {
			int latestGenerationNumber = IOManager.getLatestGenerationNumber();
			List<Genome> latestGenomes;
			latestGenomes = IOManager.readGenomesFromLatestGeneration();
			currentGeneration = new Generation(latestGenerationNumber, Network.genomesToNetwork(latestGenomes, species), childrenPerParent, chanceOfMutation, intensity);
			children = childrenPerParent * latestGenomes.size();
			numberOfGenerations = latestGenerationNumber;
		} else {
			currentGenome = new Genome(species, initialWeight, initialBias);
			currentNetwork = new Network(currentGenome, species);
			currentGeneration = new Generation(numberOfGenerations, currentNetwork, children, chanceOfMutation, intensity);
		}
		
		currentGeneration.evenAllFitnessValues();
		
		Executor exec = new Executor();
		
		do {
			System.out.println("New generation number: " + numberOfGenerations);
			currentGeneration = new Generation(numberOfGenerations, currentGeneration, children, elitists, chanceOfMutation, intensity);
			controller.setCurrentGeneration(currentGeneration);
			
			for(int i = 0; i < children; i++) {
				controller.setNetwork(currentGeneration.getNetwork(i));
				exec.runGame(controller,ghostController,false, 0);
			}
			System.out.println("Total fitness: " + currentGeneration.totalFitness());
			System.out.println("Highest fitness: " + currentGeneration.highestFitness());
			
			if(numberOfGenerations % saveInterval == 0) {
				currentGeneration.saveGeneration(elitists);
			}
			
			numberOfGenerations++;
		} while(keepEvolving(currentGeneration));
		currentGeneration.saveGeneration(elitists);
	}
	
	private static boolean keepEvolving(Generation generation) {
		if(infinity) return true;
		if(generation.highestFitness() < terminalFitness && generation.getNumber() < terminalGeneration) return true;
		return false;
	}
	
	private static void setSpeciesAndControllerToEvaluationBased(Species species, NeuralNetworkController controller) {

	}
}
