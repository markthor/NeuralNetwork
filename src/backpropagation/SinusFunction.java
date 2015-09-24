package backpropagation;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

public class SinusFunction implements CostFunction {

	@Override
	public double cost(double a, int outputIndex, List<Double> inputs) {
		if(outputIndex != 0) throw new IllegalArgumentException();
		return FastMath.pow(a - scaledSin(inputs.get(0)), 2)/2.0;
	}

	@Override
	public double derivative(double a, int outputIndex, List<Double> inputs) {
		if(outputIndex != 0) throw new IllegalArgumentException();
		return a - scaledSin(inputs.get(0));
	}
	
	public double scaledSin(double input) {
		return (FastMath.sin(input*2*Math.PI)/2)+0.5;
	}
}
