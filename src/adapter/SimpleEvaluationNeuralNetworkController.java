package adapter;

import java.util.ArrayList;
import java.util.List;

import network.Network;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Ghost;
import tools.CollectionTool;

public class SimpleEvaluationNeuralNetworkController extends EvaluationNeuralNetworkController {

	public SimpleEvaluationNeuralNetworkController(Network network) {
		super(network);

	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		currentGeneration.addFitnessToNetwork(numberOftries, game.getScore(), network);
		return getBestMove(game);
	}
	
	@Override
	protected List<Double> getInputFromGameStateAndNode(int node, Game game) {
		List<Double> input = new ArrayList<Double>();
		input.add(scaleDistance(getDistanceToNearestGhostFromNode(node, game)));
		input.add(scaleDistance(getDistanceToSecondNearestGhostFromNode(node, game)));
		input.add(booleanToZeroOrOne(isNearestGhostEdibleFromNode(node, game)));
		input.add(booleanToZeroOrOne(isSecondNearestGhostEdibleFromNode(node, game)));
		input.add(scaleDistance(getDistanceToNearestPowerPill(node, game)));
		input.add(scaleDistance(getDistanceToNearestPill(node, game)));
		return input;
	}
	
	protected double getDistanceToNearestGhostFromNode(int node, Game game) {
		int[] ghostNodes = new int[4];
		ghostNodes[0] = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		ghostNodes[1] = game.getGhostCurrentNodeIndex(GHOST.INKY);
		ghostNodes[2] = game.getGhostCurrentNodeIndex(GHOST.SUE);
		ghostNodes[3] = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		int nearestGhostNode = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghostNodes, DM.MANHATTAN);

		return game.getDistance(node, nearestGhostNode, DM.MANHATTAN);
	}
	
	protected double getDistanceToSecondNearestGhostFromNode(int node, Game game) {
		List<Integer> ghostNodes = new ArrayList<Integer>();
		ghostNodes.add(game.getGhostCurrentNodeIndex(GHOST.BLINKY));
		ghostNodes.add(game.getGhostCurrentNodeIndex(GHOST.INKY));
		ghostNodes.add(game.getGhostCurrentNodeIndex(GHOST.SUE));
		ghostNodes.add(game.getGhostCurrentNodeIndex(GHOST.PINKY));
		
		int nearestGhostNode = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), CollectionTool.integerCollectionToIntegerArray(ghostNodes), DM.MANHATTAN);
		ghostNodes.remove(new Integer(nearestGhostNode));
		
		if(ghostNodes.size() != 3) {
			throw new IllegalStateException("Should not happen, revise code");
		}
		
		nearestGhostNode = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), CollectionTool.integerCollectionToIntegerArray(ghostNodes), DM.MANHATTAN);
		return game.getDistance(node, nearestGhostNode, DM.MANHATTAN);
	}
	
	protected boolean isNearestGhostEdibleFromNode(int node, Game game) {
		int[] ghostNodes = new int[4];
		ghostNodes[0] = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		ghostNodes[1] = game.getGhostCurrentNodeIndex(GHOST.INKY);
		ghostNodes[2] = game.getGhostCurrentNodeIndex(GHOST.SUE);
		ghostNodes[3] = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		int nearestGhostNode = game.getClosestNodeIndexFromNodeIndex(node, ghostNodes, DM.MANHATTAN);
		if(game.getGhostCurrentNodeIndex(GHOST.BLINKY) == nearestGhostNode) {
			return game.isGhostEdible(GHOST.BLINKY);
		}
		if(game.getGhostCurrentNodeIndex(GHOST.INKY) == nearestGhostNode) {
			return game.isGhostEdible(GHOST.INKY);
		}
		if(game.getGhostCurrentNodeIndex(GHOST.SUE) == nearestGhostNode) {
			return game.isGhostEdible(GHOST.SUE);
		}
		if(game.getGhostCurrentNodeIndex(GHOST.PINKY) == nearestGhostNode) {
			return game.isGhostEdible(GHOST.PINKY);
		}
		
		throw new IllegalStateException("Should not happen, revise code");
	}
	
	protected boolean isSecondNearestGhostEdibleFromNode(int node, Game game) {
		List<Integer> ghostNodes = new ArrayList<Integer>();
		ghostNodes.add(game.getGhostCurrentNodeIndex(GHOST.BLINKY));
		ghostNodes.add(game.getGhostCurrentNodeIndex(GHOST.INKY));
		ghostNodes.add(game.getGhostCurrentNodeIndex(GHOST.SUE));
		ghostNodes.add(game.getGhostCurrentNodeIndex(GHOST.PINKY));
		
		int nearestGhostNode = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), CollectionTool.integerCollectionToIntegerArray(ghostNodes), DM.MANHATTAN);
		ghostNodes.remove(new Integer(nearestGhostNode));
		
		if(ghostNodes.size() != 3) {
			throw new IllegalStateException("Should not happen, revise code");
		}
		
		nearestGhostNode = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), CollectionTool.integerCollectionToIntegerArray(ghostNodes), DM.MANHATTAN);
		
		if(game.getGhostCurrentNodeIndex(GHOST.BLINKY) == nearestGhostNode) {
			return game.isGhostEdible(GHOST.BLINKY);
		}
		if(game.getGhostCurrentNodeIndex(GHOST.INKY) == nearestGhostNode) {
			return game.isGhostEdible(GHOST.INKY);
		}
		if(game.getGhostCurrentNodeIndex(GHOST.SUE) == nearestGhostNode) {
			return game.isGhostEdible(GHOST.SUE);
		}
		if(game.getGhostCurrentNodeIndex(GHOST.PINKY) == nearestGhostNode) {
			return game.isGhostEdible(GHOST.PINKY);
		}
		
		throw new IllegalStateException("Should not happen, revise code");
	}
}
