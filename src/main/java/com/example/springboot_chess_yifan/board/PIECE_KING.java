package com.example.springboot_chess_yifan.board;

public class PIECE_KING extends Piece {
	
	private boolean hasMoved = false;
	
	public PIECE_KING(Player owner) {
		super(PieceType.KING, owner);
	}
	
	
	
	public boolean isHasMoved() {
		return hasMoved;
	}



	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}



	@Override
	public Piece clone() {
		PIECE_KING clone = new PIECE_KING(this.getOwner()); 
		clone.setHasMoved(hasMoved);
		return clone;
	}
	
}
