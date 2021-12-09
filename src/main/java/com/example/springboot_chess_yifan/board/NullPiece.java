package com.example.springboot_chess_yifan.board;

public class NullPiece extends Piece{

	public NullPiece() {
		super(PieceType.NULL, Player.NULL);
	}
	
	@Override
	public boolean isNullPiece() {
		return true;
	}

}
