package tools;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class MathTool {
	public static Random random = new Random();
	public static NormalDistribution nd = new NormalDistribution(0, 1);
	
	public static double getNumberBetweenZeroAndOne() {
		return random.nextDouble();
	}
	
	public static double sigma(double z) {
		return 1 / (1+ FastMath.pow(Math.E, -z));
	}
	
	public static Vector sigmaDerivativeVector(Vector z) {
		double[] vectorData = new double[z.length()];
		for (int i = 0; i < z.length(); i++) {
			vectorData[i] = sigmaDerivative(z.get(i));
		}
		return new BasicVector(vectorData);
	}
	
	public static double sigmaDerivative(double z) {
		return FastMath.pow(Math.E, -z) / FastMath.pow((1+FastMath.pow(Math.E, -z)), 2);
	}
	
	public static double getNormalDistribution() {
		return nd.sample();
	}
	
	public static int getIntFromZeroIncludingUpperExcludingZero(int upperBound) {
		return random.nextInt(upperBound) + 1;
	}
	
	public static int getIntFromZeroAndIncludingZeroExcludingUpper(int upperBound) {
		return random.nextInt(upperBound);
	}
	
	public static double getDoubleBetweenZeroAnd(double upperBound) {
		return random.nextDouble()*upperBound;
	}
}
