package Pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Board.*;
import Player.Color;

public class Bishop extends Piece {

	private final static int[] POSSIBLE_MOVES = {-9,-7,7,9};
	
	public Bishop(int piecePosition, Color pieceCol) {
		super(piecePosition, pieceCol, PieceType.BISHOP, true);
	}
	
	public Bishop(int piecePosition, Color pieceCol, final boolean isFirstMove) {
		super(piecePosition, pieceCol, PieceType.BISHOP, isFirstMove);
	}

	@Override
	public List<Move> findLegalMoves(final Board board) {

		final List<Move> legalMoves = new ArrayList<>();
		for(final int positionoffset : POSSIBLE_MOVES) {
			int destination = this.piecePos;
			while(BoardUtil.isValidCoordinate(destination)) {
				
				if(isFirstCol(destination, positionoffset) || isEightCol(destination, positionoffset)) {
					break;
				}
				destination += positionoffset;
				if(BoardUtil.isValidCoordinate(destination)) {
					
					
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
						break;
					}
				}
			}
		}
		return Collections.unmodifiableList(legalMoves);
	}

	@Override
	public String toString() {
		return PieceType.BISHOP.toString();
	}
	
	private static boolean isFirstCol(final int currPos, final int posOffset) {
		return BoardUtil.FIRST_COLUMN[currPos] && (posOffset == -9 || posOffset == 7);
	}
	private static boolean isEightCol(final int currPos, final int posOffset) {
		return BoardUtil.EIGTH_COL[currPos] && (posOffset == -7 || posOffset == 9);
	}

	@Override
	public Bishop movePiece(final Move move) {
		
		return new Bishop(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor());
	}
}
