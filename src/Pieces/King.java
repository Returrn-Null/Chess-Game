package Pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.BoardUtil;
import Board.Move;
import Board.Tile;
import Pieces.Piece.PieceType;
import Player.Color;

public class King extends Piece {
	
	private final boolean kingSideCastleCap;
	private final boolean QueenSideCastleCap;
	private final boolean isCastled;
	private final static int[] POSSIBLE_MOVES = {-9, -8, -7, -1, 1, 7, 8, 9};
	public King(final int piecePosition, final Color pieceCol, final boolean kingSideCastleCap, final boolean QueenSideCastleCap ) {
		super(piecePosition, pieceCol, PieceType.KING, true);
		this.kingSideCastleCap = kingSideCastleCap;
		this.QueenSideCastleCap = QueenSideCastleCap;
		this.isCastled = false;
	}
	
	public King(final int piecePosition, final Color pieceCol, final boolean isFirstMove, final boolean iscastled, final boolean kingSideCastleCap, final boolean QueenSideCastleCap ) {
		super(piecePosition, pieceCol, PieceType.KING, isFirstMove);
		this.isCastled = iscastled;
		this.QueenSideCastleCap = QueenSideCastleCap;
		this.kingSideCastleCap = kingSideCastleCap;
	}
	
	public boolean isCastled() {
		return this.isCastled;
	}
	
	public boolean iskingSideCastleCap() {
		return this.kingSideCastleCap;
	}
	
	public boolean isQueenSideCastleCap() {
		return this.QueenSideCastleCap;
	}
	
	public boolean isKingSideCastleCapable() {
		return false;
	}
	
	public boolean isQueenSideCastleCapable() {
		return false;
	}
	

	@Override
	public List<Move> findLegalMoves(final Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		int destination;
		for(final int currOffset: POSSIBLE_MOVES) {
			
			destination = this.piecePos+currOffset;
			if(BoardUtil.isValidCoordinate(destination)) {
				if(firColumnEdge(this.piecePos, currOffset)|| isEigthCol(this.piecePos,currOffset)) {
					continue;
				}
				final Tile destinationTile = board.getTile(destination);
				if(!destinationTile.isOccupied()) {
					legalMoves.add(new Move.MajorMove(board,this, destination));
				}
				else {
					final Piece tilePiece = destinationTile.getPiece();
					final Color pieceColor = tilePiece.getPieceColor();//piece getting eaten
					
					if(this.pieceColor != pieceColor) {
						legalMoves.add(new Move.MajorAttackMove(board,this,destination,tilePiece));
					}
				}
			}
		}
		return legalMoves;
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
	
	@Override
	public King movePiece(final Move move) {
		
		return new King(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor(),false, 
						move.isCastlingMove(), false, false);
	}
	
	private static boolean firColumnEdge(final int currPos, final int offsetMove) {
		
		return BoardUtil.FIRST_COLUMN[currPos] && ((offsetMove == -9) || (offsetMove == -1) || 
		(offsetMove == 7));
		
	}
	
	private static boolean isEigthCol(final int currPos, final int offsetMove) {
		
		return BoardUtil.EIGTH_COL[currPos] && ((offsetMove == 9) || (offsetMove == -7) || 
		(offsetMove == 1));
	}
}
