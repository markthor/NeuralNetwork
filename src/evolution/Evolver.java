package evolution;

import io.IOManager;

import java.util.ArrayList;
import java.util.List;

import network.Network;
import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;
import adapter.AdvancedNeuralNetworkController;
import adapter.NeuralNetworkController;
import cli.CLIParser;


public class Evolver {

	public static boolean evolve;
	public static boolean readOld;
	public static int readGen;
	
	// Evolution parameters
	public static int hiddenSize;
	public static double chanceOfMutation;
	public static double intensity;
	public static int children;
	public static int elitists;
	public static int terminalFitness;
	public static int terminalGeneration;
	public static int numberOfGenerations;
	public static int saveInterval;
	
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
		readOld = false;
		readGen = 0;
		
		// Evolution parameters
		hiddenSize = 20;
		chanceOfMutation = 0.0517;
		intensity = 0.443;
		children = 30;
		elitists = 5;
		terminalFitness = 1300;
		terminalGeneration = 500;
		saveInterval = 100;
	}
	
	private static void startSimulation() {
		Executor exec=new Executor();
		if (!evolve) {
			numberOfGenerations = 1;
			Species species = new Species(25, hiddenSize, 5);
			Genome genome;
			if(readOld) {
				genome = IOManager.readGenome(1,1);
			} else {
				genome = new Genome(species, 0, 0);
			}
			
			Network network = new Network(genome, species);
			List<Network> networks = new ArrayList<Network>();
			networks.add(network);
			Generation generation = new Generation(0, networks);
			AdvancedNeuralNetworkController controller = new AdvancedNeuralNetworkController(network);
			controller.setCurrentGeneration(generation);
			
			exec.runGameTimed(controller,new StarterGhosts(),true);
		} else {
			evolutionLoop();
		}
	}
	
	private static void evolutionLoop() {
		numberOfGenerations = 1;
		Species species = new Species(25, hiddenSize, 5);
		Genome currentGenome;
		if (readOld) {
			currentGenome = IOManager.readGenome(1, 1);
		} else {
			currentGenome = new Genome(species, 0, 0);
		}
		Network currentNetwork = new Network(currentGenome, species);
		Generation currentGeneration = new Generation(numberOfGenerations, currentNetwork, children, chanceOfMutation, intensity);
		currentGeneration.evenAllFitnessValues();
		AdvancedNeuralNetworkController controller = new AdvancedNeuralNetworkController(currentNetwork);
		
		Executor exec = new Executor();
		
		do {
			System.out.println("New generation number: " + numberOfGenerations);
			currentGeneration = new Generation(numberOfGenerations, currentGeneration, children, elitists, chanceOfMutation, intensity);
			controller.setCurrentGeneration(currentGeneration);
			
			for(int i = 0; i < children; i++) {
				controller.setNetwork(currentGeneration.getNetwork(i));
				exec.runGame(controller,new StarterGhosts(),false, 0);
			}
			System.out.println("Total fitness: " + currentGeneration.totalFitness());
			System.out.println("Highest fitness: " + currentGeneration.highestFitness());
			
			if(numberOfGenerations % saveInterval == 0) {
				currentGeneration.saveGeneration(elitists);
			}
			
			numberOfGenerations++;
		} while(currentGeneration.highestFitness() < terminalFitness && currentGeneration.getNumber() < terminalGeneration);
		currentGeneration.saveGeneration(elitists);
	}
}
