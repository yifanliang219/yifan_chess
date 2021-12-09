package com.example.springboot_chess_yifan.logic;

import com.example.springboot_chess_yifan.board.Piece;
import com.example.springboot_chess_yifan.board.PieceType;
import com.example.springboot_chess_yifan.board.Player;
import com.example.springboot_chess_yifan.board.Square;
import com.example.springboot_chess_yifan.game.GameState;
import com.example.springboot_chess_yifan.game.Status;

public class PromotionHandler {
	
	public static GameState handlePromotion(GameState gameState, String param) {
		
		PieceType pieceType = PieceType.valueOf(param);
		Player active_player = gameState.getActive_player();
		Piece promotionTo = Piece.generatePiece(pieceType, active_player);
		int pawn_rank = 0;
		if(active_player == Player.WHITE) {
			pawn_rank = 8;
		}else {
			pawn_rank = 1;
		}
		for(int file=0; file<=8; file++) {
			Square sq = gameState.getBoard().getSquareByFileAndRank(file, pawn_rank);
			if(sq.getPiece().getPieceType() == PieceType.PAWN) {
				sq.setPiece(promotionTo);
				gameState.setStatus(Status.WAIT_MOVE);
				gameState.togglePlayer();
				gameState.setMessage("");
				break;
			}
		}
		return gameState;
	}
	
}
