package network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Species {
	private List<Integer> numberOfNeuronsAtlayer;
	private int numberOfNeurons = 0;
	private int numberOfSynapsis = 0;
	private int layers;

	public Species(Integer... numberOfNeuronsAtlayer) {
		this.numberOfNeuronsAtlayer = Arrays.asList(numberOfNeuronsAtlayer);
		layers = this.numberOfNeuronsAtlayer.size();
		calculateNumberOfNeurons();
		calculateNumberOfSynapsis();
	}
	
	private void calculateNumberOfNeurons() {
		for(Integer i: numberOfNeuronsAtlayer) {
			numberOfNeurons += i;
		}
	}
	
	private void calculateNumberOfSynapsis() {
		int numberOfNeuronsAtPreviousLayer = numberOfNeuronsAtlayer.get(0);
		int layer = 0;
		for(Integer i: numberOfNeuronsAtlayer) {
			if(layer != 0) {
				numberOfSynapsis += i * numberOfNeuronsAtPreviousLayer;
				numberOfNeuronsAtPreviousLayer = i;
			}
			layer++;
		}
	}

	public List<Integer> getNumberOfNeuronsAtlayer() {
		return numberOfNeuronsAtlayer;
	}
	
	public int getNumberOfNeurons() {
		return numberOfNeurons;
	}
	
	public int getNumberOfSynapsis() {
		return numberOfSynapsis;
	}
	
	public int getLayers() {
		return layers;
	}
}
