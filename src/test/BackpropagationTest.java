package test;

import network.Genome;
import network.Network;
import network.Species;

import org.la4j.Matrix;
import org.testng.Assert;
import org.testng.annotations.Test;

import tools.MathTool;
import Backpropagation.Backpropagation;
import Backpropagation.SinusFunction;

public class BackpropagationTest {
	public class GenerationTest {
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
	}
}
