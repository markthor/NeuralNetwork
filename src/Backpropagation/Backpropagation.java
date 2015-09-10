package Backpropagation;

import java.util.ArrayList;
import java.util.List;

import network.Network;
import network.Neuron;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import tools.MathTool;

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
	
	private List<Vector> errorVectors(int layers, List<Double> inputs) {
		List<Vector> errorVectors = new ArrayList<>();
		
		double[] errors = new double[network.getOutputNeurons().size()];
		int j = 0;
		for (Neuron n : network.getOutputNeurons()) {
			errors[j] = getError(n.output(), n.rawActivationInput(), j, inputs);
			j++;
		}
		errorVectors.add(new BasicVector(errors));
		
		// When the initial error vector is calculated, calculate back through the network
		for(int l = layers-1; l > 1; l++) {
			
		}
		
		
		return errorVectors;
	}
	
	public Matrix getWeightMetrix(int layer) {
		if(layer == 1) throw new IllegalArgumentException();
		
		int numberOfNeuronsAtLayer = network.getAllNeurons().get(layer-1).size();
		int numberOfNeuronsAtPreviousLayer = network.getAllNeurons().get(layer-2).size();
		double[][] weightMatrixData = new double[numberOfNeuronsAtLayer][numberOfNeuronsAtPreviousLayer];
		for(int j = 0; j < weightMatrixData.length; j++) {
			for(int k = 0; k < weightMatrixData[j].length; k++) {
				weightMatrixData[j][k] = network.getAllNeurons().get(layer-1).get(j).getInputSynapsis().get(k).getWeight();
			}
		}
		return new Basic2DMatrix(weightMatrixData);
	}
	
	public Vector getBias
	
	private double getError(double a, double z, int j, List<Double> inputs) {
		return function.derivative(a, j, inputs) * MathTool.sigmaDerivative(z);
	}
}
