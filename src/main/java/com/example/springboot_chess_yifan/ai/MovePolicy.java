package com.example.springboot_chess_yifan.ai;

import com.example.springboot_chess_yifan.game.GameState;

public interface MovePolicy {

	GameState moveByAI(GameState gameState);
	GameState promotionByAI(GameState gameState);
	
}
