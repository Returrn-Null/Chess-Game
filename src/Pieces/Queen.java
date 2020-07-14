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

public class Queen extends Piece{

	private final static int[] POSSIBLE_MOVES = {-9, -8, -7, -1 , 1, 7, 8, 9};
	
	public Queen(final int piecePosition,final Color pieceCol) {
		super(piecePosition, pieceCol, PieceType.QUEEN, true);
	}
	
	public Queen(final int piecePosition, final Color pieceCol, final boolean isFirstMove) {
		super(piecePosition, pieceCol, PieceType.QUEEN, isFirstMove);
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
		return PieceType.QUEEN.toString();
	}
	
	@Override
	public Queen movePiece(final Move move) {
		
		return new Queen(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor());
	}
	
	private static boolean isFirstCol(final int currPos, final int posOffset) {
		return BoardUtil.FIRST_COLUMN[currPos] && (posOffset == -9 || posOffset == 7 || posOffset == -1);
	}
	private static boolean isEightCol(final int currPos, final int posOffset) {
		return BoardUtil.EIGTH_COL[currPos] && (posOffset == -7 || posOffset == 9 || posOffset == 1);
	}
	
}
