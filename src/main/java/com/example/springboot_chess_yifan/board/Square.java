package com.example.springboot_chess_yifan.board;


public class Square {

	private final int file;
	private final int rank;
	// color = "BLACK"/"WHITE"
	private final String color;
	// position = "(file,rank)", e.g., (1,8) means 1st file and 8th rank
	private final String position;
	private Piece piece;

	public Square(int file, int rank) {
		this.file = file;
		this.rank = rank;
		this.color = calculateColor(file, rank);
		this.position = "(" + file + "," + rank + ")";
		this.piece = new NullPiece();
	}

	public Square(int file, int rank, Piece piece) {
		this(file, rank);
		this.piece = piece;
	}

	private String calculateColor(int file, int rank) {
		if ((file + rank) % 2 == 0) {
			return "DARK";
		} else {
			return "LIGHT";
		}
	}
	
	public boolean isSquareOnBoard() {
		return true;
	}
	
	@Override
	public String toString() {
		if(piece.isNullPiece()) {
			return color;
		}else {
			return piece.toString();
		}
	}
	
	
	
	public Piece getPiece() {
		return piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public int getFile() {
		return file;
	}

	public int getRank() {
		return rank;
	}

	public String getColor() {
		return color;
	}

	public String getPosition() {
		return position;
	}

	public boolean equals(Square sq) {
		return (this.file == sq.file && this.rank == sq.rank);
	}
	
	@Override
	public Square clone() {
		Square clone = new Square(file, rank, piece.clone());
		return clone;
	}

}
