package com.example.springboot_chess_yifan.logic;

import com.example.springboot_chess_yifan.board.NullPiece;
import com.example.springboot_chess_yifan.board.PIECE_KING;
import com.example.springboot_chess_yifan.board.PIECE_ROOK;
import com.example.springboot_chess_yifan.board.Piece;
import com.example.springboot_chess_yifan.board.Player;
import com.example.springboot_chess_yifan.board.Square;
import com.example.springboot_chess_yifan.game.GameState;
import com.example.springboot_chess_yifan.game.Status;

public class MoveApplier {

	public static GameState applyMove(GameState gameState, Move move) {
		switch (move.getStartSquare(gameState).getPiece().getPieceType()) {

		case ROOK:
			return applyMove_rook(gameState, move);

		case KNIGHT:
			return applyMove_standard(gameState, move);

		case BISHOP:
			return applyMove_standard(gameState, move);

		case QUEEN:
			return applyMove_standard(gameState, move);

		case KING:
			return applyMove_king(gameState, move);

		case PAWN:
			return applyMove_pawn(gameState, move);

		default:
			return gameState;
		}
	}
	/*
	 * applyMove_standard moves a piece that is a knight, bishop or queen.
	 */
	private static GameState applyMove_standard(GameState gameState, Move move) {

		GameState afterMove = gameState.clone();
		Piece p = move.getStartSquare(afterMove).getPiece();
		move.getStartSquare(afterMove).setPiece(new NullPiece());
		move.getEndSquare(afterMove).setPiece(p);
		afterMove.setLastMove(move);
		afterMove.togglePlayer();				
		return afterMove;

	}

	private static GameState applyMove_rook(GameState gameState, Move move) {

		GameState afterMove = gameState.clone();
		PIECE_ROOK rook = (PIECE_ROOK) move.getStartSquare(afterMove).getPiece();
		rook.setHasMoved(true);
		move.getStartSquare(afterMove).setPiece(new NullPiece());
		move.getEndSquare(afterMove).setPiece(rook);
		afterMove.setLastMove(move);
		afterMove.togglePlayer();				
		return afterMove;

	}

	private static GameState applyMove_king(GameState gameState, Move move) {

		GameState afterMove = gameState.clone();
		PIECE_KING king = (PIECE_KING) move.getStartSquare(afterMove).getPiece();
		king.setHasMoved(true);
		move.getStartSquare(afterMove).setPiece(new NullPiece());
		move.getEndSquare(afterMove).setPiece(king);
		afterMove.setLastMove(move);
		// check if the move is a castling
		if (move.getStart_file() + 2 == move.getEnd_file()) {
			int rank = move.getStart_rank();
			PIECE_ROOK rook = (PIECE_ROOK) afterMove.getBoard().getSquareByFileAndRank(8, rank).getPiece();
			afterMove.getBoard().getSquareByFileAndRank(8, rank).setPiece(new NullPiece());
			afterMove.getBoard().getSquareByFileAndRank(6, rank).setPiece(rook);
			rook.setHasMoved(true);
		} else if (move.getStart_file() - 2 == move.getEnd_file()) {
			int rank = move.getStart_rank();
			PIECE_ROOK rook = (PIECE_ROOK) afterMove.getBoard().getSquareByFileAndRank(1, rank).getPiece();
			afterMove.getBoard().getSquareByFileAndRank(1, rank).setPiece(new NullPiece());
			afterMove.getBoard().getSquareByFileAndRank(4, rank).setPiece(rook);
			rook.setHasMoved(true);
		}
		afterMove.togglePlayer();				
		return afterMove;

	}

	private static GameState applyMove_pawn(GameState gameState, Move move) {

		GameState afterMove = gameState.clone();
		Piece p = move.getStartSquare(afterMove).getPiece();
		move.getStartSquare(afterMove).setPiece(new NullPiece());
		move.getEndSquare(afterMove).setPiece(p);
		afterMove.setLastMove(move);
		// if the pawn move is a capture move and the captured square was empty, that
		// means it's en passant
		if (move.getStart_file() != move.getEnd_file() && move.getEndSquare(gameState).getPiece().isNullPiece()) {
			if (p.getOwner() == Player.WHITE) {
				Square captured_piece_sq = afterMove.getBoard().getSquareByFileAndRank(move.getEnd_file(),
						move.getEnd_rank() - 1);
				captured_piece_sq.setPiece(new NullPiece());
			} else {
				Square captured_piece_sq = afterMove.getBoard().getSquareByFileAndRank(move.getEnd_file(),
						move.getEnd_rank() + 1);
				captured_piece_sq.setPiece(new NullPiece());
			}
		}
		// if the pawn has reached the last rank, ask for promotion
		if ((p.getOwner() == Player.WHITE && move.getEnd_rank() == 8)
				|| (p.getOwner() == Player.BLACK && move.getEnd_rank() == 1)) {
			
			afterMove.setMessage("Choose piece to promote to");	
			afterMove.setStatus(Status.WAIT_PROMOTION);
			return afterMove;
		}
		afterMove.togglePlayer();				
		return afterMove;
	}

}
