package backpropagation;

import java.util.ArrayList;
import java.util.List;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

public class BatchResult {
	private List<BPResult> bpResults;

	public BatchResult() {
		bpResults = new ArrayList<>();
	}

	public List<Matrix> getAverageWeightPartialDerivatives() {
		int layersWithWeights = bpResults.get(0).getWeightPartialDerivatives()
				.size();

		List<Matrix> result = new ArrayList<Matrix>();

		// Initialize matrixes
		for (int i = 0; i < layersWithWeights; i++) {
			int rows = bpResults.get(0).getWeightPartialDerivatives().get(i)
					.rows();
			int columns = bpResults.get(0).getWeightPartialDerivatives().get(i)
					.columns();
			Basic2DMatrix averageMatrix = new Basic2DMatrix(rows, columns);
			result.add(averageMatrix);
		}

		// Sum
		for (int i = 0; i < bpResults.size(); i++) {
			for (int j = 0; j < layersWithWeights; j++) {
				result.set(
						j,
						result.get(j).add(
								bpResults.get(i).getWeightPartialDerivatives()
										.get(j)));
			}
		}

		for (int j = 0; j < layersWithWeights; j++) {
			double oneOverBatchSize = 1.0 / ((double) bpResults.size());
			result.set(j, result.get(j).multiply(oneOverBatchSize));
		}

		return result;
	}

	public List<Vector> getAverageBiasPartialDerivatives() {
		int layersWithBias= bpResults.get(0).getBiasPartialDerivatives().size();
		
		List<Vector> result = new ArrayList<Vector>();
		
		// Initialize matrixes
		for(int i = 0; i < layersWithBias; i++) {
			int length = bpResults.get(0).getBiasPartialDerivatives().get(i).length();
			BasicVector averageVector = new BasicVector(length);
			result.add(averageVector);
		}
		
		// Sum
		for(int i = 0; i < bpResults.size(); i++) {
			for(int j = 0; j < layersWithBias; j++) {
				result.set(j, result.get(j).add(bpResults.get(i).getBiasPartialDerivatives().get(j)));
			}
		}
		
		for(int j = 0; j < layersWithBias; j++) {
			double oneOverBatchSize = 1.0/((double)bpResults.size());
			result.set(j, result.get(j).multiply(oneOverBatchSize));
		}
		
		return result;
	}

	public void addBPResult(BPResult bpr) {
		bpResults.add(bpr);
	}
	
	public List<BPResult> getBpResults() {
		return bpResults;
	}
}

