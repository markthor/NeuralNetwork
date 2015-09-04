package network;

public class Synapsis {
	private double weight;
	private double bias;
	private double lastInput;
	
	private Neuron inputNeuron;
	private Neuron outputNeuron;

	public Synapsis(double weight, double bias, Neuron input, Neuron output) {
		this.weight = weight;
		this.bias = bias;
		this.inputNeuron	= input;
		this.outputNeuron = output;
		input.addOutputSynapsis(this);
		output.addInputSynapsis(this);
		assert input.getLayer() < output.getLayer();
	}
	
	public void input(double input) {
		lastInput = input;
	}
	
	public double output() {
		return (lastInput * weight) + bias;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getBias() {
		return bias;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public String toString() {
		return "Synapsis with weight " + weight + " and bias " + bias;
	}
}
