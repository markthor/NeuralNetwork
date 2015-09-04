package evolution;

import java.util.Arrays;

import tools.MathTool;

public class Genome {
	private double[] genCode;

	public Genome(double[] genCode) {
		this.genCode = genCode;
	}
	
	public Genome(Species species, double defaultWeight, double defaultBias) {
		genCode = new double[species.getNumberOfSynapsis()*2];
		for(int i = 0; i<genCode.length; i+=2) {
			genCode[i] = defaultWeight;
			genCode[i+1] = defaultBias;
		}
	}
	
	public void mutate(double chanceOfMutation, double intensity) {
		for(int i = 0; i < genCode.length; i++) {
			if(MathTool.getNumberBetweenZeroAndOne() < chanceOfMutation)
				genCode[i] = genCode[i] + (MathTool.getNormalDistribution() * intensity);
		}
	}
	
	public double[] getGenCode() {
		return genCode;
	}

	@Override
	public String toString() {
		return "Genome [genCode=" + Arrays.toString(genCode) + "]";
	}
	
	@Override
	public Genome clone() {
		return new Genome(genCode.clone());
	}
	
	@Override
	public int hashCode() {
		double result = 0;
		for(int i = 0; i<genCode.length; i++) {
			result += genCode[i] * 61.0;
			result = result % 191449;
		}
		return (int) result;
	}
	
}
