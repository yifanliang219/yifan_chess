package com.example.springboot_chess_yifan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_chess_yifan.ai.AI;
import com.example.springboot_chess_yifan.board.Piece;
import com.example.springboot_chess_yifan.board.PieceType;
import com.example.springboot_chess_yifan.board.Square;
import com.example.springboot_chess_yifan.game.GameState;
import com.example.springboot_chess_yifan.game.Status;
import com.example.springboot_chess_yifan.logic.Move;
import com.example.springboot_chess_yifan.logic.MoveApplier;
import com.example.springboot_chess_yifan.logic.MoveType;
import com.example.springboot_chess_yifan.logic.PromotionHandler;

@RestController
public class ChessController {

	@Autowired
	private GameState gameState;

	@GetMapping("/board.json")
	public GameState getGameState(@RequestParam(value = "start_file", required = false) String start_file,
			@RequestParam(value = "start_rank", required = false) String start_rank,
			@RequestParam(value = "end_file", required = false) String end_file,
			@RequestParam(value = "end_rank", required = false) String end_rank,
			@RequestParam(value = "moveType", required = false) String moveType) {

		if (start_file == null)
			gameState = new GameState();
		else {
			Move move = Move.generateMove(Integer.parseInt(start_file), Integer.parseInt(start_rank), Integer.parseInt(end_file), Integer.parseInt(end_rank), MoveType.valueOf(moveType));
			gameState = MoveApplier.applyMove(gameState, move);			
		}
		
		return gameState;
	}
	
	@GetMapping("/board.json/promotion")
	public GameState getGameState(@RequestParam(value = "piece", required = true) String piece) {
		
		gameState = PromotionHandler.handlePromotion(gameState, piece);
		return gameState;
		
	}
	
	@GetMapping("/board.json/ai")
	public GameState getGameState() {
		
		gameState = AI.moveByAI(gameState);
		if(gameState.getStatus() == Status.WAIT_PROMOTION) {
			gameState = AI.promotionByAI(gameState);
		}
		return gameState;
		
	}

}