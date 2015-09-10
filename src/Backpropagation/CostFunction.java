package Backpropagation;

import java.util.List;

public interface CostFunction {
	public double cost(double a, int outputIndex, List<Double> inputs);
	public double derivative(double a, int outputIndex, List<Double> inputs);
}
