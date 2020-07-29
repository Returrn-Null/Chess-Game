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
							   min(moveTransition.getTransitionBoard(),this.searchDepth-1, Integer.MIN_VALUE, Integer.MAX_VALUE):
							   max(moveTransition.getTransitionBoard(),this.searchDepth-1,Integer.MIN_VALUE, Integer.MAX_VALUE);
							   
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
	public int min(final Board board, final int depth, int alpha, int beta) {
		
		if(depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		int lowestSeenValue = Integer.MAX_VALUE;
		for(final Move move: board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = max(moveTransition.getTransitionBoard(),depth-1, alpha, beta);
				if(currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
				}
				if(beta >=currentValue) {
					beta = currentValue;
				}
				if(beta<= alpha) {
					break;
				}
			}
		}
		return lowestSeenValue;
	}
	
	private boolean isEndGameScenario(Board board) {
		
		return board.getCurrentPlayer().isInCheckMate() ||
			   board.getCurrentPlayer().isInStaleMate();
	}

	public int max(final Board board, final int depth, int alpha, int beta) {
		
		if(depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		int highestSeenValue = Integer.MIN_VALUE;
		for(final Move move: board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = min(moveTransition.getTransitionBoard(),depth-1, alpha, beta);
				if(currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
				}
				if(alpha<= currentValue) {
					alpha = currentValue;
				}
				if(beta<= alpha) {
					break;
				}
			}
		}
		return highestSeenValue;
	}
	
	
	/**
	 * other implementation for minimax using only one method, just recursive.
	 * Beginning value for alpha -infinity beta is infinity
	 * @param board
	 * @param depth
	 * @param maxPlayer
	 * @return max or lowest depending on the player
	 */
	public int miniMax(final Board board, final int depth,final  boolean maxPlayer,int alpha,int beta) {
		if(depth == 0 || isEndGameScenario(board)) {
			return this.boardEvaluator.evaluate(board, depth);
		}
		
		if(maxPlayer) {
			int highestSeenValue = Integer.MIN_VALUE;
			for(final Move move: board.getCurrentPlayer().getLegalMoves()) {
				final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
				if(moveTransition.getMoveStatus().isDone()) {
					final int currentValue = miniMax(moveTransition.getTransitionBoard(),depth-1, false, alpha, beta);
					if(currentValue >= highestSeenValue) {
						highestSeenValue = currentValue;
					}
					if(alpha <= currentValue) {
						alpha = currentValue;
					}
					if(beta<=alpha) {
						break;
					}
				}
			}
			return highestSeenValue;
		}
		else {
			int lowestSeenValue = Integer.MAX_VALUE;
			for(final Move move: board.getCurrentPlayer().getLegalMoves()) {
				final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
				if(moveTransition.getMoveStatus().isDone()) {
					final int currentValue = miniMax(moveTransition.getTransitionBoard(),depth-1, true, alpha, beta);
					if(currentValue <= lowestSeenValue) {
						lowestSeenValue = currentValue;
					}
					if(beta>= currentValue) {
						beta = currentValue;
					}
					if(beta<=alpha) {
						break;
					}
				}
			}
			return lowestSeenValue;
		}
	}
}
