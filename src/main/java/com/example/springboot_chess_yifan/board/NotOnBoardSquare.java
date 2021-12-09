package com.example.springboot_chess_yifan.board;

public class NotOnBoardSquare extends Square{
	
	public NotOnBoardSquare(int file, int rank) {
		super(file, rank);
	}
	
	@Override
	public boolean isSquareOnBoard() {
		return false;
	}
	
}
