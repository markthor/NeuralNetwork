package adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.ZeroException;

import network.Network;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Ghost;

public class EvaluationNeuralNetworkController extends NeuralNetworkController {
	
	protected static final double MAX_DISTANCE = 255.0;
	
	public EvaluationNeuralNetworkController(Network network) {
		super(network);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		currentGeneration.addFitnessToNetwork(numberOftries, game.getScore(), network);
		return getBestMove(game);
	}
	
	protected MOVE getBestMove(Game game) {
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
	
	protected double evaluateNode(int node, Game game) {
		return network.activateInputs(getInputFromGameStateAndNode(node, game)).get(0);
	}
	
	protected List<Double> getInputFromGameStateAndNode(int node, Game game) { 
		return new ArrayList<Double>();
	}
	
	protected double scaleDistance(double distance) {
		return distance/MAX_DISTANCE;
	}
	
	private List<Double> getInputFromGameState(Game game) {
		List<Double> input = new ArrayList<Double>();
		fillWithPossibleMoves(input, game);
		fillWithEvaluationOfNeighbouringNodesActivePills(input, game);
		fillWithEvaluationOfNeighbouringNodesGhosts(input, game);
		input.add(1.0-evaluatePositionGhost(game.getPacmanCurrentNodeIndex(), game));
		fillWithZeroOrOne(input, isNearestGhostEdible(game));
		
		return input;
	}
	
	public void fillWithEvaluationOfNeighbouringNodesActivePills(List<Double> input, Game game) {
		Double[] result = new Double[4];
		for(int i = 0; i < result.length; i++) {
			result[i] = new Double(0.0);
		}
		
		int[] neighbours = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		double highestEvaluation = -1;
		int neighbourWithHighestEvaluation = 0;
		for(int neighbourIndex: neighbours) {
			double evaluation = evaluatePositionActivePills(neighbourIndex, game);
			if(evaluation > highestEvaluation) {
				neighbourWithHighestEvaluation = neighbourIndex;
				highestEvaluation = evaluation;
			}
		}
		
		if(highestEvaluation == -1) {
			throw new IllegalStateException("Should not happen, revise code");
		}
		
		switch (game.getMoveToMakeToReachDirectNeighbour(game.getPacmanCurrentNodeIndex(), neighbourWithHighestEvaluation)) {
		case RIGHT:
			result[0] = 1.0;
		case LEFT:
			result[1] = 1.0;
			break;
		case UP:
			result[2] = 1.0;
			break;
		case DOWN:
			result[3] = 1.0;
			break;
		case NEUTRAL:
			throw new IllegalStateException("Should not happen, revise code");
		}
		
		input.addAll(new ArrayList<Double>(Arrays.asList(result)));
	}
	
	public void fillWithEvaluationOfNeighbouringNodesGhosts(List<Double> input, Game game) {
		Double[] result = new Double[4];
		for(int i = 0; i < result.length; i++) {
			result[i] = new Double(0.0);
		}
		
		int[] neighbours = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		double highestEvaluation = -1;
		int neighbourWithHighestEvaluation = 0;
		for(int neighbourIndex: neighbours) {
			double evaluation = evaluatePositionGhost(neighbourIndex, game);
			if(evaluation > highestEvaluation) {
				neighbourWithHighestEvaluation = neighbourIndex;
				highestEvaluation = evaluation;
			}
		}
		
		if(highestEvaluation == -1) {
			throw new IllegalStateException("Should not happen, revise code");
		}
		
		switch (game.getMoveToMakeToReachDirectNeighbour(game.getPacmanCurrentNodeIndex(), neighbourWithHighestEvaluation)) {
		case RIGHT:
			result[0] = 1.0;
		case LEFT:
			result[1] = 1.0;
			break;
		case UP:
			result[2] = 1.0;
			break;
		case DOWN:
			result[3] = 1.0;
			break;
		case NEUTRAL:
			throw new IllegalStateException("Should not happen, revise code");
		}
		
		input.addAll(new ArrayList<Double>(Arrays.asList(result)));
	}
	
//	public void fillWithEvaluationOfNeighbouringNodes(List<Double> input, Game game) {
//		Double[] result = new Double[8];
//		for(int i = 0; i < result.length; i++) {
//			result[i] = new Double(0.0);
//		}
//		int[] neighbours = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
//		for(int neighbourIndex: neighbours) {
//			switch(game.getMoveToMakeToReachDirectNeighbour(game.getPacmanCurrentNodeIndex(), neighbourIndex)) {
//				case RIGHT: result[0] = evaluatePositionActivePills(neighbourIndex, game);
//							result[1] = evaluatePositionGhost(neighbourIndex, game);
//							break;
//				case LEFT:  result[2] = evaluatePositionActivePills(neighbourIndex, game);
//							result[3] = evaluatePositionGhost(neighbourIndex, game);
//							break;
//				case UP: 	result[4] = evaluatePositionActivePills(neighbourIndex, game);
//							result[5] = evaluatePositionGhost(neighbourIndex, game);
//							break;
//				case DOWN:  result[6] = evaluatePositionActivePills(neighbourIndex, game);
//							result[7] = evaluatePositionGhost(neighbourIndex, game);
//							break;
//				case NEUTRAL:
//							throw new IllegalStateException("Should not happen, revise code");
//			}
//		}
//		input.addAll(new ArrayList<Double>(Arrays.asList(result)));
//	}
	
	public double evaluatePositionActivePills(int neighbourIndex, Game game) {
		return evaluatePosition(neighbourIndex, game.getActivePillsIndices(), game);
	}
	
	public double evaluatePositionGhost(int neighbourIndex, Game game) {
		int[] ghostNodes = new int[4];
		ghostNodes[0] = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		ghostNodes[1] = game.getGhostCurrentNodeIndex(GHOST.INKY);
		ghostNodes[2] = game.getGhostCurrentNodeIndex(GHOST.SUE);
		ghostNodes[3] = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		return 1.0 - evaluatePosition(neighbourIndex, ghostNodes, game);
	}
	
	private double evaluatePosition(int neighbourIndex, int[] indicies, Game game) {
		double result = 0.0;
		result = 1 / game.getDistance(neighbourIndex, game.getClosestNodeIndexFromNodeIndex(neighbourIndex, indicies, DM.MANHATTAN), DM.MANHATTAN);
		if(Double.isInfinite(result)) {
			return 1.0;
		}
		return result;
	}
	
	@Override
	protected void fillWithPossibleMoves(List<Double> input, Game game) {
		List<MOVE> moves = Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex()));
		addZeroOrOneToInput(input, moves, MOVE.RIGHT);
		addZeroOrOneToInput(input, moves, MOVE.LEFT);
		addZeroOrOneToInput(input, moves, MOVE.UP);
		addZeroOrOneToInput(input, moves, MOVE.DOWN);
	}
	
	protected double getDistanceToNearestPowerPill(int node, Game game) {
		int[] powerPills = game.getActivePowerPillsIndices();
		if(powerPills.length == 0) {
			return 1.0;
		}
		return game.getShortestPathDistance(node, game.getClosestNodeIndexFromNodeIndex(node, powerPills, DM.PATH));
	}
	
	protected double getDistanceToNearestPill(int node, Game game) {
		int[] pills = game.getActivePillsIndices();
		if(pills.length == 0) {
			return 1.0;
		}
		return game.getShortestPathDistance(node, game.getClosestNodeIndexFromNodeIndex(node, pills, DM.PATH));
	}
}
