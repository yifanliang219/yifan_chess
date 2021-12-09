package com.example.springboot_chess_yifan;

import java.util.concurrent.Future;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.springboot_chess_yifan.board.Board;
import com.example.springboot_chess_yifan.game.GameState;
import com.example.springboot_chess_yifan.logic.Move;
import com.example.springboot_chess_yifan.logic.MoveApplier;

@SpringBootApplication
public class SpringbootChessYifanApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootChessYifanApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	/*
		System.out.println("Initial board:");
		GameState state = new GameState();
		Board board = state.getBoard();
		board.printBoard();
		state.getPossibleMovesSummary();

		
		Move moveWhiteKnight = new Move(4, 2, 4, 4);
		state = MoveApplier.applyMove(state, moveWhiteKnight);
		state.getBoard().printBoard();
		state.getPossibleMovesSummary();

		
		Move moveBlackPawn = new Move(5, 7, 5, 5);
		state = MoveApplier.applyMove(state, moveBlackPawn);
		state.getBoard().printBoard();
		state.getPossibleMovesSummary();

		
		Move moveWhiteKnight2 = new Move(4, 4, 5, 5);
		state = MoveApplier.applyMove(state, moveWhiteKnight2);
		state.getBoard().printBoard();
		state.getPossibleMovesSummary();
		
		Move moveWhiteKnight3 = new Move(6, 7, 6, 5);
		state = MoveApplier.applyMove(state, moveWhiteKnight3);
		state.getBoard().printBoard();
		state.getPossibleMovesSummary();*/
		
		
		
	}
	

}
