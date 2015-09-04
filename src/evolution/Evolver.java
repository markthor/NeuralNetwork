package evolution;

import io.IOManager;

import java.util.ArrayList;
import java.util.List;

import network.Network;
import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;
import adapter.AdvancedNeuralNetworkController;
import adapter.NeuralNetworkController;

public class Evolver {
	public final static boolean evolve = true;
	public final static boolean readOld = false;
	public final static int readNum = 0;
	
	// Evolution parameters
	public static int hiddenSize = 20;
	public static int numberOfGenerations = 0;
	public static double chanceOfMutation = 0.0517;
	public static double intensity = 0.443;
	public static int children = 30;
	public static int elitists = 5;
	public static int terminalFitness = 1300;
	public static int terminalGeneration = 2;
	
	/**
	 * The main method. Several options are listed - simply remove comments to use the option you want.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		Executor exec=new Executor();
		if (!evolve) {
			Species species = new Species(25, hiddenSize, 5);
			Genome genome = IOManager.readGenomeNumber(0);
			Network network = new Network(genome, species);
			
			List<Network> networks = new ArrayList<Network>();
			networks.add(network);
			Generation generation = new Generation(0, networks);
			NeuralNetworkController controller = new NeuralNetworkController(network);
			controller.setCurrentGeneration(generation);
			
			exec.runGameTimed(controller,new StarterGhosts(),true);
		} else {
			evolutionLoop();
		}
	}
	
	private static void evolutionLoop() {
		Species species = new Species(25, hiddenSize, 5);
		//Specipes species = new Species(4, 4, 4);
		Genome currentGenome;
		if (readOld) {
			currentGenome = IOManager.readGenomeNumber(readNum);
		} else {
			currentGenome = new Genome(species, 0, 0);
		}
		Network currentNetwork = new Network(currentGenome, species);
		Generation currentGeneration = new Generation(numberOfGenerations, currentNetwork, children, chanceOfMutation, intensity);
		currentGeneration.evenAllFitnessValues();
		AdvancedNeuralNetworkController controller = new AdvancedNeuralNetworkController(currentNetwork);
		//SimpleNeuralNetworkController controller = new SimpleNeuralNetworkController(currentNetwork);
		
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
			numberOfGenerations++;
		} while(currentGeneration.highestFitness() < terminalFitness && currentGeneration.getNumber() < terminalGeneration);
		IOManager.saveMultipleGenomesToFile(Network.networksToGenomes(currentGeneration.getTopNetworksWithHighestFitness(elitists)));
	}
}
