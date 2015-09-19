package backpropagation;

import java.util.List;

import org.la4j.Matrix;
import org.la4j.Vector;

public class BPResult {
	private List<Matrix> weightPartialDerivatives;
	private List<Vector> biasPartialDerivatives;
	
	public BPResult(List<Matrix> weightPartialDerivatives,
			List<Vector> biasPartialDerivatives) {
		this.weightPartialDerivatives = weightPartialDerivatives;
		this.biasPartialDerivatives = biasPartialDerivatives;
	}
	
	public List<Matrix> getWeightPartialDerivatives() {
		return weightPartialDerivatives;
	}
	public List<Vector> getBiasPartialDerivatives() {
		return biasPartialDerivatives;
	}
	
	
}
