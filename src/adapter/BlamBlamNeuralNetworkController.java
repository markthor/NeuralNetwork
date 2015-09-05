package adapter;

import java.util.ArrayList;
import java.util.List;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import network.Network;

public class BlamBlamNeuralNetworkController extends NeuralNetworkController {

	public BlamBlamNeuralNetworkController(Network network) {
		super(network);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		currentGeneration.addFitnessToNetwork(game.getScore(), network);
		return getMoveFromOutput(network.activateInputs(getInputFromGameState(game)));
	}
	
	private double scaleDist(Double dist) {
		return dist / 255.0;
	}
	
	private List<Double> getInputFromGameState(Game game) {
		List<Double> input = new ArrayList<Double>();
		fillWithCoordinatesOfNode(input, game.getPacmanCurrentNodeIndex(), game);
		//fillWithCoordinatesOfNode(input, game.getGhostCurrentNodeIndex(GHOST.BLINKY), game);
		//fillWithCoordinatesOfNode(input, game.getGhostCurrentNodeIndex(GHOST.INKY), game);
		//fillWithCoordinatesOfNode(input, game.getGhostCurrentNodeIndex(GHOST.PINKY), game);
		//fillWithCoordinatesOfNode(input, game.getGhostCurrentNodeIndex(GHOST.SUE), game);
		//fillWithCoordinatesOfNode(input, game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.MANHATTAN), game);
		//fillWithCoordinatesOfNode(input, game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getPowerPillIndices(), DM.MANHATTAN), game);
		input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY), DM.MANHATTAN)));
		input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY), DM.MANHATTAN)));
		input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY), DM.MANHATTAN)));
		input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE), DM.MANHATTAN)));
		input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.MANHATTAN), DM.MANHATTAN)));
		input.add(scaleDist(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), DM.MANHATTAN), DM.MANHATTAN)));
		
		//fillWithZeroOrOne(input, game.isGhostEdible(GHOST.BLINKY));
		//fillWithZeroOrOne(input, game.isGhostEdible(GHOST.INKY));
		//fillWithZeroOrOne(input, game.isGhostEdible(GHOST.PINKY));
		//fillWithZeroOrOne(input, game.isGhostEdible(GHOST.SUE));
		//game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY), DM.MANHATTAN);
		//fillWithCoordinatesOfNode(input, game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getJunctionIndices(), DM.MANHATTAN), game);
		
		//System.out.println("furthest dist: "+ game.getDistance(game.getPacmanCurrentNodeIndex(), game.getFarthestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), DM.MANHATTAN), DM.MANHATTAN));
		
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
