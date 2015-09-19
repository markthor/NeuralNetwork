package network;

import java.util.Arrays;
import java.util.Random;
import java.util.PrimitiveIterator.OfDouble;
import java.util.stream.DoubleStream;

import tools.MathTool;

public class Genome {
	private double[] genCode;

	public Genome(double[] genCode) {
		this.genCode = genCode;
	}
	
	public Genome(Species species, double defaultWeight, double defaultBias) {
		genCode = new double[species.getNumberOfSynapsis()+species.getNumberOfNeurons()];
		for(int i = 0; i<species.getNumberOfSynapsis(); i++) {
			genCode[i] = defaultWeight;
		}
		for(int i = species.getNumberOfSynapsis(); i<genCode.length; i++) {
			genCode[i] = defaultBias;
		}
	}
	
	public void mutate(double chanceOfMutation, double intensity) {
		for(int i = 0; i < genCode.length; i++) {
			if(MathTool.getNumberBetweenZeroAndOne() < chanceOfMutation)
				genCode[i] =+ (MathTool.getNormalDistribution() * intensity);
		}
	}
	
	public void mutateHeavy(double from, double to) {
		Random rand = new Random();
		DoubleStream ds = rand.doubles(-10, 10);
		OfDouble it = ds.iterator();
		for (int i = 0; i < genCode.length; i++) {
			genCode[i] += it.nextDouble();
		}
	}
	
	public Genome crossover(Genome other) {
		if(other.getGenCode().length != genCode.length) {
			throw new IllegalArgumentException("genCodes have to be of equal length");
		}
		
		double[] resultGenCode = new double[genCode.length];
		for(int i = 0; i < resultGenCode.length; i++) {
			if(MathTool.getNumberBetweenZeroAndOne() < 0.5) {
				resultGenCode[i] = genCode[i];
			} else {
				resultGenCode[i] = other.getGenCode()[i];
			}
		}
		
		return new Genome(resultGenCode);
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
	
//	@Override
//	public int hashCode() {
//		double result = 0;
//		for(int i = 0; i<genCode.length; i++) {
//			result += genCode[i] * 61.0;
//			result = result % 191449;
//		}
//		return (int) result;
//	}
	
}
