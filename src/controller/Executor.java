package controller;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

public class Executor {
	public static void main(String args[]) {
		double[][] matrixData = new double[2][3];
		matrixData[0][0] = 2.0;
		matrixData[0][1] = 1.0;
		matrixData[0][2] = 2.0;
		matrixData[1][0] = 1.0;
		matrixData[1][1] = 2.0;
		matrixData[1][2] = 3.0;
		Matrix matrix1 = new Basic2DMatrix(matrixData);
		
		System.out.println(matrix1);
		
		double[][] matrixData2 = new double[1][3];
		matrixData2[0][0] = 1.0;
		matrixData2[0][1] = 3.0;
		matrixData2[0][2] = 1.0;
		Matrix matrix2 = new Basic2DMatrix(matrixData2);
		
		Matrix matrix3 = matrix1.multiply(matrix2.transpose());
		
		System.out.println(matrix3.toString());
		
//		Species s = new Species(1, 1, 1);
//		Genome g1 = new Genome(s, 0, 0);
//		Genome g2 = new Genome(s, 1, 1);
//		Genome g3 = new Genome(s, 2, 2);
//		Network n1 = new Network(g1, s);
//		Network n2 = new Network(g2, s);
//		Network n3 = new Network(g3, s);
//		
//		ArrayList<Network> arrayList = new ArrayList<Network>();
//		arrayList.add(n1);
//		arrayList.add(n2);
//		arrayList.add(n3);
//		
//		Generation gen = new Generation(1, arrayList);
//		gen.addFitnessToNetwork(1, 6, n1);
//		gen.addFitnessToNetwork(1, 4, n2);
//		gen.addFitnessToNetwork(1, 7, n3);
//		gen.saveGeneration();
	} 
}
