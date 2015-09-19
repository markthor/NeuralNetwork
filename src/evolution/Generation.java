package evolution;

import io.IOManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import network.Genome;
import network.Network;
import tools.CollectionTool;
import tools.MathTool;

public class Generation {
	private Map<Network, Evaluation> networksWithFitness;
	private int number;

	public Generation(int number, int size, int numberOfElitists, int numberOfParents, Generation previousGeneration, SelectionCriteria selection, SpawnCriteria spawn, double chanceOfMutation, double intensity, double crossoverProbability) {
		
		List<Network> inputNetworks = new ArrayList<Network>();
		List<Network> parents = new ArrayList<Network>();
		
		inputNetworks.addAll(previousGeneration.getTopNetworksWithHighestFitness(numberOfElitists));
		
		if(selection == SelectionCriteria.All) {
			parents.addAll(previousGeneration.getTopNetworksWithHighestFitness(size-numberOfElitists));
			numberOfParents = size-numberOfElitists;
		}
		if(selection == SelectionCriteria.Fittest) {
			parents.addAll(previousGeneration.getTopNetworksWithHighestFitness(numberOfParents));
		}
		if(selection == SelectionCriteria.StochasticallyBasedOnFitness) {
			for(int i = 0; i < numberOfParents; i++) {
				parents.add(previousGeneration.selectParentToNextGenerationBasedOnFitness());
			}
		}
		if(selection == SelectionCriteria.StochasticallyBasedOnRank) {
			for(int i = 0; i < numberOfParents; i++) {
				parents.add(previousGeneration.selectParentToNextGenerationBasedOnRank());
			}
		}
		
		if(spawn == SpawnCriteria.Mutation) {
			int childrenPerParent = (size-numberOfElitists)/numberOfParents;
			int additionalChildren = (size-numberOfElitists)%numberOfParents;
			for(Network parent: parents) {
				for(int i = 0; i < childrenPerParent; i++) {
					inputNetworks.add(spawnChild(parent, chanceOfMutation, intensity));
				}
			}
			for(int i = 0; i<additionalChildren; i++) {
				inputNetworks.add(spawnChild(parents.get(0), chanceOfMutation, intensity));
			}
		}
		if(spawn == SpawnCriteria.Crossover) {
			int numberOfChildren = size-numberOfElitists;
			for(int i = 0; i < numberOfChildren; i++) {
				if(MathTool.getDoubleBetweenZeroAnd(1) < crossoverProbability) {
					inputNetworks.add(spawnChild(CollectionTool.getRandomElement(parents), CollectionTool.getRandomElement(parents)));
				} else {
					inputNetworks.add(spawnChild(CollectionTool.getRandomElement(parents), chanceOfMutation, intensity));
				}
			}
		}
		
		if(inputNetworks.size() != size) {
			throw new IllegalArgumentException("Construction parameters did not add up.");
		}
		constructGeneration(number, inputNetworks);
	}
	
	public Generation(int number, List<Network> parents, int size, double chanceOfMutation, double intensity) {
		List<Network> inputNetworks = new ArrayList<Network>();
		inputNetworks.addAll(parents);
		
		int childrenPerParent = (size-parents.size())/parents.size();
		int additionalChildren = (size-parents.size())%parents.size();
		for(Network parent: parents) {
			for(int i = 0; i < childrenPerParent; i++) {
				inputNetworks.add(spawnChild(parent, chanceOfMutation, intensity));
			}
		}
		for(int i = 0; i<additionalChildren; i++) {
			inputNetworks.add(spawnChild(parents.get(0), chanceOfMutation, intensity));
		}
		
		constructGeneration(number, inputNetworks);
	}
	
	/***
	 * Seeds a generation from a single parent.
	 */
	public Generation(int number, Network parent, int size, double chanceOfMutation, double intensity) {
		List<Network> inputNetworks = new ArrayList<Network>(size);
		for(int i = 0; i < size-1; i++) {
			Genome mutatedGenome = parent.getGenome().clone();
			mutatedGenome.mutateHeavy(-10, 10);
			inputNetworks.add(new Network(mutatedGenome, parent.getSpecies()));
		}
		inputNetworks.add(new Network(parent.getGenome().clone(), parent.getSpecies()));
		
		assert inputNetworks.size() == size;
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
		networksWithFitness = new HashMap<Network, Evaluation>();
		for(Network n: inputNetworks) {
			networksWithFitness.put(n, new Evaluation());
		}
	}
	
	private Network spawnChild(Network parent, double chanceOfMutation, double intensity) {
		Genome mutatedGenome = parent.getGenome().clone();
		mutatedGenome.mutate(chanceOfMutation, intensity);
		return new Network(mutatedGenome, parent.getSpecies());
	}
	
	private Network spawnChild(Network parent1, Network parent2) {
		Genome combinedGenome = parent1.getGenome().crossover(parent2.getGenome());
		return new Network(combinedGenome, parent1.getSpecies());
	}
	
	public void addFitnessToNetwork(int tryNumber, int fitness, Network network) {
		if(!networksWithFitness.containsKey(network)) {
			throw new IllegalStateException("Tried to add fitness to a network that did not exist in the generation");
		}
		networksWithFitness.get(network).setFitness(tryNumber, fitness);
	}
	
	public double getAverageFitnessOfNetwork(Network network) {
		return networksWithFitness.get(network).getAverageFitness();
	}
	
	public List<Network> getTopNetworksWithHighestFitness(int numberOfElitists) {
		List<Network> result = new ArrayList<Network>();
		networksWithFitness = CollectionTool.sortByValue(networksWithFitness);
		
		for(Entry<Network, Evaluation> ne : networksWithFitness.entrySet()) {
			if(result.size() < numberOfElitists)
			result.add(ne.getKey());
		}
		
		return result;
	}
	
	public Network selectParentToNextGenerationBasedOnFitness() {
		if(networksWithFitness.keySet().size() == 0) {
			throw new IllegalStateException("The generation must have networks to select the fittest");
		}
		for(Evaluation e :networksWithFitness.values()) {
			if(e.getAverageFitness() < 0.0) {
				System.out.println(Arrays.toString(networksWithFitness.values().toArray(new Integer[0])));
				throw new IllegalStateException("Not all fitness values have been updated");
			}
		}
		
		double totalFitness = 0.0;
		for(Evaluation e: networksWithFitness.values()) {
			totalFitness += e.getAverageFitness();
		}
		
		double selector = MathTool.getDoubleBetweenZeroAnd(totalFitness);
		double accumulatedFitness = 0;
		for(Entry<Network, Evaluation> entry: networksWithFitness.entrySet()) {
			if(isIncludedInInterval(selector, accumulatedFitness, accumulatedFitness+entry.getValue().getAverageFitness())) {
				return entry.getKey();
			} else {
				accumulatedFitness += entry.getValue().getAverageFitness();
			}
		}

		throw new IllegalStateException("Should not happen, revise code");
	}
	
	public Network selectParentToNextGenerationBasedOnRank() {
		if(networksWithFitness.keySet().size() == 0) {
			throw new IllegalStateException("The generation must have networks to select the fittest");
		}
		for(Evaluation e :networksWithFitness.values()) {
			if(e.getAverageFitness() < 0.0) {
				System.out.println(Arrays.toString(networksWithFitness.values().toArray(new Integer[0])));
				throw new IllegalStateException("Not all fitness values have been updated");
			}
		}
		
		networksWithFitness = CollectionTool.sortByValue(networksWithFitness);
		//More tun if need be
		int i = 0;
		for(Entry<Network, Evaluation> entry : networksWithFitness.entrySet()) {
			if(MathTool.getDoubleBetweenZeroAnd(1) < 0.5 || i == networksWithFitness.entrySet().size()-1)
				return entry.getKey();
			i++;
		}
		
		throw new IllegalStateException("Should not happen, revise code");
	}
	
	private boolean isIncludedInInterval(double input, double lower, double upper) {
		return input >= lower && input <= upper;
	}
	
	public void evenAllFitnessValues() {
		for(Entry<Network, Evaluation> ne: networksWithFitness.entrySet()) {
			addFitnessToNetwork(1, 1, ne.getKey());
		}
	}
	
	public void saveGeneration() {
		IOManager.saveMultipleGenomesToFile(number, Network.networksToGenomes(getTopNetworksWithHighestFitness(getSize())));
		IOManager.saveGenerationToFile(this, number);
	}
	
	public double highestFitness() {
		return getAverageFitnessOfNetwork(getTopNetworksWithHighestFitness(1).get(0));
	}
	
	public double totalFitness() {
		double result = 0;
		for(Entry<Network, Evaluation> entry: networksWithFitness.entrySet()) {
			result += entry.getValue().getAverageFitness();
		}
		return result;
	}
	
	public double averageFitness() {
		return ((double) totalFitness()) / ((double) getNetworks().size());
	}
	
	public int getSize() {
		return networksWithFitness.size();
	}

	public List<Network> getNetworks() {
		return new ArrayList<Network>(Arrays.asList(networksWithFitness.keySet().toArray(new Network[0])));
	}
	
	public Network getNetwork(int index) {
		return getNetworks().get(index);
	}
	
	public int getNumber() {
		return number;
	}
	
//	@Override
//	public String toString() {
//		return "Generation number: " + number + "\n" + networksWithFitness;
//	}
	
	private class Evaluation implements Comparable<Evaluation> {
		private Map<Integer, Integer> fitnessPerTry;

		public Evaluation() {
			fitnessPerTry = new HashMap<Integer, Integer>();
			fitnessPerTry.put(1, -1);
		}

		public int getTotalFitness() {
			int result = 0;
			for(Entry<Integer, Integer> entry: fitnessPerTry.entrySet()) {
				result += entry.getValue();
			}
			return result;
		}

		public void setFitness(int tryNumber, int fitness) {
			fitnessPerTry.put(tryNumber, fitness);
		}
		
		public double getAverageFitness() {
			return ((double) getTotalFitness()) / ((double) fitnessPerTry.size());
		}

		@Override
		public int compareTo(Evaluation e) {
			return (new Double(getAverageFitness()).compareTo(new Double(e.getAverageFitness())))*-1;
		}
		
		@Override
		public String toString() {
			return new Double(getAverageFitness()).toString();
		}
	}
}
