package com.example.springboot_chess_yifan.logic;

import java.util.ArrayList;
import java.util.List;

import com.example.springboot_chess_yifan.board.PIECE_KING;
import com.example.springboot_chess_yifan.board.PIECE_ROOK;
import com.example.springboot_chess_yifan.board.Piece;
import com.example.springboot_chess_yifan.board.PieceType;
import com.example.springboot_chess_yifan.board.Player;
import com.example.springboot_chess_yifan.board.Square;
import com.example.springboot_chess_yifan.game.GameState;

public class MoveCalculator {

	public static List<Move> possibleMovesByPlayer(GameState gameState, Player player) {
		
		List<Move> possibleMoves =  new ArrayList<>();
		List<Move> safeKingMoves = kingSafetyFilter_noCastling(gameState, player, possibleMovesByPlayer_noCastling_without_kingSafety(gameState, player));
		List<Move> castlingMoves = getCastlingMove(gameState, player);
		possibleMoves.addAll(safeKingMoves);
		possibleMoves.addAll(castlingMoves);
		return possibleMoves;
				
	}

	private static List<Move> possibleMovesByPlayer_noCastling_without_kingSafety(GameState gameState, Player player) {
		List<Square> player_squares = gameState.getBoard().getAllSquares(player);
		List<Move> possibleMoves = new ArrayList<>();
		for (Square sq : player_squares) {
			possibleMoves.addAll(possibleMovesBySquare_noCastling(gameState, sq));
		}
		return possibleMoves;
	}

	private static List<Move> possibleMovesBySquare_noCastling(GameState gameState, Square sq) {
		switch (sq.getPiece().getPieceType()) {

		case ROOK:
			return possibleMoves_rook(gameState, sq);
		case KNIGHT:
			return possibleMoves_knight(gameState, sq);
		case BISHOP:
			return possibleMoves_bishop(gameState, sq);
		case QUEEN:
			return possibleMoves_queen(gameState, sq);
		case KING:
			return possibleMoves_king(gameState, sq);
		case PAWN:
			return possibleMoves_pawn(gameState, sq);

		default:
			return new ArrayList<>();

		}
	}

	private static List<Move> kingSafetyFilter_noCastling(GameState gameState, Player player, List<Move> standardMoves) {

		List<Move> safeKingMoves = new ArrayList<>();

		for (Move move : standardMoves) {
			GameState testState = MoveApplier.applyMove(gameState, move);
			if (isKingSafe(testState, player)) {
				safeKingMoves.add(move);
			}
		}
		return safeKingMoves;

	}

	private static boolean isSquareSafe(GameState gameState, Player attacker, Square sq) {

		boolean isSafe = true;
		for (Move move : possibleMovesByPlayer_noCastling_without_kingSafety(gameState, attacker)) {
			if (move.getEndSquare(gameState).equals(sq)) {
				isSafe = false;
				break;
			}
		}
		return isSafe;

	}

	private static boolean isKingSafe(GameState gameState, Player player) {

		return isSquareSafe(gameState, player.togglePlayer(), gameState.getBoard().getSquareOfKing_player(player));
	}

	private static List<Move> getCastlingMove(GameState gameState, Player player) {
		// king castling

		List<Move> castling = new ArrayList<>();
		Square startSq = gameState.getBoard().getSquareOfKing_player(player);
		int file = startSq.getFile();
		int rank = startSq.getRank();
		PIECE_KING king = (PIECE_KING) startSq.getPiece();

		// if the king has not moved since start of the game
		if (!king.isHasMoved()) {
			
			// check right castling
			boolean canCastleLeft = false;
			Square oneSqRight = gameState.getBoard().getSquareByFileAndRank(6, rank);
			Square twoSqRight = gameState.getBoard().getSquareByFileAndRank(7, rank);
			Square threeSqRight = gameState.getBoard().getSquareByFileAndRank(8, rank);
			// if the two squares right next to the king are empty
			if (oneSqRight.getPiece().isNullPiece() && twoSqRight.getPiece().isNullPiece()) {
				// if the piece at 8th file is a friendly rook
				if (threeSqRight.getPiece().getPieceType() == PieceType.ROOK
						&& threeSqRight.getPiece().getOwner() == king.getOwner()) {
					PIECE_ROOK right_rook = (PIECE_ROOK) threeSqRight.getPiece();
					if (!right_rook.isHasMoved()) {
						canCastleLeft = true;
						// if the 3 squares are safe
						for (Square p : new Square[] { startSq, oneSqRight, twoSqRight }) {
							if (!isSquareSafe(gameState, gameState.getActive_player().togglePlayer(), p)) {
								canCastleLeft = false;
								break;
							}
						}
					}
				}
			}
			if(canCastleLeft) {
				Move kingRightCastling = new Move_CASTLING(file, rank, 7, rank);
				castling.add(kingRightCastling);
			}
				
				
				
			// check left castling
			boolean canCastleRight = false;
			Square oneSqLeft = gameState.getBoard().getSquareByFileAndRank(4, rank);
			Square twoSqLeft = gameState.getBoard().getSquareByFileAndRank(3, rank);
			Square threeSqLeft = gameState.getBoard().getSquareByFileAndRank(2, rank);
			Square fourSqLeft = gameState.getBoard().getSquareByFileAndRank(1, rank);
			// if the 3 squares left next to the king are empty
			if (oneSqLeft.getPiece().isNullPiece() && twoSqLeft.getPiece().isNullPiece()
					&& threeSqLeft.getPiece().isNullPiece()) {
				// if the piece at 1st file is a friendly rook
				if (fourSqLeft.getPiece().getPieceType() == PieceType.ROOK
						&& fourSqLeft.getPiece().getOwner() == king.getOwner()) {
					PIECE_ROOK left_rook = (PIECE_ROOK) fourSqLeft.getPiece();
					if (!left_rook.isHasMoved()) {
						canCastleRight = true;
						// if the 3 squares are safe
						for (Square p : new Square[] { startSq, oneSqLeft, twoSqLeft }) {
							if (!isSquareSafe(gameState, gameState.getActive_player().togglePlayer(), p)) {
								canCastleRight = false;
								break;
							}
						}
						
					}
				}
			}
			if(canCastleRight) {
				Move kingLeftCastling = new Move_CASTLING(file, rank, 3, rank);
				castling.add(kingLeftCastling);
			}
			
		}
		return castling;
	}

	private static List<Move> possibleMoves_rook(GameState gameState, Square startSq) {

		int[][] moveIncrements = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

		return rookBishopQueenMoves(gameState, startSq, moveIncrements);

	}

	private static List<Move> possibleMoves_knight(GameState gameState, Square startSq) {

		// 8 possible squares for moving a knight to
		int[][] moveIncrements = { { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }, { 2, 1 }, { 2, -1 }, { -2, 1 },
				{ -2, -1 } };

		return knightKingMoves(gameState, startSq, moveIncrements);

	}

	private static List<Move> possibleMoves_bishop(GameState gameState, Square startSq) {

		int[][] moveIncrements = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

		return rookBishopQueenMoves(gameState, startSq, moveIncrements);

	}

	private static List<Move> possibleMoves_queen(GameState gameState, Square startSq) {

		int[][] moveIncrements = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }, { 1, 0 }, { -1, 0 }, { 0, 1 },
				{ 0, -1 } };

		return rookBishopQueenMoves(gameState, startSq, moveIncrements);

	}

	private static List<Move> possibleMoves_king(GameState gameState, Square startSq) {

		int[][] moveIncrements = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }, { 1, 0 }, { -1, 0 }, { 0, 1 },
				{ 0, -1 } };

		return knightKingMoves(gameState, startSq, moveIncrements);

	}

	private static List<Move> possibleMoves_pawn(GameState gameState, Square startSq) {

		if (startSq.getPiece().getOwner().equals(Player.WHITE)) {
			return possibleMoves_whitePawn(gameState, startSq);
		} else {
			return possibleMoves_blackPawn(gameState, startSq);
		}

	}

	private static List<Move> possibleMoves_whitePawn(GameState gameState, Square startSq) {

		List<Move> moves = new ArrayList<>();
		int file = startSq.getFile();
		int rank = startSq.getRank();

		// check forward moves
		// no need to check if the square is outside of the board since the pawn should
		// have promoted at the opposite edge
		Square oneSqForward = gameState.getBoard().getSquareByFileAndRank(file, rank + 1);
		if (oneSqForward.getPiece().isNullPiece()) {
			Move moveOneSqForward = new Move_STANDARD(file, rank, file, rank + 1);
			moves.add(moveOneSqForward);
			if (rank == 2) {
				Square twoSqForward = gameState.getBoard().getSquareByFileAndRank(file, rank + 2);
				if (twoSqForward.getPiece().isNullPiece()) {
					Move moveTwoSqForward = new Move_STANDARD(file, rank, file, rank + 2);
					moves.add(moveTwoSqForward);
				}
			}
		}

		// check capture moves
		Square left_cap_sq = gameState.getBoard().getSquareByFileAndRank(file - 1, rank + 1);
		Square right_cap_sq = gameState.getBoard().getSquareByFileAndRank(file + 1, rank + 1);

		Piece lp = left_cap_sq.getPiece();
		if (lp.getOwner() == Player.BLACK) {
			Move left_capture = new Move_STANDARD(file, rank, file - 1, rank + 1);
			moves.add(left_capture);
		}

		Piece rp = right_cap_sq.getPiece();
		if (rp.getOwner() == Player.BLACK) {
			Move right_capture = new Move_STANDARD(file, rank, file + 1, rank + 1);
			moves.add(right_capture);
		}

		// check en passent
		Move lastMove = gameState.getLastMove();

		if (rank != 5 || lastMove == null) {
			return moves;
		}
		Square start_lastMove = lastMove.getStartSquare(gameState);
		Square end_lastMove = lastMove.getEndSquare(gameState);
		Piece piece_lastMove = end_lastMove.getPiece();

		// check left en passent
		if (piece_lastMove.getPieceType() == PieceType.PAWN && start_lastMove.getRank() == 7
				&& start_lastMove.getFile() == file - 1 && end_lastMove.getRank() == 5) {
			Move left_enPassent = new Move_ENPASSANT(file, rank, file - 1, rank + 1);
			moves.add(left_enPassent);
		}
		// check right en passent
		else if (piece_lastMove.getPieceType() == PieceType.PAWN && start_lastMove.getRank() == 7
				&& start_lastMove.getFile() == file + 1 && end_lastMove.getRank() == 5) {
			Move right_enPassent = new Move_ENPASSANT(file, rank, file + 1, rank + 1);
			moves.add(right_enPassent);
		}

		return moves;
	}

	private static List<Move> possibleMoves_blackPawn(GameState gameState, Square startSq) {

		List<Move> moves = new ArrayList<>();
		int file = startSq.getFile();
		int rank = startSq.getRank();

		// check forward moves
		// no need to check if the square is outside of the board since the pawn should
		// have promoted at the opposite edge
		Square oneSqForward = gameState.getBoard().getSquareByFileAndRank(file, rank - 1);
		if (oneSqForward.getPiece().isNullPiece()) {
			Move moveOneSqForward = new Move_STANDARD(file, rank, file, rank - 1);
			moves.add(moveOneSqForward);
			if (rank == 7) {
				Square twoSqForward = gameState.getBoard().getSquareByFileAndRank(file, rank - 2);
				if (twoSqForward.getPiece().isNullPiece()) {
					Move moveTwoSqForward = new Move_STANDARD(file, rank, file, rank - 2);
					moves.add(moveTwoSqForward);
				}
			}
		}

		// check capture moves
		Square left_cap_sq = gameState.getBoard().getSquareByFileAndRank(file - 1, rank - 1);
		Square right_cap_sq = gameState.getBoard().getSquareByFileAndRank(file + 1, rank - 1);

		Piece lp = left_cap_sq.getPiece();
		if (lp.getOwner() == Player.WHITE) {
			Move left_capture = new Move_STANDARD(file, rank, file - 1, rank - 1);
			moves.add(left_capture);
		}

		Piece rp = right_cap_sq.getPiece();
		if (rp.getOwner() == Player.WHITE) {
			Move right_capture = new Move_STANDARD(file, rank, file + 1, rank - 1);
			moves.add(right_capture);
		}

		// check en passent
		Move lastMove = gameState.getLastMove();

		if (rank != 4 || lastMove == null)
			return moves;

		Square start_lastMove = lastMove.getStartSquare(gameState);
		Square end_lastMove = lastMove.getEndSquare(gameState);
		Piece piece_lastMove = end_lastMove.getPiece();

		// check left en passent
		if (piece_lastMove.getPieceType() == PieceType.PAWN && start_lastMove.getRank() == 2
				&& start_lastMove.getFile() == file - 1 && end_lastMove.getRank() == 4) {
			Move left_enPassent = new Move_ENPASSANT(file, rank, file - 1, rank - 1);
			moves.add(left_enPassent);
		}
		// check right en passent
		else if (piece_lastMove.getPieceType() == PieceType.PAWN && start_lastMove.getRank() == 2
				&& start_lastMove.getFile() == file + 1 && end_lastMove.getRank() == 4) {
			Move right_enPassent = new Move_ENPASSANT(file, rank, file + 1, rank - 1);
			moves.add(right_enPassent);
		}

		return moves;
	}

	// get moves for rook, bishop and queen
	private static List<Move> rookBishopQueenMoves(GameState gameState, Square startSq, int[][] moveIncrements) {
		ArrayList<Move> moves = new ArrayList<>();

		int file = startSq.getFile();
		int rank = startSq.getRank();

		// for each direction
		for (int[] pos_incre : moveIncrements) {
			int file_incre = pos_incre[0];
			int rank_incre = pos_incre[1];
			// initialize the first square to check
			int des_file = file + file_incre;
			int des_rank = rank + rank_incre;
			Square sq = gameState.getBoard().getSquareByFileAndRank(des_file, des_rank);
			// if the square is not outside of the board
			while (sq.isSquareOnBoard()) {
				Piece des_piece = sq.getPiece();
				// if it is an empty square
				if (des_piece.isNullPiece()) {
					// can move to the square
					Move move = new Move_STANDARD(file, rank, des_file, des_rank);
					moves.add(move);
					// check the next square in the same direction
					des_file += file_incre;
					des_rank += rank_incre;
					sq = gameState.getBoard().getSquareByFileAndRank(des_file, des_rank);
					continue;
				} else {
					// if the piece on destination square is an enemy piece, the piece can be
					// captured.
					if (!(des_piece.getOwner() == startSq.getPiece().getOwner())) {
						Move move = new Move_STANDARD(file, rank, des_file, des_rank);
						moves.add(move);
					}
					// otherwise, it is a friendly piece, cannot move to the square
					break;
				}
			}
		}
		return moves;
	}

	// get moves for knight and king
	public static List<Move> knightKingMoves(GameState gameState, Square startSq, int[][] moveIncrements) {
		List<Move> moves = new ArrayList<>();

		int file = startSq.getFile();
		int rank = startSq.getRank();

		// for 8 directions
		for (int[] pos_incre : moveIncrements) {
			int file_incre = pos_incre[0];
			int rank_incre = pos_incre[1];
			// initialize the first square to check
			int des_file = file + file_incre;
			int des_rank = rank + rank_incre;
			Square sq = gameState.getBoard().getSquareByFileAndRank(des_file, des_rank);
			// if the square is not outside of the board
			if (sq.isSquareOnBoard()) {
				Piece des_piece = sq.getPiece();
				// if it is an empty square
				if (des_piece.getOwner() != startSq.getPiece().getOwner()) {
					// can move to the square
					Move move = new Move_STANDARD(file, rank, des_file, des_rank);
					moves.add(move);
				}
			}
		}

		return moves;
	}

}
