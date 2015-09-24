package test;

import java.util.ArrayList;
import java.util.List;

import network.Genome;
import network.Network;
import network.Species;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;
import org.testng.Assert;
import org.testng.annotations.Test;

import backpropagation.Backpropagation;
import backpropagation.LinearFunction;
import backpropagation.SinusFunction;
import backpropagation.SinusTrainingEntry;
import backpropagation.TrainingEntry;
import tools.MathTool;

public class BackpropagationTest {
	public class GenerationTest {
		@Test
		public void testBackPropagtion() {
			int size = 5;
			Species s = new Species(1, 15, 5, 1);
			Genome g = new Genome(s, 0, 0);
			g.mutateHeavy(-1, 1);
			Network n = new Network(g, s);
//			n.getAllNeurons().get(0).get(0).setBias(-1);
//			n.getAllNeurons().get(1).get(0).setBias(-0.5);
//			n.getAllNeurons().get(1).get(1).setBias(0.0);
//			n.getAllNeurons().get(2).get(0).setBias(-2);
//			
//			n.getAllNeurons().get(0).get(0).getOutputSynapsis().get(0).setWeight(1);
//			n.getAllNeurons().get(0).get(0).getOutputSynapsis().get(1).setWeight(2);
//			n.getAllNeurons().get(2).get(0).getInputSynapsis().get(0).setWeight(0.5);
//			n.getAllNeurons().get(2).get(0).getInputSynapsis().get(1).setWeight(1);
			
			Backpropagation bp = new Backpropagation(n, new SinusFunction(), 0.10, 50);
			ArrayList<TrainingEntry> trainingSet = new ArrayList<TrainingEntry>();
			for(double d = 0; d <= 1.0; d=d+0.001) {
				trainingSet.add(new SinusTrainingEntry(d));
			}
			bp.iterativelyBackpropagate(10000, trainingSet);
			
			
			for(double i = 0; i < 1; i+=0.05) {
				SinusFunction sf = new SinusFunction();
				ArrayList<Double> inputs = new ArrayList<Double>();
				inputs.add(i);
				System.out.println("Test: " + n.activateInputs(inputs).get(0) + " should be: " + sf.scaledSin(inputs.get(0)));
			}
		}
		
		@Test
		public void testBackPropagtionWeightMatrixDimensions() {
			int size = 5;
			Species s = new Species(2, 3, 2);
			Genome g = new Genome(s, MathTool.getNormalDistribution(), MathTool.getNormalDistribution());
			Network n = new Network(g, s);
			
			Backpropagation bp = new Backpropagation(n, new SinusFunction(), 0.1, 10);
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
		
		@Test 
		void testMatrixMultiplication() {
			double[][] matrixdata1 = new double[3][2];
			double[][] matrixdata2 = new double[2][4];
			matrixdata1[0][0] = 2;
			matrixdata1[0][1] = 3;
			matrixdata1[1][0] = 1;
			matrixdata1[1][1] = 2;
			matrixdata1[2][0] = 5;
			matrixdata1[2][1] = 4;
			
			matrixdata2[0][0] = 4;
			matrixdata2[0][1] = 1;
			matrixdata2[0][2] = 2;
			matrixdata2[0][3] = 1;
			matrixdata2[1][0] = 3;
			matrixdata2[1][1] = 1;
			matrixdata2[1][2] = 3;
			matrixdata2[1][3] = 2;
			
			Matrix m1 = new Basic2DMatrix(matrixdata1);
			Matrix m2 = new Basic2DMatrix(matrixdata2);
			
			System.out.println(m1.multiply(m2));
		}
		
		public boolean isCloseEnough(double d1, double d2) {
			return Math.abs(d1-d2) < 0.001;
		}
	}
}
