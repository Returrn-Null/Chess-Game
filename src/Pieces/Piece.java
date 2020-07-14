package Pieces;

import java.util.List;
import Board.Move;
import Player.Color;
import Board.Board;

public abstract class Piece {

	protected final PieceType pieceType;
	protected final int piecePos;
	protected final Color pieceColor;
	protected final boolean isFirstMove;
	private final int hashCode;
	
	
	Piece(final int  piecePosition, final Color pieceCol, final PieceType pieceType, final boolean isFirstMove){
		this.pieceColor = pieceCol;
		this.piecePos = piecePosition;
		this.isFirstMove = isFirstMove;
		this.pieceType = pieceType;
		this.hashCode = computeHashCode();
	}
	
	public int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31* result + pieceColor.hashCode();
		result = 31* result + piecePos;
		result = 31* result + (isFirstMove ?1:0);
		return result;
	}
	
	public int getPiecePosition() {
		return piecePos;
	}
	
	public Color getPieceColor() {
		return this.pieceColor;
	}
	
	public boolean isFirstMove() {
		return isFirstMove;
	}
	
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	public int getPieceValue() {
		return this.pieceType.getPieceValue();
	}
	
	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof Piece)) {
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return piecePos == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
			   pieceColor == otherPiece.getPieceColor() && isFirstMove == otherPiece.isFirstMove();
	}
	
	@Override
	public int hashCode() {
		return this.hashCode;
	}
	
	public abstract Piece movePiece(Move move);
	public abstract List<Move> findLegalMoves(final Board board);
	
	public enum PieceType{
		
		PAWN("P", 100) {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		KNIGHT("N", 300) {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		BISHOP("B", 300) {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		ROOK("R", 500) {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return true;
			}
		},
		QUEEN("Q", 900) {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		KING("K", 10000) {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		private String pieceName;
		private int pieceValue; //heuristic for the AI
		
		
		PieceType(final String pieceName, final int pieceValue){
			this.pieceName = pieceName;
			this.pieceValue = pieceValue;
		}
		
		
		@Override
		public String toString() {
			return this.pieceName;
		}
		
		public abstract boolean isKing();
		
		
		public int getPieceValue() {
			return this.pieceValue;
		}
		public abstract boolean isRook(); 
	}
}
