package adapter;

import java.util.ArrayList;
import java.util.List;

import network.Network;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class AdvancedNeuralNetworkController extends NeuralNetworkController {
	
	public AdvancedNeuralNetworkController(Network network) {
		super(network);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		currentGeneration.addFitnessToNetwork(numberOftries, game.getScore(), network);
		return getMoveFromOutput(network.activateInputs(getInputFromGameState(game)));
	}
	
	private List<Double> getInputFromGameState(Game game) {
		List<Double> input = new ArrayList<Double>();
		fillWithPossibleMoves(input, game);
		fillWithCoordinatesOfNode(input, game.getPacmanCurrentNodeIndex(), game);
		fillWithCoordinatesOfNode(input, game.getGhostCurrentNodeIndex(GHOST.BLINKY), game);
		fillWithCoordinatesOfNode(input, game.getGhostCurrentNodeIndex(GHOST.INKY), game);
		fillWithCoordinatesOfNode(input, game.getGhostCurrentNodeIndex(GHOST.PINKY), game);
		fillWithCoordinatesOfNode(input, game.getGhostCurrentNodeIndex(GHOST.SUE), game);
		fillWithCoordinatesOfNode(input, game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), parseIntArray(game.getActivePillsIndices()), DM.MANHATTAN), game);
		fillWithCoordinatesOfNode(input, game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), parseIntArray(game.getPowerPillIndices()), DM.MANHATTAN), game);
		fillWithZeroOrOne(input, isNearestGhostEdible(game));
		fillWithCoordinatesOfNode(input, game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), parseIntArray(game.getJunctionIndices()), DM.MANHATTAN), game);
		return input;
	}
	

}
