package evolution;

import io.IOManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import network.Network;
import tools.MapTool;
import tools.MathTool;

public class Generation {
	private Map<Network, Integer> networksWithFitness;
	private int number;

	/***
	 * Seeds a generation with multiple parents, each one seeding childrenPerParent children,
	 */
	public Generation(int number, List<Network> multipleParents, int childrenPerParent, double chanceOfMutation, double intensity) {
		List<Network> inputNetworks = new ArrayList<Network>();
		inputNetworks.addAll(multipleParents);
		
		for(Network parent: multipleParents) {
			for(int i = 0; i < childrenPerParent; i++) {
				inputNetworks.add(spawnChild(parent, chanceOfMutation, intensity));
			}
		}
		
		constructGeneration(number, inputNetworks);
	}
	
	/***
	 * Seeds a generation from a previous generation, selecting the parent stochastically based on fitness and keeping the numberOfElitists best networks unmodified.
	 */
	public Generation(int number, Generation lastGeneration, int numberOfNetworks, int numberOfElitists, double chanceOfMutation, double intensity) {
		List<Network> inputNetworks = new ArrayList<Network>(numberOfNetworks);
		
		Network parent = lastGeneration.selectParentToNextGeneration();
		List<Network> eliteNetworks = lastGeneration.getTopNetworksWithHighestFitness(numberOfElitists);
		inputNetworks.addAll(eliteNetworks);
		
		for(int i = 0; i < numberOfNetworks-numberOfElitists; i++) {
			Genome mutatedGenome = parent.getGenome().clone();
			mutatedGenome.mutate(chanceOfMutation, intensity);
			inputNetworks.add(new Network(mutatedGenome, parent.getSpecies()));
		}
		
		assert inputNetworks.size() == numberOfNetworks;
		constructGeneration(number, inputNetworks);
	}
	
	/***
	 * Seeds a generation from a single parent.
	 */
	public Generation(int number, Network parent, int numberOfNetworks, double chanceOfMutation, double intensity) {
		List<Network> inputNetworks = new ArrayList<Network>(numberOfNetworks);
		for(int i = 0; i < numberOfNetworks-1; i++) {
			Genome mutatedGenome = parent.getGenome().clone();
			mutatedGenome.mutate(chanceOfMutation, intensity);
			inputNetworks.add(new Network(mutatedGenome, parent.getSpecies()));
		}
		inputNetworks.add(new Network(parent.getGenome().clone(), parent.getSpecies()));
		
		assert inputNetworks.size() == numberOfNetworks;
		constructGeneration(number, inputNetworks);
	}

	/***
	 * Seeds a generation from a predefined list of networks.
	 */
	public Generation(int number, List<Network> inputNetworks) {
		constructGeneration(number, inputNetworks);
	}
	
	private void constructGeneration(int number, List<Network> inputNetworks) {
		this.number = number;
		networksWithFitness = new HashMap<Network, Integer>();
		for(Network n: inputNetworks) {
			networksWithFitness.put(n, -1);
		}
	}
	
	private Network spawnChild(Network parent, double chanceOfMutation, double intensity) {
		Genome mutatedGenome = parent.getGenome().clone();
		mutatedGenome.mutate(chanceOfMutation, intensity);
		return new Network(mutatedGenome, parent.getSpecies());
	}
	
	public void addFitnessToNetwork(int fitness, Network network) {
		if(!networksWithFitness.containsKey(network)) {
			throw new IllegalStateException("Tried to add fitness to a network that did not exist in the generation");
		}
		networksWithFitness.put(network, fitness);
	}
	
	public int getFitnessOfNetwork(Network network) {
		return networksWithFitness.get(network);
	}
	
	public List<Network> getTopNetworksWithHighestFitness(int numberOfElitists) {
		List<Network> result = new ArrayList<Network>();
		networksWithFitness = MapTool.sortByValue(networksWithFitness);
		
		for(Entry<Network, Integer> nf : networksWithFitness.entrySet()) {
			if(result.size() < numberOfElitists)
			result.add(nf.getKey());
		}
		
		return result;
	}
	
	public Network selectParentToNextGeneration() {
		if(networksWithFitness.keySet().size() == 0) {
			throw new IllegalStateException("The generation must have networks to select the fittest");
		}
		for(Integer i :networksWithFitness.values()) {
			if(i < 0) {
				System.out.println(Arrays.toString(networksWithFitness.values().toArray(new Integer[0])));
				throw new IllegalStateException("Not all fitness values have been updated");
			}
		}
		
		int totalFitness = 0;
		for(Integer n: networksWithFitness.values()) {
			totalFitness += n;
		}
		
		int selector = MathTool.getIntBetweenZeroAnd(totalFitness);
		int accumulatedFitness = 0;
		for(Entry<Network, Integer> entry: networksWithFitness.entrySet()) {
			if(isIncludedInInterval(selector, accumulatedFitness, accumulatedFitness+entry.getValue())) {
				return entry.getKey();
			} else {
				accumulatedFitness += entry.getValue();
			}
		}
		
		System.err.println("Should not happen, revise code");
		return null;
	}
	
	public void evenAllFitnessValues() {
		for(Entry<Network, Integer> nf: networksWithFitness.entrySet()) {
			addFitnessToNetwork(1, nf.getKey());
		}
	}
	
	public void saveGeneration(int elitistsToSave) {
		IOManager.saveMultipleGenomesToFile(1, Network.networksToGenomes(getTopNetworksWithHighestFitness(elitistsToSave)));
		IOManager.saveGenerationToFile(this, number);
	}
	
	private boolean isIncludedInInterval(int input, int lower, int upper) {
		return input > lower && input <= upper;
	}
	
	public List<Network> getNetworks() {
		return new ArrayList<Network>(Arrays.asList(networksWithFitness.keySet().toArray(new Network[0])));
	}
	
	public Network getNetwork(int index) {
		return getNetworks().get(index);
	}
	
	public int highestFitness() {
		return getFitnessOfNetwork(getTopNetworksWithHighestFitness(1).get(0));
	}
	
	public int totalFitness() {
		int result = 0;
		for(Entry<Network, Integer> entry: networksWithFitness.entrySet()) {
			result += entry.getValue();
		}
		return result;
	}
	
	public int getNumber() {
		return number;
	}
	
	@Override
	public String toString() {
		return "Generation number: " + number + "\n" + networksWithFitness;
	}
}
