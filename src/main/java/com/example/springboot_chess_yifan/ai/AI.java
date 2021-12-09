package com.example.springboot_chess_yifan.ai;

import java.util.List;

import com.example.springboot_chess_yifan.game.GameState;
import com.example.springboot_chess_yifan.logic.Move;
import com.example.springboot_chess_yifan.logic.MoveApplier;
import com.example.springboot_chess_yifan.logic.PromotionHandler;

public class AI {

	
	public static GameState moveByAI(GameState gameState) {
		List<Move> moves = gameState.getPossibleMovesByActivePlayer();
		int size = moves.size();
		if(size == 0) {
			gameState.setMessage(gameState.getActive_player().togglePlayer() + " Win");
			return gameState;
		}
		int random = (int)(Math.random()*moves.size());
		Move move = moves.get(random);
		return MoveApplier.applyMove(gameState, move);
	}
	
	public static GameState promotionByAI(GameState gameState) {
		return PromotionHandler.handlePromotion(gameState, "QUEEN");
	}
	
}
