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

public class Rook extends Piece{

	private final static int[] POSSIBLE_MOVES = {-8,-1,1,8};
	public Rook(int piecePosition, Color pieceCol) {
		super(piecePosition, pieceCol, PieceType.ROOK, true);
	}
	
	public Rook(final Color pieceColor, final int piecePosition, final boolean isFirstMove) {
		super(piecePosition, pieceColor, PieceType.ROOK, isFirstMove);
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
		return PieceType.ROOK.toString();
	}
	
	@Override
	public Rook movePiece(final Move move) {
		
		return new Rook(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor());
	}
	
	private static boolean isFirstCol(final int currPos, final int posOffset) {
		return BoardUtil.FIRST_COLUMN[currPos] && (posOffset == -1);
	}
	private static boolean isEightCol(final int currPos, final int posOffset) {
		return BoardUtil.EIGTH_COL[currPos] && (posOffset == 1);
	}

}
