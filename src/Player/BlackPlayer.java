package Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Board.Board;
import Board.Move;
import Board.Tile;
import Pieces.Piece;
import Pieces.Rook;

public class BlackPlayer extends Player {

	public BlackPlayer(final Board board,final Collection<Move> whiteStdLegalMoves, final Collection<Move> blackStdLegalMoves) {
		
		super(board,blackStdLegalMoves, whiteStdLegalMoves);
	}

	@Override
	public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
		
		final List<Move> kingCastles = new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			//White king side
			if(!this.board.getTile(5).isOccupied() && !this.board.getTile(6).isOccupied()) {
				final Tile rookTile = this.board.getTile(7);
				
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(5, opponentsLegals).isEmpty() &&
					   Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty() &&
					   rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing,6,5,rookTile.getTileCoordinate(),(Rook)rookTile.getPiece()));
					}
				}
			}
			//queen side
			if(!this.board.getTile(1).isOccupied() && !this.board.getTile(2).isOccupied() && !this.board.getTile(3).isOccupied()) {
				
				final Tile rookTile = this.board.getTile(0);
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(2, opponentsLegals).isEmpty() &&
							   Player.calculateAttacksOnTile(3, opponentsLegals).isEmpty() &&
							   rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2, 3, rookTile.getTileCoordinate(), (Rook) rookTile.getPiece()));
							}					
				}
				
			}
			
		}

		return Collections.unmodifiableList(kingCastles);
	}
	
	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getBlackPieces();
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();
	}

}
