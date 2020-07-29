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

public class WhitePlayer extends Player {

	public WhitePlayer(final Board board, final Collection<Move> whiteStdLegalMoves, final Collection<Move> blackStdLegalMoves) {
		
		super(board, whiteStdLegalMoves, blackStdLegalMoves);
		
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Color getColor() {
		return Color.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.blackPlayer();
	}

	@Override
	public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,final Collection<Move> opponentsLegals) {
		
		final List<Move> kingCastles = new ArrayList<>();
		
		if(this.playerKing.isFirstMove() && !this.isInCheck()) {
			//White king side
			if(!this.board.getTile(61).isOccupied() && !this.board.getTile(62).isOccupied()) {
				final Tile rookTile = this.board.getTile(63);
				final Tile kingTile = this.board.getTile(60);
				
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove() &&
				   kingTile.isOccupied() && kingTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
					   Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() &&
					   Player.calculateAttacksOnTile(60, opponentsLegals).isEmpty() &&
					   rookTile.getPiece().getPieceType().isRook()) {
						
						kingCastles.add(new Move.KingSideCastleMove(this.board,this.playerKing,62,61,rookTile.getTileCoordinate(),(Rook)rookTile.getPiece()));
					}
				}
			}
			//queen side
			if(!this.board.getTile(59).isOccupied() && !this.board.getTile(58).isOccupied() && !this.board.getTile(57).isOccupied()) {
				
				final Tile rookTile = this.board.getTile(56);
				final Tile kingTile = this.board.getTile(56);
				
				if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove() &&
				   kingTile.isOccupied() && kingTile.getPiece().isFirstMove() &&
				   Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() &&
				   Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty() &&
				   Player.calculateAttacksOnTile(60, opponentsLegals).isEmpty() &&
				   rookTile.getPiece().getPieceType().isRook()) {
					kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58, 59, rookTile.getTileCoordinate(), (Rook) rookTile.getPiece()));//TODO add castle move
				}
				
			}
			
		}
		
		return Collections.unmodifiableList(kingCastles);
	}

}
