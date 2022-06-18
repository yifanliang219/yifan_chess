package com.example.springboot_chess_yifan.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_chess_yifan.ai.MovePolicy;
import com.example.springboot_chess_yifan.game.GameState;
import com.example.springboot_chess_yifan.game.Status;
import com.example.springboot_chess_yifan.logic.Move;
import com.example.springboot_chess_yifan.logic.MoveApplier;
import com.example.springboot_chess_yifan.logic.MoveType;
import com.example.springboot_chess_yifan.logic.PromotionHandler;

@RestController
public class ChessController {

	private Map<String, GameState> games = new HashMap<>();
		
	@Autowired
	@Qualifier("defaultMoveAgent")
	private MovePolicy movePolicy;

	/*
	 * without request params, initialize the board,
	 * with request params, update board according to move.
	 */
	@GetMapping("init")
	public GameState init() {
		GameState gameState = new GameState();
		games.put(gameState.getGameId(), gameState);
		return gameState;
	}
	
	@GetMapping("/games/{gameId}/move")
	public GameState getGameState(
			@PathVariable String gameId,
			@RequestParam(value = "start_file", required = false) String start_file,
			@RequestParam(value = "start_rank", required = false) String start_rank,
			@RequestParam(value = "end_file", required = false) String end_file,
			@RequestParam(value = "end_rank", required = false) String end_rank,
			@RequestParam(value = "moveType", required = false) String moveType) {
		GameState gameState = games.get(gameId);
		if (start_file == null) {
		    return gameState;
		}
		else {
			Move move = Move.generateMove(Integer.parseInt(start_file), Integer.parseInt(start_rank), Integer.parseInt(end_file), Integer.parseInt(end_rank), MoveType.valueOf(moveType));
			gameState = MoveApplier.applyMove(gameState, move);
			games.replace(gameId, gameState);
			return gameState;
		}
	}
	
	/*
	 * for handling user's promotion move
	 */
	@GetMapping("games/{gameId}/promotion")
	public GameState getGameState(
			@PathVariable String gameId,
			@RequestParam(value = "piece", required = true) String piece) {
		GameState gameState = games.get(gameId);
		gameState = PromotionHandler.handlePromotion(gameState, piece);
		games.replace(gameId, gameState);
		return gameState;
	}
	
	/*
	 * ask the AI for a move
	 */
	@GetMapping("games/{gameId}/ai")
	public GameState getGameState(@PathVariable String gameId) {
		GameState gameState = games.get(gameId);
		gameState = movePolicy.moveByAI(gameState);
		if(gameState.getStatus() == Status.WAIT_PROMOTION) {
			gameState = movePolicy.promotionByAI(gameState);
		}
		games.replace(gameId, gameState);
		return gameState;
	}

}
