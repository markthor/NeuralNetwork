package adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import network.Network;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EvaluationNeuralNetworkController extends NeuralNetworkController {
	
	public EvaluationNeuralNetworkController(Network network) {
		super(network);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		currentGeneration.addFitnessToNetwork(game.getScore(), network);
		return getMoveFromOutput(network.activateInputs(getInputFromGameState(game)));
	}
	
	private List<Double> getInputFromGameState(Game game) {
		List<Double> input = new ArrayList<Double>();
		fillWithPossibleMoves(input, game);
		fillWithEvaluationOfNeighbouringNodes(input, game);
		
		return input;
	}
	
	public void fillWithEvaluationOfNeighbouringNodes(List<Double> input, Game game) {
		int[] neighbours = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		
		for(int neighbourIndex: neighbours) {
			input.add(evaluatePositionActivePills(neighbourIndex, game));
		}
		
		for(int neighbourIndex: neighbours) {
			input.add(evaluatePositionGhost(neighbourIndex, game));
		}
	}
	
	public double evaluatePositionActivePills(int neighbourIndex, Game game) {
		return evaluatePosition(neighbourIndex, game.getActivePillsIndices(), game);
	}
	
	public double evaluatePositionGhost(int neighbourIndex, Game game) {
		int[] ghostNodes = new int[4];
		ghostNodes[0] = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		ghostNodes[1] = game.getGhostCurrentNodeIndex(GHOST.INKY);
		ghostNodes[2] = game.getGhostCurrentNodeIndex(GHOST.SUE);
		ghostNodes[3] = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		return evaluatePosition(neighbourIndex, ghostNodes, game);
	}
	
	private double evaluatePosition(int neighbourIndex, int[] indicies, Game game) {
		double result = 0.0;
		result = 1 / game.getDistance(neighbourIndex, game.getClosestNodeIndexFromNodeIndex(neighbourIndex, indicies, DM.MANHATTAN), DM.MANHATTAN);
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
}
