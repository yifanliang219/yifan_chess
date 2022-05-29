package com.example.springboot_chess_yifan.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

	// Square[rank][file]
	private Square[][] squares = new Square[8][8];
	
	

	public Board(Square[][] squares) {
		this.squares = squares;
	}

	public static Board generateDefaultBoard() {

		Square[][] squares = new Square[8][8];
		Board board = new Board(squares);

		// generate chess board with no pieces
		for (int file = 1; file <= 8; file++) {
			for (int rank = 1; rank <= 8; rank++) {
				Square sq = new Square(file, rank);
				squares[8 - rank][file - 1] = sq;
			}
		}

		// create map to loop setup for pieces
		Map<Integer, Player> rankToColor = new HashMap<>();
		rankToColor.put(1, Player.WHITE);
		rankToColor.put(8, Player.BLACK);

		Map<Integer, PieceType> fileToType = new HashMap<>();
		fileToType.put(1, PieceType.ROOK);
		fileToType.put(2, PieceType.KNIGHT);
		fileToType.put(3, PieceType.BISHOP);
		fileToType.put(8, PieceType.ROOK);
		fileToType.put(7, PieceType.KNIGHT);
		fileToType.put(6, PieceType.BISHOP);
		fileToType.put(4, PieceType.QUEEN);
		fileToType.put(5, PieceType.KING);

		// set up major pieces
		for (Integer rank : rankToColor.keySet()) {
			for (Integer file : fileToType.keySet()) {
				Player color = rankToColor.get(rank);
				PieceType type = fileToType.get(file);
				Piece piece = Piece.generatePiece(type, color);
				Square square = board.getSquareByFileAndRank(file, rank);
				square.setPiece(piece);
			}
		}

		// set up white pawns
		for (int file = 1; file <= 8; file++) {
			int rank = 2;
			Piece piece = new PIECE_PAWN(Player.WHITE);
			Square square = board.getSquareByFileAndRank(file, rank);
			square.setPiece(piece);
		}

		// set up black pawns
		for (int file = 1; file <= 8; file++) {
			int rank = 7;
			Piece piece = new PIECE_PAWN(Player.BLACK);
			Square square = board.getSquareByFileAndRank(file, rank);
			square.setPiece(piece);
		}

		return board;
	}

	public Square getSquareByFileAndRank(int file, int rank) {

		Square sq = null;
		try {
			sq = squares[8 - rank][file - 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return new NotOnBoardSquare(file, rank);
		}
		return sq;

	}

	public List<Square> getAllSquares() {
		List<Square> allSq = new ArrayList<>();
		for (Square[] rank : squares) {
			for (Square sq : rank) {
				allSq.add(sq);
			}
		}
		return allSq;
	}

	public List<Square> getAllSquares(Player player) {
		List<Square> allSq = new ArrayList<>();
		for (Square[] rank : squares) {
			for (Square sq : rank) {
				if (sq.getPiece().getOwner() == player) {
					allSq.add(sq);
				}
			}
		}
		return allSq;
	}
	
	public Square getSquareOfKing_player(Player player) {
		Square king_sq = null;
		for (Square sq : this.getAllSquares()) {
			Piece p = sq.getPiece();
			if (p.getPieceType() == PieceType.KING && p.getOwner() == player) {
				king_sq = sq;
				break;
			}
		}
		return king_sq;
	}

	public void printBoard() {
		for (int rank = 8; rank >= 1; rank--) {
			for (int file = 1; file <= 8; file++) {
				System.out.print(this.getSquareByFileAndRank(file, rank) + " ");
			}
			System.out.println();
		}
	}
	
	

	public Square[][] getSquares() {
		return squares;
	}

	public void setSquares(Square[][] squares) {
		this.squares = squares;
	}

	@Override
	public Board clone() {
		Square[][] clonedSq = new Square[8][8];
		for (int file = 1; file <= 8; file++) {
			for (int rank = 1; rank <= 8; rank++) {
				clonedSq[8 - rank][file - 1] = getSquareByFileAndRank(file, rank).clone();
			}
		}
		return new Board(clonedSq);
	}

}
