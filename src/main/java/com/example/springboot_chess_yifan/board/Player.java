package com.example.springboot_chess_yifan.board;

public enum Player {

	BLACK, WHITE, NULL;

	public Player togglePlayer() {
		if (this == Player.BLACK)
			return Player.WHITE;
		else if (this == Player.WHITE)
			return Player.BLACK;
		else
			return Player.NULL;
	}

}
