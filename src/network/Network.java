package network;

import java.util.ArrayList;
import java.util.List;

import evolution.Genome;
import evolution.Species;

public class Network {
	private List<Neuron> inputNeurons;
	private List<Neuron> outputNeurons;
	private List<List<Neuron>> allNeurons;

	// Meta data
	private Species species;
	private Genome genome;
	
	public Network(Genome genome, Species species) {
		this.genome = genome;
		this.species = species;
		initializeLists(species.getLayers());
		constructNetwork(species.getNumberOfNeuronsAtlayer(), genome.getGenCode());
	}
	
	private void constructNetwork(List<Integer> neuronsAtLayer, double[] genCode) {
		List<Neuron> lastLayer = new ArrayList<Neuron>();
		List<Neuron> currentLayer = new ArrayList<Neuron>();
		
		int layer = 1;
		int genCodePointer = 0;
		
		for(Integer i: neuronsAtLayer) {
			
			currentLayer.clear();
			
			for(int j = 0; j<i; j++) {
				Neuron currentNeuron = new Neuron(layer);
				allNeurons.get(layer-1).add(currentNeuron);
				
				currentLayer.add(currentNeuron);
				if(layer == 1) {
					inputNeurons.add(currentNeuron);
				} else if(layer == species.getLayers()) {
					outputNeurons.add(currentNeuron);
					for(Neuron lastLayerNeuron: lastLayer) {
						new Synapsis(genCode[genCodePointer], genCode[genCodePointer+1], lastLayerNeuron, currentNeuron);
						genCodePointer += 2;
					}
				} else {
					for(Neuron lastLayerNeuron: lastLayer) {
						new Synapsis(genCode[genCodePointer], genCode[genCodePointer+1], lastLayerNeuron, currentNeuron);
						genCodePointer += 2;
					}
				}
			}
			
			layer++;
			lastLayer = new ArrayList<Neuron>(currentLayer);
		}
	}
	
	public List<Double> activateInputs(List<Double> inputs) {
		for(int i = 0; i < inputs.size(); i++) {
			inputNeurons.get(i).inputIntoAllSynapsis(inputs.get(i));
		}
		for(int i = 1; i<species.getLayers()-1; i++) {
			for(Neuron n: allNeurons.get(i)) {
				n.readAndOutput();
			}
		}
		
		List<Double> result = new ArrayList<>();
		for(Neuron n: outputNeurons) {
			result.add(n.output());
		}
		
		return result;
	}
	
	public void initializeLists(int layers) {
		inputNeurons = new ArrayList<>();
		outputNeurons = new ArrayList<>();
		allNeurons = new ArrayList<>();
		
		for(int i = 0; i<layers; i++) {
			allNeurons.add(new ArrayList<Neuron>());
		}
	}
	
	public Genome getGenomeFromNetworkData() {
		int genCodePointer = 0;
		double[] genCode = new double[2*species.getNumberOfSynapsis()];

		for(List<Neuron> neuronsAtLayer: allNeurons) {
			for(Neuron n: neuronsAtLayer) {
				for(Synapsis s: n.getOutputSynapsis()) {
					genCode[genCodePointer] = s.getWeight();
					genCodePointer++;
					genCode[genCodePointer] = s.getBias();
					genCodePointer++;
				}
			}
		}

		
		return new Genome(genCode);
	}
	
	public Species getSpecies() {
		return species;
	}

	public Genome getGenome() {
		return genome;
	}
	
	public List<List<Neuron>> getAllNeurons() {
		return allNeurons;
	}
	
	public static List<Genome> networksToGenomes(List<Network> networks) {
		List<Genome> result = new ArrayList<Genome>();
		for(Network n: networks) {
			result.add(n.genome);
		}
		return result;
	}
	
	public static List<Network> genomesToNetwork(List<Genome> genomes, Species species) {
		List<Network> result = new ArrayList<Network>();
		for(Genome g: genomes) {
			result.add(new Network(g, species));
		}
		return result;
	}
	
	public String toString() {
		return "Network with hashcode: " + Integer.toString(hashCode());
	}
	
	public String toRichString() {
		String result = "";
		int layer = 1;
		for(List<Neuron> list: allNeurons) {
			result = result + "------- Layer " + layer + " -------\n";
			for(Neuron n: list) {
				result = result + n.toString();
			}
			layer++;
		}
		return result;
	}
	
//	@Override
//	public int hashCode() {
//		return genome.hashCode();
//	}
}
