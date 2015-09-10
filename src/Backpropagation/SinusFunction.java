package Backpropagation;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

public class SinusFunction implements CostFunction {

	@Override
	public double cost(double a, int outputIndex, List<Double> inputs) {
		if(outputIndex != 0) throw new IllegalArgumentException();
		return FastMath.pow(a - FastMath.sin(inputs.get(0)), 2);
	}

	@Override
	public double derivative(double a, int outputIndex, List<Double> inputs) {
		if(outputIndex != 0) throw new IllegalArgumentException();
		return (2 * a) - (2*FastMath.sin(inputs.get(0)));
	}


}
