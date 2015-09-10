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

	protected static final double MAX_DISTANCE = 255.0;
	
	public SimpleEvaluationNeuralNetworkController(Network network) {
		super(network);

	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		currentGeneration.addFitnessToNetwork(numberOftries, game.getScore(), network);
		return getBestMove(game);
	}
	
	private MOVE getBestMove(Game game) {
		int[] neighbours = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		
		int highestNeighbour = -1;
		double highestNeighbourScore = -1.0;
		for(int i = 0; i < neighbours.length; i++) {
			double evaluation = evaluateNode(neighbours[i], game);
			if(evaluateNode(neighbours[i], game) > highestNeighbourScore) {
				highestNeighbourScore = evaluation;
				highestNeighbour = neighbours[i];
			}
		}
		
		if(highestNeighbour == -1) {
			throw new IllegalStateException("Should not happen, revise code");
		}
		
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), highestNeighbour, DM.MANHATTAN);
	}
	
	private double evaluateNode(int node, Game game) {
		return network.activateInputs(getInputFromGameStateAndNode(node, game)).get(0);
	}
	
	private List<Double> getInputFromGameStateAndNode(int node, Game game) {
		List<Double> input = new ArrayList<Double>();
		input.add(scaleDistance(getDistanceToNearestGhostFromNode(node, game)));
		input.add(scaleDistance(getDistanceToSecondNearestGhostFromNode(node, game)));
		input.add(booleanToZeroOrOne(isNearestGhostEdibleFromNode(node, game)));
		input.add(booleanToZeroOrOne(isSecondNearestGhostEdibleFromNode(node, game)));
		input.add(scaleDistance(getDistanceToNearestPowerPill(node, game)));
		input.add(scaleDistance(getDistanceToNearestPill(node, game)));
		return input;
	}
	
	protected double scaleDistance(double distance) {
		return distance/MAX_DISTANCE;
	}
	
	protected double getDistanceToNearestPowerPill(int node, Game game) {
		int[] powerPills = game.getActivePowerPillsIndices();
		if(powerPills.length == 0) {
			return 1.0;
		}
		return game.getDistance(node, game.getClosestNodeIndexFromNodeIndex(node, powerPills, DM.MANHATTAN), DM.MANHATTAN);
	}
	
	protected double getDistanceToNearestPill(int node, Game game) {
		int[] pills = game.getActivePillsIndices();
		if(pills.length == 0) {
			return 1.0;
		}
		return game.getDistance(node, game.getClosestNodeIndexFromNodeIndex(node, pills, DM.MANHATTAN), DM.MANHATTAN);
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
