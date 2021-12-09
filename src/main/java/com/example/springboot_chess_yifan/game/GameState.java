package com.example.springboot_chess_yifan.game;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.springboot_chess_yifan.board.Board;
import com.example.springboot_chess_yifan.board.Player;
import com.example.springboot_chess_yifan.logic.Move;
import com.example.springboot_chess_yifan.logic.MoveCalculator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class GameState {
	
	private Board board = Board.generateDefaultBoard();	
	private Player active_player = Player.WHITE;
	private Move lastMove = null;
	private String message = "";
	private GameState previousState = null;
	private Status status = Status.WAIT_MOVE;
	
	public GameState(Board board, Player active_player, Move lastMove) {
		this.board = board;
		this.active_player = active_player;
		this.lastMove = lastMove;
	}
	
	public void togglePlayer() {
		this.active_player = active_player.togglePlayer();
	}
	
	public List<Move> getPossibleMovesByActivePlayer(){
		return MoveCalculator.possibleMovesByPlayer(this, this.active_player);
	}
	
	@Override
	public GameState clone() {
		Board clonedBoard = board.clone();
		Player clonedActivePlayer = active_player;
		Move clonedLastMove = lastMove;
		GameState clone = new GameState(clonedBoard, clonedActivePlayer, clonedLastMove);
		clone.setPreviousState(previousState);
		clone.setMessage(message);
		return clone;
	}
	
	public String getPossibleMovesSummary() {
		String summary = "Possible Moves by " + active_player + ":<br>";
		for(Move m: getPossibleMovesByActivePlayer()) {
			summary += m.getStartSquare(this).getPiece().getPieceType() + m.toString() + "<br>";
		}
		return summary;
	}
	
}
