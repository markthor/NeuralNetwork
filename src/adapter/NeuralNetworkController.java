package adapter;

import java.util.ArrayList;
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

	protected final static double MAX_X = 120.0;
	protected final static double MAX_Y = 120.0;
	protected Generation currentGeneration;

	public NeuralNetworkController(Network network) {
		super();
		this.network = network;
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		return super.getMove();
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
		
	private void addZeroOrOneToInput(List<Double> input, List<MOVE> possibleMoves, MOVE expectedMove) {
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
	
}
