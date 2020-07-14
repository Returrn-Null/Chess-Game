package Pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.BoardUtil;
import Board.Move;
import Pieces.Piece.PieceType;
import Player.Color;

public class Pawn extends Piece{

	private final static int[] POSSIBLE_MOVES = {8, 16, 7, 9};
	public Pawn(int piecePosition, Color pieceCol) {
		super(piecePosition, pieceCol, PieceType.PAWN, true);
	}
	
	public Pawn(int piecePosition, Color pieceCol, final boolean isFirstMove) {
		super(piecePosition, pieceCol, PieceType.PAWN, isFirstMove);
	}

	@Override
	public List<Move> findLegalMoves(final Board board) {
		
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final int currOffset: POSSIBLE_MOVES) {
			final int destination = this.piecePos + (currOffset*this.getPieceColor().getDirection());
			if(!BoardUtil.isValidCoordinate(destination)) {
				continue;
			}
			
			if(currOffset == 8 && !board.getTile(destination).isOccupied()) {
				
				if(this.pieceColor.isPawnPromotionSquare(destination)) {
					legalMoves.add(new Move.PawnPromotion(new Move.PawnMove(board,this,destination)));
				}
				else {
					legalMoves.add(new Move.PawnMove(board,this, destination));
				}		
			}
			else if(currOffset == 16 && this.isFirstMove() && ((BoardUtil.SEVENTH_RANK[this.piecePos] && this.getPieceColor().isBlack()) 
					|| (BoardUtil.SECOND_RANK[this.piecePos] && this.getPieceColor().isWhite()))) {
				final int behindDestination = this.piecePos+(this.getPieceColor().getDirection()*8);
				
				if(!board.getTile(behindDestination).isOccupied() && !board.getTile(destination).isOccupied()) {
					legalMoves.add(new Move.PawnJump(board,this, destination));
				}
			} else if( currOffset == 7 && !((BoardUtil.EIGTH_COL[this.piecePos] && this.pieceColor.isWhite())
					|| (BoardUtil.FIRST_COLUMN[this.piecePos] && this.pieceColor.isBlack()))) {
				if(board.getTile(destination).isOccupied()) {
					final Piece pieceAttacked = board.getTile(destination).getPiece();
					if(this.pieceColor != pieceAttacked.getPieceColor()) {
						if(this.pieceColor.isPawnPromotionSquare(destination)) {
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board,this,destination,pieceAttacked)));
						}else {
						legalMoves.add(new Move.PawnAttackMove(board,this, destination, pieceAttacked));
						}
					}
				}else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePos+(this.pieceColor.getOppositeDirection()))) {
						final Piece pieceOnTile = board.getEnPassantPawn();
						if(this.pieceColor != pieceOnTile.getPieceColor()) {
							legalMoves.add(new Move.PawnEnPassantAttack(board,this,destination,pieceOnTile));
						}
					}	
				}	
			}else if(currOffset == 9 && !((BoardUtil.EIGTH_COL[this.piecePos] && this.pieceColor.isBlack())
					|| (BoardUtil.FIRST_COLUMN[this.piecePos] && this.pieceColor.isWhite()))) {
				
				if(board.getTile(destination).isOccupied()) {
					final Piece pieceAttacked = board.getTile(destination).getPiece();
					if(this.pieceColor != pieceAttacked.getPieceColor()) {
						if(this.pieceColor.isPawnPromotionSquare(destination)) {
							legalMoves.add(new Move.PawnPromotion(new Move.PawnAttackMove(board,this,destination,pieceAttacked)));
						}else {
						legalMoves.add(new Move.PawnAttackMove(board,this, destination, pieceAttacked));
						}
					}
				}else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (this.piecePos-(this.pieceColor.getOppositeDirection()))) {
						final Piece pieceOnTile = board.getEnPassantPawn();
						if(this.pieceColor != pieceOnTile.getPieceColor()) {
							legalMoves.add(new Move.PawnEnPassantAttack(board,this,destination,pieceOnTile));
						}
					}	
				}	
				
			}
		}
		
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}
	
	@Override
	public Pawn movePiece(final Move move) {
		
		return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceColor());
	}
	
	public Piece getPromotedPiece() {
		return new Queen(this.piecePos, this.pieceColor,false);
	}

}
