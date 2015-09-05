package tools;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

public class MathTool {
	public static Random random = new Random();
	public static NormalDistribution nd = new NormalDistribution(0, 1);
	
	public static double getNumberBetweenZeroAndOne() {
		return random.nextDouble();
	}
	
	public static double sigma(double z) {
		return 1 / (1+ FastMath.pow(Math.E, -z));
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
