package com.example.springboot_chess_yifan.logic;

public class Move_CASTLING extends Move{

	public Move_CASTLING(int start_file, int start_rank, int end_file, int end_rank) {
		super(start_file, start_rank, end_file, end_rank, MoveType.CASTLING);
	}

}
