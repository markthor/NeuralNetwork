package adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import network.Network;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import evolution.Generation;

public class SimpleNeuralNetworkController extends NeuralNetworkController {

	public SimpleNeuralNetworkController(Network network) {
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
		fillWithCoordinatesOfNode(input, game.getPacmanCurrentNodeIndex(), game);
		fillWithCoordinatesOfNode(input, game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getPillIndices(), DM.MANHATTAN), game);
		
		return input;
	}
}
