package adapter;

import java.util.Arrays;
import java.util.List;

import network.Network;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import evolution.Generation;

public class NeuralNetworkController extends Controller<MOVE> {

	protected Network network;
	protected Generation currentGeneration;
	protected int numberOftries = 1;

	protected final static double MAX_X = 120.0;
	protected final static double MAX_Y = 120.0;


	public NeuralNetworkController(Network network) {
		super();
		this.network = network;
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		return super.getMove();
	}
	
	protected double booleanToZeroOrOne(boolean bool) {
		return bool ? 1.0 : 0.0;
	}
	
	protected void fillWithZeroOrOne(List<Double> input, boolean oneOrZero) {
		input.add(oneOrZero ? 1.0 : 0.0);
	}
	
	protected void fillWithCoordinatesOfNode(List<Double> input, int node, Game game) {
		input.add(scaleXCoordinate(game.getNodeXCood(node)));
		input.add(scaleYCoordinate(game.getNodeYCood(node)));
	}
	
	private double scaleXCoordinate(int node) {
		return ((double) node) / MAX_X;
	}
	
	private double scaleYCoordinate(int node) {
		return ((double) node) / MAX_Y;
	}
	
	protected void fillWithPossibleMoves(List<Double> input, Game game) {
		List<MOVE> moves = Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex()));
		addZeroOrOneToInput(input, moves, MOVE.NEUTRAL);
		addZeroOrOneToInput(input, moves, MOVE.RIGHT);
		addZeroOrOneToInput(input, moves, MOVE.LEFT);
		addZeroOrOneToInput(input, moves, MOVE.UP);
		addZeroOrOneToInput(input, moves, MOVE.DOWN);
		
	}
		
	protected void addZeroOrOneToInput(List<Double> input, List<MOVE> possibleMoves, MOVE expectedMove) {
		if(possibleMoves.contains(expectedMove)) {
			input.add(1.0);
		} else {
			input.add(0.0);
		}
	}
	
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
			case 0: return MOVE.NEUTRAL;
			case 1: return MOVE.RIGHT;
			case 2: return MOVE.LEFT;
			case 3: return MOVE.UP;
			case 4: return MOVE.DOWN;
		}
		
		return null;
	}
	
	protected boolean isNearestGhostEdible(Game game) {
		int[] ghostNodes = new int[4];
		ghostNodes[0] = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		ghostNodes[1] = game.getGhostCurrentNodeIndex(GHOST.INKY);
		ghostNodes[2] = game.getGhostCurrentNodeIndex(GHOST.SUE);
		ghostNodes[3] = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		int nearestGhostNode = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghostNodes, DM.MANHATTAN);
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
	
	protected int[] parseIntArray(int[] intArray) {
		if(intArray.length == 0) {
			int[] result = new int[0];
			result[0] = 100;
			return result;
		}
		return intArray;
	}
	
	public Network getNetwork() {
		return network;
	}
	
	public void setNetwork(Network network) {
		this.network = network;
	}
	
	public Generation getCurrentGeneration() {
		return currentGeneration;
	}
	
	public void setCurrentGeneration(Generation currentGeneration) {
		this.currentGeneration = currentGeneration;
	}
	
	public void incrementNumberOfTries() {
		numberOftries++;
	}
	
	public void resetNumberOfTries() {
		numberOftries = 1;
	}
}
