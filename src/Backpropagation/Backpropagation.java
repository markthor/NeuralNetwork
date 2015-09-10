package Backpropagation;

import java.util.ArrayList;
import java.util.List;

import tools.MathTool;
import network.Network;
import network.Neuron;

public class Backpropagation {
	private Network network;
	private CostFunction function;
	private double learningRate;
	
	public Backpropagation(Network network, CostFunction function, double learningRate) {
		super();
		this.network = network;
		this.function = function;
		this.learningRate = learningRate;
	}

	public void backpropagateUntilSatuated() {
		
	}
	
	private void backpropagate(List<Double> inputs) {
		List<Double> output = network.activateInputs(inputs);
		
	}
	
	private List<List<Double>> errorMatrix(int layers, List<Double> inputs) {
		List<List<Double>> errorMatrix = new ArrayList<List<Double>>();
		initializeMatrixList(errorMatrix, layers);

		int j = 0;
		for (Neuron n : network.getOutputNeurons()) {
			errorMatrix.get(layers-1).add(getError(n.output(), n.rawActivationInput(), j,
					inputs));
			j++;
		}
		
		// When the initial error vector is calculated, calculate back through the network
		for(int l = layers-1; l > 0; l++) {
			
		}
		
		
		return errorMatrix;
	}
	
	private double getError(double a, double z, int j, List<Double> inputs) {
		return function.derivative(a, j, inputs) * MathTool.sigmaDerivative(z);
	}
	
	private void initializeMatrixList(List<List<Double>> matrix, int layers) {
		for (int i = 0; i < layers; i++) {
			matrix.add(new ArrayList<Double>());
		}
	}
}
