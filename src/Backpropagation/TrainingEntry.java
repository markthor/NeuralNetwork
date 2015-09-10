package Backpropagation;

import java.util.List;

public interface TrainingEntry {
	public List<Double> input();
	public List<Double> expectedOutput();
}
