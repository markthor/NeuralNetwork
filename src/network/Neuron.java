package network;

import java.util.ArrayList;
import java.util.List;

import tools.MathTool;

public class Neuron {
	private List<Synapsis> inputSynapsis;
	private List<Synapsis> outputSynapsis;
	private double bias;

	private int layer;
	
	public Neuron(int layer, double bias) {
		inputSynapsis = new ArrayList<Synapsis>();
		outputSynapsis = new ArrayList<Synapsis>();
		this.layer = layer;
		this.bias = bias;
	}
	
	public void inputIntoAllSynapsis(Double input) {
		for(Synapsis s: outputSynapsis) {
			s.input(input);
		}
	}
	
	public double output() {
		double result = 0;
		for(Synapsis s: inputSynapsis) {
			result = result + s.output();
		}
		return activate(result);
	}
	
	public double activate(double sumInput) {
		return MathTool.sigma(sumInput + bias);
	}
	
	public void readAndOutput() {
		inputIntoAllSynapsis(output());
	}
	
	public void addInputSynapsis(Synapsis s) {
		inputSynapsis.add(s);
	}
	
	public void addOutputSynapsis(Synapsis s) {
		outputSynapsis.add(s);
	}
	
	public int getLayer() {
		return layer;
	}
	
	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public List<Synapsis> getOutputSynapsis() {
		return outputSynapsis;
	}
	
	public String toString() {
		String result = 
				"Neuron at layer " + layer + " connected to " + inputSynapsis.size()
				+ " input synapsis and " + outputSynapsis.size() + " output synapsis.\n";
		
		for(Synapsis s: outputSynapsis) {
			result = result + s.toString() + "\n";
		}
		
		return result;
	}
}
