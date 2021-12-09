package com.example.springboot_chess_yifan.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PIECE_KING extends Piece {
	
	private boolean hasMoved = false;
	
	public PIECE_KING(Player owner) {
		super(PieceType.KING, owner);
	}
	
	@Override
	public Piece clone() {
		PIECE_KING clone = new PIECE_KING(this.getOwner()); 
		clone.setHasMoved(hasMoved);
		return clone;
	}
	
}
