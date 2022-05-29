package com.example.springboot_chess_yifan.ai;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot_chess_yifan.game.GameState;
import com.example.springboot_chess_yifan.game.Status;
import com.example.springboot_chess_yifan.logic.Move;
import com.example.springboot_chess_yifan.logic.MoveApplier;
import com.example.springboot_chess_yifan.logic.PromotionHandler;

/*
 * Decision tree for evaluation.
 */
public class SearchTree {

	public class Node {
		
		private final GameState gameState;
		private int score;
		private List<Node> children = new ArrayList<>();

		private Node(GameState gameState) {
			this.gameState = gameState;
		}

		private void attach(Node child) {
			children.add(child);
		}
		
		public GameState getGameState() {
			return gameState;
		}
		
		public List<Node> getChildren() {
			return children;
		}
		
		public void setScore(int score) {
			this.score = score;
		}
		
		public int getScore() {
			return this.score;
		}
		
	}

	private Node root;
	private int depth;

	public SearchTree(int depth, GameState gameState) {
		this.depth = depth;
		this.root = new Node(gameState);
		buildTree(this.depth, this.root);
	}
	
	public Node getRoot() {
		return root;
	}
	
	public int getDepth() {
		return depth;
	}

	private void buildTree(int depth, Node node) {
		if (depth == 0) {
			return;
		} else {
			GameState gameState = node.gameState;
			if (gameState.getStatus() == Status.WAIT_PROMOTION) {
				for (String pieceType : new String[] {"QUEEN", "BISHOP", "KNIGHT", "ROOK"}) {
					gameState = PromotionHandler.handlePromotion(gameState, pieceType);
					Node child = new Node(gameState);
					node.attach(child);
					buildTree(depth - 1, child);
				}
			} else {
				List<Move> possibleMoves = node.gameState.getPossibleMovesByActivePlayer();
				for (Move move : possibleMoves) {
					GameState nextState = MoveApplier.applyMove(gameState, move);
					Node child = new Node(nextState);
					node.attach(child);
					buildTree(depth - 1, child);
				}
			}

		}
	}

}
