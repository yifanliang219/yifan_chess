package com.example.springboot_chess_yifan.ai;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.springboot_chess_yifan.ai.SearchTree.Node;
import com.example.springboot_chess_yifan.board.Player;
import com.example.springboot_chess_yifan.game.GameState;
import com.example.springboot_chess_yifan.logic.Move;
import com.example.springboot_chess_yifan.logic.MoveApplier;
import com.example.springboot_chess_yifan.logic.PromotionHandler;

@Component(value = "defaultMoveAgent")
public class DefaultMoveAgent implements MovePolicy {

	int searchDepth;

	@Autowired
	@Qualifier("defaultBoardEvaluator")
	private EvaluationPolicy evaluationPolicy;

	public DefaultMoveAgent() {
		this(2);
	}

	public DefaultMoveAgent(int searchDepth) {
		this.searchDepth = searchDepth;
	}

	@Override
	public GameState moveByAI(GameState gameState) {

		SearchTree tree = new SearchTree(searchDepth, gameState);
		Node root = tree.getRoot();
		List<Move> moves = gameState.getPossibleMovesByActivePlayer();
		if (moves.isEmpty()) {
			gameState.setMessage(gameState.getActive_player().togglePlayer() + " Win");
			return gameState;
		} else {
			int bestScore = Integer.MIN_VALUE;
			List<Move> bestMoves = new ArrayList<>();
			for (Node child : root.getChildren()) {
				int score = minimax(child, searchDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, gameState.getActive_player());
				if (score == bestScore) {
					bestMoves.add(child.getGameState().getLastMove());
				}
				if (score > bestScore) {
					bestScore = score;
					bestMoves.clear();
					bestMoves.add(child.getGameState().getLastMove());
				}
			}
			int random = (int) (Math.random() * bestMoves.size());
			Move bestMove = bestMoves.get(random);
			return MoveApplier.applyMove(gameState, bestMove);
		}
	}

	@Override
	public GameState promotionByAI(GameState gameState) {
		return PromotionHandler.handlePromotion(gameState, "QUEEN");
	}
	/*
	 * minimax alpha beta pruning to decide a move
	 */
	
	public int minimax(Node node, int depth, int alpha, int beta, Player maximizingPlayer) {
		if (depth == 0) {
			int score = evaluationPolicy.get_score_for_player(node.getGameState().getBoard(), maximizingPlayer);
			return score;
		} else {
			List<Node> children = node.getChildren();
			if (children.isEmpty()) {
				return Integer.MIN_VALUE;
			}
			Player activePlayer = node.getGameState().getActive_player();
			if (activePlayer == maximizingPlayer) {
				int currentMax = Integer.MIN_VALUE;
				for (Node child : children) {
					int score = minimax(child, depth - 1, alpha, beta, maximizingPlayer);
					currentMax = Math.max(currentMax, score);
					if (currentMax >= beta) {
						break;
					}
					alpha = Math.max(currentMax, alpha);
				}
				return currentMax;
			} else {
				int currentMin = Integer.MAX_VALUE;
				for (Node child : children) {
					int score = minimax(child, depth - 1, alpha, beta, maximizingPlayer);
					currentMin = Math.min(currentMin, score);
					if (currentMin <= alpha) {
						break;
					}
					beta = Math.min(currentMin, beta);
				}
				return currentMin;
			}
		}
	}

}
