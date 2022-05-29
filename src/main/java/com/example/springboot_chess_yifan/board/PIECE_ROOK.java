package com.example.springboot_chess_yifan.board;

public class PIECE_ROOK extends Piece {
	
	private boolean hasMoved = false;
	
	public PIECE_ROOK(Player owner) {
		super(PieceType.ROOK, owner);
	}
	
	
	
	public boolean isHasMoved() {
		return hasMoved;
	}



	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}



	@Override
	public Piece clone() {
		PIECE_ROOK clone = new PIECE_ROOK(this.getOwner()); 
		clone.setHasMoved(hasMoved);
		return clone;
	}
	
}
