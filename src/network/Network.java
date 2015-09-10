package network;

import java.util.ArrayList;
import java.util.List;

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
		constructNetwork(species.getNumberOfNeuronsAtlayer(), genome.getGenCode(), species.getNumberOfNeurons(), species.getNumberOfSynapsis());
	}
	
	private void constructNetwork(List<Integer> neuronsAtLayer, double[] genCode, int numberOfNeurons, int numberOfSynapsis) {
		List<Neuron> lastLayer = new ArrayList<Neuron>();
		List<Neuron> currentLayer = new ArrayList<Neuron>();
		
		int layer = 1;
		int genCodeWeightPointer = 0;
		int genCodeBiasPointer = numberOfSynapsis;
		
		for(Integer i: neuronsAtLayer) {
			
			currentLayer.clear();
			
			for(int j = 0; j<i; j++) {
				Neuron currentNeuron = new Neuron(layer, genCode[genCodeBiasPointer]);
				genCodeBiasPointer++;
				allNeurons.get(layer-1).add(currentNeuron);
				
				currentLayer.add(currentNeuron);
				if(layer == 1) {
					inputNeurons.add(currentNeuron);
				} else if(layer == species.getLayers()) {
					outputNeurons.add(currentNeuron);
					for(Neuron lastLayerNeuron: lastLayer) {
						new Synapsis(genCode[genCodeWeightPointer], lastLayerNeuron, currentNeuron);
						genCodeWeightPointer++;
					}
				} else {
					for(Neuron lastLayerNeuron: lastLayer) {
						new Synapsis(genCode[genCodeWeightPointer], lastLayerNeuron, currentNeuron);
						genCodeWeightPointer++;
					}
				}
			}
			
			layer++;
			lastLayer = new ArrayList<Neuron>(currentLayer);
		}
	}
	
	public List<Double> activateInputs(List<Double> inputs) {
		for(int i = 0; i < inputs.size(); i++) {
			inputNeurons.get(i).inputIntoAllSynapsis(inputNeurons.get(i).addBiasAndActivate(inputs.get(i)));
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
		double[] genCode = new double[species.getNumberOfSynapsis()+species.getNumberOfNeurons()];

		for(List<Neuron> neuronsAtLayer: allNeurons) {
			for(Neuron n: neuronsAtLayer) {
				for(Synapsis s: n.getOutputSynapsis()) {
					genCode[genCodePointer] = s.getWeight();
					genCodePointer++;
				}
			}
		}
		for(List<Neuron> neuronsAtLayer: allNeurons) {
			for(Neuron n: neuronsAtLayer) {
				genCode[genCodePointer] = n.getBias();
				genCodePointer++;
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
	
	public List<Neuron> getInputNeurons() {
		return inputNeurons;
	}
	
	public List<Neuron> getOutputNeurons() {
		return outputNeurons;
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
	
	@Override
	public int hashCode() {
		return genome.hashCode();
	}
}
