package adapter;

import java.util.ArrayList;
import java.util.List;

import network.Network;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import pacman.game.Constants.DM;

public class ModerateEvaluationNeuralNetworkController extends EvaluationNeuralNetworkController {

	protected static final double MAX_DISTANCE = 255.0;
	
	public ModerateEvaluationNeuralNetworkController(Network network) {
		super(network);
	}
	
	@Override
	protected List<Double> getInputFromGameStateAndNode(int node, Game game) { 
		List<Double> input = new ArrayList<Double>();
		input.add(scaleDistance(getDistanceToGhostFromNode(GHOST.BLINKY, game, node)));
		input.add(scaleDistance(getDistanceToGhostFromNode(GHOST.INKY, game, node)));
		input.add(scaleDistance(getDistanceToGhostFromNode(GHOST.PINKY, game, node)));
		input.add(scaleDistance(getDistanceToGhostFromNode(GHOST.SUE, game, node)));
		input.add(isGhostEdible(GHOST.BLINKY, game));
		input.add(isGhostEdible(GHOST.INKY, game));
		input.add(isGhostEdible(GHOST.PINKY, game));
		input.add(isGhostEdible(GHOST.SUE, game));
		input.add(scaleDistance(getDistanceToNearestPowerPill(node, game)));
		input.add(scaleDistance(getDistanceToNearestPill(node, game)));
		return input;
	}
	
	protected double isGhostEdible(GHOST ghost, Game game) {
		if(game.isGhostEdible(ghost)) {
			return 1.0;
		} else {
			return 0.0;
		}
	}
	
	protected double scaleTimeToEat(int timeToEat) {
		return ((double)timeToEat)/((double)Constants.EDIBLE_TIME);
	}
	
	protected double getDistanceToGhostFromNode(GHOST ghost, Game game, int node) {
		return game.getShortestPathDistance(node, game.getGhostCurrentNodeIndex(ghost));
	}
}
