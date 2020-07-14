package Agent;

import Board.Board;
import Pieces.Piece;
import Player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {

	private static final int CHECK_BONUS = 100;//TODO tweak this value and test
	private static final int CHECK_MATE_BONUS = 10000;
	private static final int DEPTH_BONUS = 100;
	private static final int CASTLED_BONUS = 60;

	@Override
	public int evaluate(Board board, int depth) {
		
		//get score from white - score from black
		return scorePlayer(board,board.whitePlayer(),depth)-
			   scorePlayer(board, board.blackPlayer(),depth);
	}

	private int scorePlayer(final Board board,final Player player,final int depth) {
		
		return pieceValue(player) + 
			   mobility(player) +
			   check(player) + 
			   checkMate(player, depth) +
			   castled(player);
	}

	private static int castled(final Player player) {

		return player.isCastled() ?CASTLED_BONUS :0;
	}

	private static int checkMate(final Player player, final int depth) {
		return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS*depthBonus(depth) : 0;
	}

	
	private static int depthBonus(final int depth) {
		return depth == 0? 1: DEPTH_BONUS*depth;
	}

	private static int check(final Player player) {
		return player.getOpponent().isInCheck() ? CHECK_BONUS: 0;
	}

	/**
	 * returns the possibilities a player has in a certain position
	 * @param player
	 * @return mobility (in legal moves)
	 */
	private static int mobility(final Player player) {
		return player.getLegalMoves().size();
	}
	
	/**
	 * 
	 * assigns the value of piecetype for a player
	 * @param player
	 * @return
	 */
	private static int pieceValue(final Player player) {
		
		int pieceValueScore = 0;
		for(final Piece piece: player.getActivePieces()) {
			pieceValueScore+= piece.getPieceValue();
		}
		return pieceValueScore;
	}

}
