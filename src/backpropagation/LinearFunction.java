package backpropagation;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

public class LinearFunction implements CostFunction {

	@Override
	public double cost(double a, int outputIndex, List<Double> inputs) {
		if(outputIndex != 0) throw new IllegalArgumentException();
		return FastMath.pow(a - linear(inputs.get(0)), 2);
	}

	@Override
	public double derivative(double a, int outputIndex, List<Double> inputs) {
		if(outputIndex != 0) throw new IllegalArgumentException();
		return (2 * a) - (2*linear(inputs.get(0)));
	}
	
	private double linear(double input) {
		return input/2;
	}

}
