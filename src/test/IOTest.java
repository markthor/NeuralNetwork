package test;

import io.IOManager;
import network.Genome;
import network.Network;
import network.Species;

import org.testng.Assert;
import org.testng.annotations.Test;

public class IOTest {
	@Test
	public void testGenomeIO() {
		Species s = new Species(2, 2, 1);
		Genome g = new Genome(s, 0.1, 0.3);
		g.mutate(1, 1);
		Network n = new Network(g, s);
		
		IOManager.saveGenomeToFile(g, 52525207);
		Genome readG = IOManager.readGenome(52525207, 1);
		
		//Assert.assertSame(readG.getGenCode(), g.getGenCode(),0);
	}
}
