package Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import Board.*;
import Pieces.*;

public abstract class Player {

	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> LegalMoves;
	private final boolean isInCheck;
		
	Player(final Board board,final Collection<Move> legalMoves, final Collection<Move> opponentMoves){
			
		this.board = board;
		this.playerKing = establishKing();
		for(Move move : calculateKingCastles(legalMoves,opponentMoves)) {
			legalMoves.add(move);
		}
		this.LegalMoves = legalMoves;
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
	}
	
	public King getPlayerKing() {
		return this.playerKing;
	}
	
	public Collection<Move> getLegalMoves(){
		return this.LegalMoves;
	}

	protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		for(final Move move: moves) {
			if(piecePosition == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		return attackMoves;
	}

	private King establishKing() {
		for(final Piece piece : getActivePieces()) {
			if(piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		
		throw new RuntimeException("Should ot reach here, not a valid board, no king!");
	}
	
	public boolean isMoveLegal(final Move move) {
		
		return this.LegalMoves.contains(move);
	}
	
	public boolean isInCheck() {
		
		
		return this.isInCheck;
	}
	
	public boolean isInCheckMate() {
		return this.isInCheck && !hasEscapeMoves() ;
	}
	
	protected boolean hasEscapeMoves() {
		
		for(final Move move : this.LegalMoves) {
			final MoveTransition transition = makeMove(move);
			if(transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}

	public boolean isInStaleMate() {
		return !this.isInCheck && !hasEscapeMoves();
	}
	
	public boolean isCastled() {
		return false;
	}
	
	public boolean isKingSideCastleCapable() {
		return this.playerKing.isKingSideCastleCapable();
	}
	
	public boolean isQueenSideCastleCapable() {
		return this.playerKing.isQueenSideCastleCapable();
	}
	
	public MoveTransition makeMove(final Move move) {
		
		if(!isMoveLegal(move)) {
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL);
		}
		
		final Board transitionBoard = move.execute();
		
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.getCurrentPlayer().getLegalMoves());
		
		if(!kingAttacks.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_IN_CHECK);
		}
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	public abstract Collection<Piece> getActivePieces();
	public abstract Color getColor();
	public abstract Player getOpponent();
	public abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
}
