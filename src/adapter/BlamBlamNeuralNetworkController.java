package adapter;

import java.util.ArrayList;
import java.util.List;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import network.Network;

public class BlamBlamNeuralNetworkController extends NeuralNetworkController {
	
	private static final double MAX_DIST = 255.0;

	public BlamBlamNeuralNetworkController(Network network) {
		super(network);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		currentGeneration.addFitnessToNetwork(game.getScore(), network);
		return getMoveFromOutput(network.activateInputs(getInputFromGameState(game)));
	}
	
	private double scaleDist(Double dist) {
		return dist / MAX_DIST;
	}
	
	private void addGhosts(Game game, List<Double> input) {
		if (!game.isGhostEdible(GHOST.BLINKY)) {
			input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY), DM.MANHATTAN)));
		} else {
			input.add(1.0);
		}
		if (!game.isGhostEdible(GHOST.INKY)) {
			input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY), DM.MANHATTAN)));
		} else {
			input.add(1.0);
		}
		if (!game.isGhostEdible(GHOST.PINKY)) {
			input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY), DM.MANHATTAN)));
		} else {
			input.add(1.0);
		}
		if (!game.isGhostEdible(GHOST.SUE)) {
			input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE), DM.MANHATTAN)));
		} else {
			input.add(1.0);
		}	
	}
	
	private void addEdibleGhosts(Game game, List<Double> input) {
		if (game.isGhostEdible(GHOST.BLINKY)) {
			input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY), DM.MANHATTAN)));
		} else {
			input.add(1.0);
		}
		if (game.isGhostEdible(GHOST.INKY)) {
			input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY), DM.MANHATTAN)));
		} else {
			input.add(1.0);
		}
		if (game.isGhostEdible(GHOST.PINKY)) {
			input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY), DM.MANHATTAN)));
		} else {
			input.add(1.0);
		}
		if (game.isGhostEdible(GHOST.SUE)) {
			input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE), DM.MANHATTAN)));
		} else {
			input.add(1.0);
		}		
	}
	
	private List<Double> getInputFromGameState(Game game) {
		List<Double> input = new ArrayList<Double>();
		addGhosts(game, input);
		addEdibleGhosts(game, input);
		input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.MANHATTAN), DM.MANHATTAN)));
		input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.MANHATTAN), DM.MANHATTAN)));
		return input;
	}
	
	@Override
	protected MOVE getMoveFromOutput(List<Double> output) {
		int i = 0;
		int indexWithHighestValue = 0;
		double highestValue = Double.MIN_VALUE;
		for(Double d: output) {
			if(d > highestValue) {
				highestValue = d;
				indexWithHighestValue = i;
			}
			i++;
		}
		
		switch(indexWithHighestValue) {
			case 0: return MOVE.RIGHT;
			case 1: return MOVE.LEFT;
			case 2: return MOVE.UP;
			case 3: return MOVE.DOWN;
		}
		
		return null;
	}
}
