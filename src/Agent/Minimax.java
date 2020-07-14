package Agent;

import Board.Board;
import Board.Move;
import Player.MoveTransition;

public class Minimax implements MoveStrategy{

	private final BoardEvaluator boardEvaluator;
	private final int searchDepth;
	
	public Minimax(final int searchDepth) {
		this.searchDepth = searchDepth;
		this.boardEvaluator = new StandardBoardEvaluator();//TODO change once implemented
	}
	
	@Override
	public String toString() {
		return "Minimax";
	}
	
	/**
	 * returns the best move
	 */
	@Override
	public Move execute(Board board) {
		
		final long startTime = System.currentTimeMillis();
		Move bestMove = null;
		int highestSeenValue = Integer.MIN_VALUE;
		int lowestSeenValue = Integer.MAX_VALUE;
		int currentValue;
		
		System.out.println(board.getCurrentPlayer()+"THINking wiht depth"+this.searchDepth);
		int numMoves = board.getCurrentPlayer().getLegalMoves().size();
		for(final Move move: board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				
				currentValue = board.getCurrentPlayer().getColor().isWhite()?
							   min(moveTransition.getTransitionBoard(),this.searchDepth-1):
							   max(moveTransition.getTransitionBoard(),this.searchDepth-1);
							   
				if(board.getCurrentPlayer().getColor().isWhite() && currentValue >= highestSeenValue) {
					
					highestSeenValue = currentValue;
					bestMove = move;
						
				}else if( board.getCurrentPlayer().getColor().isBlack() && currentValue <= lowestSeenValue) {
					
					lowestSeenValue = currentValue;
					bestMove = move;
					
				}
			}
		}
		
		final long executionTime = System.currentTimeMillis()-startTime;
		return bestMove;
	}
	
	
	/**
	 * minimax is co-recursive so min calls max and max calls min
	 * @param board
	 * @param depth
	 * @return
	 */
	public int min(final Board board, final int depth) {
		
		if(depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		int lowestSeenValue = Integer.MAX_VALUE;
		for(final Move move: board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = max(moveTransition.getTransitionBoard(),depth-1);
				if(currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
				}
			}
		}
		return lowestSeenValue;
	}
	
	private boolean isEndGameScenario(Board board) {
		
		return board.getCurrentPlayer().isInCheckMate() ||
			   board.getCurrentPlayer().isInStaleMate();
	}

	public int max(final Board board, final int depth) {
		
		if(depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		int highestSeenValue = Integer.MIN_VALUE;
		for(final Move move: board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = min(moveTransition.getTransitionBoard(),depth-1);
				if(currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
				}
			}
		}
		return highestSeenValue;
	}
}
