package com.example.springboot_chess_yifan.ai;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.springboot_chess_yifan.board.Board;
import com.example.springboot_chess_yifan.board.PieceType;
import com.example.springboot_chess_yifan.board.Player;
import com.example.springboot_chess_yifan.board.Square;


@Component(value = "defaultBoardEvaluator")
public class DefaultBoardEvaluator implements EvaluationPolicy {

	private int get_piece_value(PieceType type) {
		switch (type) {
		case PAWN:
			return 1;
		case KNIGHT:
			return 3;
		case BISHOP:
			return 3;
		case ROOK:
			return 5;
		case QUEEN:
			return 9;
		case KING:
			return 999;
		default:
			return 0;
		}
	}
	
	@Override
	public int get_score_for_player(Board board, Player player) {
		
		List<Square> playerSquares = board.getAllSquares(player);
		List<Square> enemySquares = board.getAllSquares(player.togglePlayer());
		
		int score = 0;
		
		for(Square sq: playerSquares) {
			int sq_value = get_piece_value(sq.getPiece().getPieceType());
			score += sq_value;
		}
		
		for(Square sq: enemySquares) {
			int sq_value = get_piece_value(sq.getPiece().getPieceType());
			score -= sq_value;
		}
		
		return score;
		
	}

}
