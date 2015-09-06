package controller;

import io.IOManager;

import java.util.ArrayList;

import tools.MathTool;
import network.Genome;
import network.Network;
import network.Species;
import evolution.Generation;

public class Executor {
	public static void main(String args[]) {

		Species s = new Species(1, 1, 1);
		Genome g1 = new Genome(s, 0, 0);
		Genome g2 = new Genome(s, 1, 1);
		Genome g3 = new Genome(s, 2, 2);
		Network n1 = new Network(g1, s);
		Network n2 = new Network(g2, s);
		Network n3 = new Network(g3, s);
		
		ArrayList<Network> arrayList = new ArrayList<Network>();
		arrayList.add(n1);
		arrayList.add(n2);
		arrayList.add(n3);
		
		Generation gen = new Generation(1, arrayList);
		gen.addFitnessToNetwork(1, 6, n1);
		gen.addFitnessToNetwork(1, 4, n2);
		gen.addFitnessToNetwork(1, 7, n3);
		gen.saveGeneration();
//		
//		double intesity = 0.3;
//		
//		System.out.println(MathTool.getNormalDistribution()*intesity);
//		System.out.println(MathTool.getNormalDistribution()*intesity);
//		System.out.println(MathTool.getNormalDistribution()*intesity);
//		System.out.println(MathTool.getNormalDistribution()*intesity);
		
//		Species species = new Species(1, 2, 1);
//		Genome genome = new Genome(species, 0, 0);
//		genome.mutate(1, 1);
//		Network network = new Network(genome, species);
//		Generation generation = new Generation(1, network, 3, 1.0, 0.5);
//		generation.addFitnessToNetwork(10, generation.getNetworks().get(0));
//		generation.addFitnessToNetwork(5, generation.getNetworks().get(1));
//		generation.addFitnessToNetwork(2, generation.getNetworks().get(2));
//		System.out.println(generation.toString());
//		System.out.println(generation.selectParentToNextGeneration());
//		
//		System.out.println(generation.getNetwork(0).hashCode());
//		System.out.println(generation.getNetwork(1).hashCode());
//		System.out.println(generation.getNetwork(2).hashCode());
//		
//		IOManager.saveGenomeToFile(genome);
//		
//		Genome g = IOManager.readGenomeNumber(0);
//		System.out.println(g.toString());
	} 
}
