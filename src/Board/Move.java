package Board;

import Board.Board.Builder;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Rook;

public abstract class Move {

	
	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCooard;
	protected final boolean isFirstMove;
	
	public static final Move NULL_MOVE = new NullMove();
	
	private Move(final Board board, final Piece piece, final int DestCoord){
		this.board = board;
		this.movedPiece = piece;
		this.destinationCooard = DestCoord;
		this.isFirstMove = movedPiece.isFirstMove();
	}
	
	private Move(final Board board, final int destination) {
		this.board = board;
		this.destinationCooard = destination;
		this.movedPiece = null;
		this.isFirstMove = false;
	}
	
	public boolean isCastlingMove() {
		return false;
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime*result+this.destinationCooard;
		result = prime*result + this.movedPiece.hashCode();
		result = prime*result + this.movedPiece.getPiecePosition();
		return result;
	}
	
	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof Move)) {
			return false;
		}
		
		final Move otherMove = (Move) other;
		return getCurrentCoordinate() == otherMove.getCurrentCoordinate() && 
			   getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
			   getMovedPiece().equals(otherMove.getMovedPiece());
	}
	
	
	public int getCurrentCoordinate() {
		return this.movedPiece.getPiecePosition();
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public Board execute() {
		
		final Board.Builder builder = new Builder();
		
		for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
			if(!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		
		for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}
		
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getColor());
		return builder.build();
	}
	
	
	
	public Piece getMovedPiece() {
		return this.movedPiece;
	}
	
	public boolean isAttack() {
		return false;
	}
	
	public boolean isCastling() {
		return false;
	}
	
	public Piece getAttackedPiece() {
		return null;
	}
	
	public static class AttackMove extends Move{
		
		final Piece attackedPiece;
		public AttackMove(final Board board, final Piece piece,final int DestCoord, final Piece attackedPiece) {
			super(board, piece, DestCoord);
			this.attackedPiece = attackedPiece;
		}
		
		@Override
		public boolean isAttack() {
			return true;
		}
		
		@Override
		public Piece getAttackedPiece() {
			return this.attackedPiece;
		}
		
		@Override
		public int hashCode() {
			return this.attackedPiece.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean equals(final Object other) {
			if(this == other) {
				return true;
			}
			if(!(other instanceof AttackMove)) {
				return false;
			}
			
			final AttackMove otherAttackMove = (AttackMove) other;
			return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
		}
	}
	
	public static class MajorAttackMove extends AttackMove{
		
		public MajorAttackMove(final Board board, final Piece pieceMoved, final int destination, final Piece pieceAttacked) {
			super(board,pieceMoved, destination, pieceAttacked);
		}
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof MajorAttackMove && super.equals(other);
		}
		
		@Override
		public String toString() {
			return movedPiece.getPieceType()+BoardUtil.getPositionAtCoord(this.destinationCooard);
		}
		
	}
	
	public static final class MajorMove extends Move{

		public MajorMove(final Board board, final Piece piece, final int DestCoord) {
			super(board, piece, DestCoord);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof MajorMove && super.equals(other);
		}
		
		@Override
		public String toString() {
			return movedPiece.getPieceType().toString()+BoardUtil.getPositionAtCoord(this.destinationCooard);
		}
	}
	
	public static final class PawnMove extends Move{

		public PawnMove(final Board board, final Piece piece, final int DestCoord) {
			super(board, piece, DestCoord);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnMove && super.equals(other);
		}
		
		@Override
		public String toString() {
			return BoardUtil.getPositionAtCoord(this.destinationCooard);
		}
		
	}
	
	public static class PawnAttackMove extends AttackMove{
		
		public PawnAttackMove(final Board board, final Piece piece, final int DestCoord, final Piece attackedPiece) {
			super(board, piece, DestCoord, attackedPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnAttackMove && super.equals(other);
		}
		
		@Override
		public String toString() {
			return BoardUtil.getPositionAtCoord(this.movedPiece.getPiecePosition()).substring(0,1)+"x"+ BoardUtil.getPositionAtCoord(this.destinationCooard);
		}
		
	}
	
	public static final class PawnEnPassantAttack extends PawnAttackMove{
		
		public PawnEnPassantAttack(final Board board, final Piece piece, final int DestCoord, final Piece attackedPiece) {
			super(board, piece, DestCoord, attackedPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnEnPassantAttack && super.equals(other);
		}
		
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				if(!piece.equals(this.getAttackedPiece())) {
					builder.setPiece(piece);
				}
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getColor());
			return builder.build();
		}
		
	}
	
	public int getDestinationCoordinate() {
		return this.destinationCooard;
	}
	
	public static class PawnPromotion extends Move{
		
		final Move decorateMove;
		final Pawn promotedPawn;
		
		public PawnPromotion(final Move decorateMove) {
			super(decorateMove.getBoard(),decorateMove.getMovedPiece(),decorateMove.getDestinationCoordinate());
			this.decorateMove = decorateMove;
			this.promotedPawn = (Pawn) decorateMove.getMovedPiece();
		}
		
		@Override
		public Board execute() {
			
			final Board pawnMovedBoard = this.decorateMove.execute();
			final Board.Builder builder = new Builder();
			for(final Piece piece: pawnMovedBoard.getCurrentPlayer().getActivePieces()) {
				if(!this.promotedPawn.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for(final Piece piece: pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.promotedPawn.getPromotedPiece().movePiece(this));
			builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getColor());
			
			return builder.build();
		}
		
		@Override
		public boolean isAttack() {
			return this.decorateMove.isAttack();
		}
		
		@Override
		public Piece getAttackedPiece() {
			return this.decorateMove.getAttackedPiece();
		}
		
		@Override
		public String toString() {
			return "";//TODO implement this
		}
		
		@Override
		public int hashCode() {
			return decorateMove.hashCode()+(31*promotedPawn.hashCode());
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof PawnPromotion && super.equals(other);
		}
		
	}
	
	public static final class PawnJump extends Move{

		public PawnJump(final Board board, final Piece piece, final int DestCoord) {
			super(board, piece, DestCoord);
		}
		
		@Override
		public Board execute() {
			
			final Builder builder = new Builder();
			for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getColor());
			return builder.build();
		}
		
		@Override
		public String toString() {
			return BoardUtil.getPositionAtCoord(this.destinationCooard);
		}
		
	}
	
	static abstract class CastleMove extends Move{

		protected final Rook castleRook;
		protected final int castleRookDestination;
		protected final int castleRookStart;
		public CastleMove(final Board board, final Piece piece, final int DestCoord,final int castleRookDestination, final int castleRookStart, final Rook castleRook) {
			super(board, piece, DestCoord);
			this.castleRook = castleRook;
			this.castleRookDestination = castleRookDestination;
			this.castleRookStart = castleRookStart;
		}
		
		public Rook getCastleRook() {
			return this.castleRook;
		}
		
		@Override
		public boolean isCastlingMove() {
			return true;
		}
		
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for(final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			for(final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setPiece(new Rook(this.castleRookDestination,this.castleRook.getPieceColor()));
			builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getColor());
			return builder.build();
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime*result +this.castleRook.hashCode();
			result = prime*result + this.castleRookDestination;
			return result;
		}
		
		@Override
		public boolean equals(final Object other) {
			if(this == other) {
				return true;
			}
			if(!(other instanceof CastleMove)) {
				return false;
			}
			
			final CastleMove otherCM = (CastleMove) other;
			return super.equals(otherCM) && this.castleRook.equals(otherCM.getCastleRook());
		}
		
	}
	
	public static final class KingSideCastleMove extends CastleMove{

		public KingSideCastleMove(final Board board, final Piece piece, final int DestCoord, final int castleRookDestination, final int castleRookStart, final Rook castleRook) {
			super(board, piece, DestCoord, castleRookDestination, castleRookStart, castleRook);
		}
		
		@Override
		public String toString() {
			return "O-O";
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof KingSideCastleMove && super.equals(other);
			
		}
		
	}
	
	public static final class QueenSideCastleMove extends CastleMove{
		public QueenSideCastleMove(final Board board, final Piece piece, final int DestCoord, final int castleRookDestination, final int castleRookStart, final Rook castleRook) {
			super(board, piece, DestCoord, castleRookDestination, castleRookStart, castleRook);
		}
		
		@Override
		public String toString() {
			return "O-O-O";
		}
		
		@Override
		public boolean equals(final Object other) {
			return this == other || other instanceof QueenSideCastleMove && super.equals(other);
			
		}
		
	}
	
	public static final class NullMove extends Move{

		public NullMove() {
			super(null, 65);
		}
		
		@Override
		public Board execute() {
			
			throw new RuntimeException("cannot execute the null move");
		}
		
		@Override
		public int getCurrentCoordinate() {
			return -1;
		}
	}
	
	public static class MoveFactory {
		private MoveFactory() {
			throw new RuntimeException("Not instantiable");
		}
		public static Move createMove(final Board board, final int currentCoord, final int destinationCoord) {
			
			for(final Move move: board.getAllLegalMoves()) {
				if(move.getCurrentCoordinate() == currentCoord && move.getDestinationCoordinate() == destinationCoord ) {
					return move;
				}
			}
			return NULL_MOVE;
		}
	}
}
