package com.example.springboot_chess_yifan.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Piece {

	private final PieceType pieceType;
	private final Player owner;

	public Piece(PieceType pieceType, Player owner) {
		this.pieceType = pieceType;
		this.owner = owner;
	}

	public static Piece generatePiece(PieceType pieceType, Player owner) {
		switch (pieceType) {

		case KING:
			return new PIECE_KING(owner);
		case QUEEN:
			return new PIECE_QUEEN(owner);
		case ROOK:
			return new PIECE_ROOK(owner);
		case KNIGHT:
			return new PIECE_KNIGHT(owner);
		case BISHOP:
			return new PIECE_BISHOP(owner);
		case PAWN:
			return new PIECE_PAWN(owner);

		default:
			return new NullPiece();
		}
	}
	
	public boolean isNullPiece() {
		return false;
	}

	public String toString() {
		return owner.name() + pieceType.name();
	}
	
	@Override
	public Piece clone() {
		Piece clone = generatePiece(pieceType, owner);
		return clone;
	}
}
