package Agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.Move;
import Player.MoveTransition;

/**
 * this class needs an ordering of moves in order to function properly.
 * It enhances the research of moves that are the most promising (25% best). 
 * Other moves with lower depth.
 * @author Alexander
 *
 */
public class MinimaxStat implements MoveStrategy {

	private final BoardEvaluator boardEvaluator;
	public ArrayList<Integer> movesVal = new ArrayList<>();
	
	
	public MinimaxStat() {
		boardEvaluator = new StandardBoardEvaluator();
	}
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
					movesVal.add(currentValue);
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
					movesVal.add(currentValue);
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
	
	private boolean isEndGameScenario(Board board) {
		
		return board.getCurrentPlayer().isInCheckMate() ||
			   board.getCurrentPlayer().isInStaleMate();
	}

	private ArrayList<Move> OrderMoves(final ArrayList<Move> moves){
		
		//return Lists.sort(moves);
		return null;
	}
	
}
