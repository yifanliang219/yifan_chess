package com.example.springboot_chess_yifan.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PIECE_ROOK extends Piece {
	
	private boolean hasMoved = false;
	
	public PIECE_ROOK(Player owner) {
		super(PieceType.ROOK, owner);
	}
	
	@Override
	public Piece clone() {
		PIECE_ROOK clone = new PIECE_ROOK(this.getOwner()); 
		clone.setHasMoved(hasMoved);
		return clone;
	}
	
}
