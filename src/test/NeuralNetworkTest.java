package test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import network.Genome;
import network.Network;
import network.Species;

import org.junit.Test;

public class NeuralNetworkTest {
	@Test
	public void testANN1() {
		Species s = new Species(2, 2, 1);
		Genome g = new Genome(s, 0.1, 0.1);
		Network n = new Network(g, s);
		
		List<Double> inputs = new ArrayList<Double>();
		inputs.add(2.0);
		inputs.add(-1.0);
		List<Double> outputs = n.activateInputs(inputs);
		
		System.out.println(outputs.get(0));
		
		Assert.assertTrue(isCloseEnough(0.5525197575, outputs.get(0)));
	}
	
	@Test
	public void testANN2() {
		Species s = new Species(2, 2, 1);
		Genome g = new Genome(s, 0.1, 0.1);
		Network n = new Network(g, s);
		n.getAllNeurons().get(0).get(0).setBias(0.2);
		
		List<Double> inputs = new ArrayList<Double>();
		inputs.add(2.0);
		inputs.add(-1.0);
		List<Double> outputs = n.activateInputs(inputs);
		
		Assert.assertTrue(isCloseEnough(0.5525311749, outputs.get(0)));
	}
	
	@Test
	public void testANN3() {
		Species s = new Species(2, 2, 1);
		Genome g = new Genome(s, 0.1, 0.1);
		Network n = new Network(g, s);
		n.getAllNeurons().get(0).get(0).setBias(0.2);
		n.getAllNeurons().get(0).get(1).setBias(0.9);
		n.getAllNeurons().get(0).get(0).getOutputSynapsis().get(0).setWeight(0.7);
		
		List<Double> inputs = new ArrayList<Double>();
		inputs.add(2.0);
		inputs.add(-1.0);
		List<Double> outputs = n.activateInputs(inputs);
		
		Assert.assertTrue(isCloseEnough(0.5558727625, outputs.get(0)));
	}
	
	private boolean isCloseEnough(double expected, double actual) {
		return Math.abs(expected-actual) < 0.000001;
	}
}
