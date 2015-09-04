package evolution;

import io.IOManager;

import java.util.ArrayList;
import java.util.List;

import network.Network;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cli.CLIParser;

import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;
import adapter.AdvancedNeuralNetworkController;
import adapter.NeuralNetworkController;

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
		terminalGeneration = 2;
	}
	
	private static void startSimulation() {
		Executor exec=new Executor();
		if (!evolve) {
			numberOfGenerations = 1;
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
			currentGenome = IOManager.readGenomeNumber(readGen);
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
