package test;

import java.util.ArrayList;

import network.Genome;
import network.Network;
import network.Species;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;
import org.testng.Assert;
import org.testng.annotations.Test;

import tools.MathTool;
import Backpropagation.Backpropagation;
import Backpropagation.LinearFunction;
import Backpropagation.SinusFunction;

public class BackpropagationTest {
	public class GenerationTest {
		@Test
		public void testBackPropagtion() {
			int size = 5;
			Species s = new Species(1, 10, 1);
			Genome g = new Genome(s, 0, 0);
			g.mutate(1, 0.1);
			Network n = new Network(g, s);
			
			Backpropagation bp = new Backpropagation(n, new LinearFunction(), 0.15);
			bp.backpropagateUntilSatuated();
			
			ArrayList<Double> inputs = new ArrayList<Double>();
			inputs.add(0.3);
			System.out.println(n.activateInputs(inputs).get(0));
		}
		
		@Test
		public void testBackPropagtionWeightMatrixDimensions() {
			int size = 5;
			Species s = new Species(2, 3, 2);
			Genome g = new Genome(s, MathTool.getNormalDistribution(), MathTool.getNormalDistribution());
			Network n = new Network(g, s);
			
			Backpropagation bp = new Backpropagation(n, new SinusFunction(), 0.1);
			Matrix matrix = bp.getWeightMetrix(3);
			Assert.assertEquals(matrix.rows(), 2);
			Assert.assertEquals(matrix.columns(), 3);
		}
		
		@Test
		public void testApplySigmaDerivativeOnVector() {
			double[] vectorData = new double[3];
			vectorData[0] = 2.0;
			vectorData[1] = 1;
			vectorData[2] = -0.5;
			Vector vector = new BasicVector(vectorData);
			vector = MathTool.sigmaDerivativeVector(vector);
			Assert.assertTrue(isCloseEnough(vector.get(0), 0.105));
			Assert.assertTrue(isCloseEnough(vector.get(1), 0.197));
			Assert.assertTrue(isCloseEnough(vector.get(2), 0.235));
		}
		
		public boolean isCloseEnough(double d1, double d2) {
			return Math.abs(d1-d2) < 0.001;
		}
	}
}
