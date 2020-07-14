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

public class Knight extends Piece {
	
	private final int[] POSSIBLE_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17};

	public Knight(final int piecePosition, final Color pieceCol) {
		super(piecePosition, pieceCol, PieceType.KNIGHT, true);
	}
	
	public Knight(final int piecePosition, final Color pieceCol, final boolean isFirstMove) {
		super(piecePosition, pieceCol, PieceType.KNIGHT, isFirstMove);
	}
	
	@Override
	public List<Move> findLegalMoves(final Board board){
		
		List<Move> legalMoves = new ArrayList<>();
		int destination;
		for(final int currMove : POSSIBLE_MOVES) {// currmove = offset
			
			destination = this.piecePos+currMove;//applying offset to curr pos
			if(BoardUtil.isValidCoordinate(destination)) {
				
				if(firColumnEdge(this.piecePos, currMove) || isSeventhCol(this.piecePos, currMove)
				|| isEigthCol(this.piecePos, currMove) || isSecondCol(this.piecePos, currMove)) {
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
		
		
		
		return Collections.unmodifiableList(legalMoves);
	}
	
	
	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}
	
	@Override
	public Knight movePiece(final Move move) {
		
		return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor());
	}
	
	private static boolean firColumnEdge(final int currPos, final int offsetMove) {
		
		return BoardUtil.FIRST_COLUMN[currPos] && ((offsetMove == -17) || (offsetMove == -10) || 
		(offsetMove == 6) || (offsetMove == 15));
		
	}
	
	private static boolean isSecondCol(final int currPos, final int offsetMove ) {
		
		return BoardUtil.SECOND_COL[currPos] && ((offsetMove == -10) || (offsetMove == 6));
	}
	
	private static boolean isSeventhCol(final int currPos, final int offsetMove) {
		
		return BoardUtil.SEVENTH_COL[currPos] && ((offsetMove == -6) || (offsetMove == 10));
	}
	private static boolean isEigthCol(final int currPos, final int offsetMove) {
		
		return BoardUtil.EIGTH_COL[currPos] && ((offsetMove == -15) || (offsetMove == -6) || 
		(offsetMove == 10) || (offsetMove == 17));
	}
}
