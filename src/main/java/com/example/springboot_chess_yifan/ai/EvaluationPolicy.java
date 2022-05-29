package com.example.springboot_chess_yifan.ai;

import com.example.springboot_chess_yifan.board.Board;
import com.example.springboot_chess_yifan.board.Player;

public interface EvaluationPolicy {

	int get_score_for_player(Board board, Player player);
	
}
