package com.example.springboot_chess_yifan.logic;

import com.example.springboot_chess_yifan.board.NullPiece;
import com.example.springboot_chess_yifan.board.PIECE_BISHOP;
import com.example.springboot_chess_yifan.board.PIECE_KING;
import com.example.springboot_chess_yifan.board.PIECE_KNIGHT;
import com.example.springboot_chess_yifan.board.PIECE_PAWN;
import com.example.springboot_chess_yifan.board.PIECE_QUEEN;
import com.example.springboot_chess_yifan.board.PIECE_ROOK;
import com.example.springboot_chess_yifan.board.Square;
import com.example.springboot_chess_yifan.game.GameState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Move {

	private final int start_file;
	private final int start_rank;
	private final int end_file;
	private final int end_rank;
	private final MoveType moveType;

	public Move(int start_file, int start_rank, int end_file, int end_rank, MoveType moveType) {
		this.start_file = start_file;
		this.start_rank = start_rank;
		this.end_file = end_file;
		this.end_rank = end_rank;
		this.moveType = moveType;
	}

	public static Move generateMove(int start_file, int start_rank, int end_file, int end_rank, MoveType moveType) {
		switch (moveType) {

		case STANDARD:
			return new Move_STANDARD(start_file, start_rank, end_file, end_rank);
		case CASTLING:
			return new Move_CASTLING(start_file, start_rank, end_file, end_rank);
		case ENPASSANT:
			return new Move_ENPASSANT(start_file, start_rank, end_file, end_rank);

		default:
			return null;
		}
	}

	public Square getStartSquare(GameState state) {
		return state.getBoard().getSquareByFileAndRank(start_file, start_rank);
	}

	public Square getEndSquare(GameState state) {
		return state.getBoard().getSquareByFileAndRank(end_file, end_rank);
	}

	@Override
	public String toString() {
		return String.format("(%d,%d) -> (%d,%d)", getStart_file(), getStart_rank(), getEnd_file(), getEnd_rank());
	}

}
