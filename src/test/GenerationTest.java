package test;

import network.Genome;
import network.Network;
import network.Species;

import org.testng.Assert;
import org.testng.annotations.Test;

import tools.MathTool;
import evolution.Generation;

public class GenerationTest {
	@Test
	public void testBasicGenerationFunctionality() {
		int size = 5;
		Species s = new Species(10, 15, 7);
		Genome g = new Genome(s, MathTool.getNormalDistribution(), MathTool.getNormalDistribution());
		Network parent = new Network(g, s);
		
		Generation generation = new Generation(1, parent, 5, MathTool.getNormalDistribution(), MathTool.getNormalDistribution());
	}
}
