package Backpropagation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import network.Network;
import network.Neuron;
import network.Synapsis;

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
		ArrayList<Double> trainingSet = new ArrayList<Double>();
		for(double d = 0; d <= 1.0; d=d+0.01) {
			trainingSet.add(d);
		}
		int numberOfRunsTest = 0;
		for(double cost = averageCost(trainingSet); cost>0.005; cost=averageCost(trainingSet)) {
			for(Double d: trainingSet) {
				ArrayList<Double> inputs = new ArrayList<Double>();
				inputs.add(d);
				backpropagate(inputs);
			}
			Collections.shuffle(trainingSet);
			numberOfRunsTest++;
			if(numberOfRunsTest % 1000 == 0) {
				System.out.println(cost);
			}
		}
	}
	
	private double averageCost(List<Double> trainingSet) {
		double sum = 0;
		for(Double d: trainingSet) {
			ArrayList<Double> inputs = new ArrayList<Double>();
			inputs.add(d);
			sum += function.cost(network.activateInputs(inputs).get(0), 0, inputs);
		}
		return sum / ((double)trainingSet.size());
	}
	
	private void backpropagate(List<Double> inputs) {
		List<Double> output = network.activateInputs(inputs);
		Vector[] errorVectors = errorVectors(network.getSpecies().getLayers(),
				inputs);
		List<Matrix> weightPartialDerivatives = getWeightPartialDerivatives(errorVectors);
		updateBiases(errorVectors);
		updateWeights(weightPartialDerivatives);

	}
	
	private void updateBiases(Vector[] errorVectors) {
		for (int layer = 1; layer <= network.getSpecies().getLayers(); layer++) {
			int neuronNumber = 0;
			for(Neuron n: network.getAllNeurons().get(layer-1)) {
				n.modifyBias((-learningRate)*errorVectors[layer-1].get(neuronNumber));
				neuronNumber++;
			}
		}
	}
	
	private void updateWeights(List<Matrix> weightPartialDerivatives) {
		for (int layer = 2; layer <= network.getSpecies().getLayers(); layer++) {
			int neuronNumber = 0;
			for(Neuron n: network.getAllNeurons().get(layer-1)) {
				int synapsisNumber = 0;
				for(Synapsis s: n.getInputSynapsis()) {
					s.modifyWeight((-learningRate)*weightPartialDerivatives.get(layer-2).get(neuronNumber, synapsisNumber));
					synapsisNumber++;
				}
				neuronNumber++;
			}
		}
	}
	
	private List<Matrix> getWeightPartialDerivatives(Vector[] errorVectors) {
		List<double[][]> weightErrorMatricesData = new ArrayList<>();
		
		for (int layer = 2; layer <= errorVectors.length; layer++) {
			double[][] weightErrorMatrixData = new double[network.getAllNeurons().get(layer-1).size()][network.getAllNeurons().get(layer-2).size()];
			
			for (int neuronNumber = 0; neuronNumber < errorVectors[layer-1].length(); neuronNumber++) {
				int synapsisNumber = 0;
				for(Synapsis s: network.getAllNeurons().get(layer-1).get(neuronNumber).getInputSynapsis()) {
					double a = network.getAllNeurons().get(layer-1).get(neuronNumber).getInputSynapsis().get(synapsisNumber).getInputNeuron().output();
					weightErrorMatrixData[neuronNumber][synapsisNumber] = a * errorVectors[layer-1].get(neuronNumber);
					synapsisNumber++;
				}
				
			}
			weightErrorMatricesData.add(weightErrorMatrixData);
		}
		
		ArrayList<Matrix> result = new ArrayList<Matrix>();
		for(double[][] data: weightErrorMatricesData) {
			result.add(new Basic2DMatrix(data));
		}
		return result;
	}
	
	private Vector[] errorVectors(int layers, List<Double> inputs) {
		Vector[] errorVectors = new Vector[layers];
		
		double[] errors = new double[network.getOutputNeurons().size()];
		int j = 0;
		for (Neuron n : network.getOutputNeurons()) {
			errors[j] = getError(n.output(), n.rawActivationInput(), j, inputs);
			j++;
		}
		errorVectors[layers-1] = new BasicVector(errors);
		
		// When the initial error vector is calculated, calculate back through the network
		int indexOfPreviousLayer = layers-1;
		for(int l = layers-1; l > 0; l--) {
			errorVectors[l-1] = getErrorVector(l, errorVectors[indexOfPreviousLayer]);
			indexOfPreviousLayer--;
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
	
	private Vector getErrorVector(int layer, Vector previousErrorVector) {
		return getWeightMetrix(layer+1).transpose().multiply(previousErrorVector).hadamardProduct(MathTool.sigmaDerivativeVector(getRawInputVector(layer)));
	}
	
	private Vector getRawInputVector(int layer) {
		double[] rawInputs = new double[network.getAllNeurons().get(layer-1).size()];
		int j = 0;
		for (Neuron n : network.getAllNeurons().get(layer-1)) {
			rawInputs[j] = n.rawActivationInput();
			j++;
		}
		return new BasicVector(rawInputs);
	}
	
	private double getError(double a, double z, int j, List<Double> inputs) {
		return function.derivative(a, j, inputs) * MathTool.sigmaDerivative(z);
	}
}
