package network;

public class Synapsis {
	private double weight;
	private double lastInput;
	
	private Neuron inputNeuron;
	private Neuron outputNeuron;

	public Synapsis(double weight, Neuron input, Neuron output) {
		this.weight = weight;
		this.inputNeuron = input;
		this.outputNeuron = output;
		input.addOutputSynapsis(this);
		output.addInputSynapsis(this);
		assert input.getLayer() < output.getLayer();
	}
	
	public void input(double input) {
		lastInput = input;
	}
	
	public double output() {
		return lastInput * weight;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public String toString() {
		return "Synapsis with weight " + weight;
	}
}
