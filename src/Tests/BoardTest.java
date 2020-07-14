package Tests;

import Player.Color;
import Player.MoveTransition;
import Board.*;
import Board.Move.MoveFactory;
import Pieces.*;
import org.junit.Test;

import Agent.Minimax;
import Agent.MoveStrategy;

//import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BoardTest {

    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().isCastled());
        //assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
        //assertTrue(board.getCurrentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.whitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
        //assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
        //assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
        //assertTrue(board.whitePlayer().toString().equals("White"));
        //assertTrue(board.blackPlayer().toString().equals("Black"));
    }
    
    @Test
    public void testFoolsMate() {
    	final Board board = Board.createStandardBoard();
    	final MoveTransition t1 = board.getCurrentPlayer().makeMove(Move.MoveFactory.createMove(board, BoardUtil.getCoordinateAtPosition("f2"), BoardUtil.getCoordinateAtPosition("f3")));
    	assertEquals(t1.getMoveStatus().isDone(), true);
    	
    	final MoveTransition t2 = t1.getTransitionBoard().getCurrentPlayer().makeMove(Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtil.getCoordinateAtPosition("e7"), BoardUtil.getCoordinateAtPosition("e5")));
    	assertEquals(t2.getMoveStatus().isDone(),true);
    	
    	final MoveTransition t3 = t2.getTransitionBoard().getCurrentPlayer().makeMove(Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtil.getCoordinateAtPosition("g2"), BoardUtil.getCoordinateAtPosition("g4")));
    	assertEquals(t3.getMoveStatus().isDone(),true);
    	
    	final MoveStrategy strategy = new Minimax(4);
    	final Move aiMove = strategy.execute(t3.getTransitionBoard());
    	
    	final Move bestMove = Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtil.getCoordinateAtPosition("d8"), BoardUtil.getCoordinateAtPosition("h4"));
    	assertEquals(aiMove,bestMove);
    }
}
