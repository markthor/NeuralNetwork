package backpropagation;

import java.util.ArrayList;
import java.util.List;

public class SinusTrainingEntry implements TrainingEntry {
	
	private double input;
	private double output;
	private static SinusFunction function = new SinusFunction();

	public SinusTrainingEntry(double input) {
		this.input = input;
		this.output = function.scaledSin(input);
	}
	
	@Override
	public List<Double> input() {
		ArrayList<Double> result = new ArrayList<Double>();
		result.add(input);
		return result;
	}

	@Override
	public List<Double> expectedOutput() {
		ArrayList<Double> result = new ArrayList<Double>();
		result.add(output);
		return result;
	}

}
