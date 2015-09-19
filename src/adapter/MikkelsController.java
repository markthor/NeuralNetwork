package adapter;

import java.util.ArrayList;
import java.util.List;

import network.Network;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MikkelsController extends NeuralNetworkController {
	
	public MikkelsController(Network network) {
		super(network);
	}

	protected Game game;
	private final static int MAX_DISTANCE = 255;

	@Override
	public MOVE getMove(Game game, long timeDue) {
		currentGeneration.addFitnessToNetwork(numberOftries, game.getScore(), network);
		
		return getBestMove(game);
	}

	private MOVE getBestMove(Game g) {
		game = g;
		//find neighbours
		int currentNode = game.getPacmanCurrentNodeIndex();
		int[] neighbours = game.getNeighbouringNodes(currentNode);
		
		int highestNeighbour = -1;
		double highestScore = -1;
		for (int i = 0; i < neighbours.length; i++) {
			double score = getScoreOfNode(neighbours[i]);
			assert score > 0;
			if(highestScore < score){
				highestNeighbour = neighbours[i];
				highestScore = score;
			}
		}
		
		if(highestNeighbour == -1){
			throw new IllegalStateException();
		}
		return game.getNextMoveTowardsTarget(currentNode, highestNeighbour, DM.PATH);
	}

	private double getScoreOfNode(int currentNode) {
		List<Double> inputs = new ArrayList<Double>();
		
		inputGhostDistances(inputs, currentNode);
		inputPillDistance(inputs, currentNode);
		inputPowerPillDistance(inputs, currentNode);
		inputGhostEdibleTime(inputs, currentNode);
		
		List<Double> output = network.activateInputs(inputs);
		return output.get(0);
	}

	private void inputGhostEdibleTime(List<Double> input, int currentNode) {
		List<Double> inputs = new ArrayList<Double>(4);
		for(GHOST ghost : GHOST.values()){
			if(game.getGhostEdibleTime(ghost) == 0.0){
				inputs.add(-1.0d);
			} else {
				inputs.add((double) game.getGhostEdibleTime(ghost));
			}
			
		}
		
		input.addAll(inputs);
	}

	private void inputPillDistance(List<Double> input, int currentNode) {
		int[] activePills = game.getActivePillsIndices();
		if(activePills.length == 0){
			input.add(-1.0);
		} else {
			double distance = game.getDistance(currentNode, game.getClosestNodeIndexFromNodeIndex(currentNode, activePills, DM.PATH), DM.PATH);
			input.add(convertDistanceToInput(distance));
		}
	}

	private void inputPowerPillDistance(List<Double> input, int currentNode) {
		int[] activePowerPills = game.getActivePowerPillsIndices();
		if(activePowerPills.length == 0){
			input.add(-1.0);
		} else {
			double distance = game.getDistance(currentNode, game.getClosestNodeIndexFromNodeIndex(currentNode, activePowerPills, DM.PATH), DM.PATH);
			input.add(convertDistanceToInput(distance));
		}
	}

	private void inputGhostDistances(List<Double> input, int currentNode) {
		List<Double> inputs = new ArrayList<Double>(4);
		
		for(GHOST ghost : GHOST.values()){
			int distance = game.getShortestPathDistance(currentNode, game.getGhostCurrentNodeIndex(ghost));
			inputs.add(convertDistanceToInput(distance));
		}
		
		input.addAll(inputs);
	}
	
	protected double convertBooleanToInput(boolean b){
		return b == true ? 1d : 0d;
	}
	
	protected double convertDistanceToInput(double distance){
		return distance / MAX_DISTANCE;
	}

}
