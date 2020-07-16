package Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Pieces.*;
import Player.BlackPlayer;
import Player.Color;
import Player.Player;
import Player.WhitePlayer;

public class Board {
	
	private final List<Tile> board;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	private final Player currentPlayer;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	
	private final Pawn enPassantPawn;
	
	private Board(final Builder builder) {
		this.board = createGameBoard(builder);
		this.whitePieces = findActivePieces(this.board,Color.WHITE);
		this.blackPieces = findActivePieces(this.board, Color.BLACK);
		this.enPassantPawn = builder.enPassantPawn;
		final Collection<Move> whiteStdLegalMoves = findLegalMoves(this.whitePieces);
		final Collection<Move> blackStdLegalMoves = findLegalMoves(this.blackPieces);
		this.whitePlayer = new WhitePlayer(this, whiteStdLegalMoves, blackStdLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteStdLegalMoves, blackStdLegalMoves);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
		
		
		
		
	}
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i<BoardUtil.NUM_TILES; i++) {
			final String tileText = this.board.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if((i+1)%8 == 0) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	public Collection<Piece> getBlackPieces(){
		return this.blackPieces;
	}
	
	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	public Pawn getEnPassantPawn() {
		return this.enPassantPawn;
	}
	
	public Collection<Piece> getWhitePieces(){
		return this.whitePieces;
	}
	
	private Collection<Move> findLegalMoves(final Collection<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for(final Piece piece:pieces) {
			legalMoves.addAll(piece.findLegalMoves(this));
		}
		return (legalMoves);
	}

	private Collection<Piece> findActivePieces(final List<Tile> board, final Color color) {
		
		final List<Piece> activePieces = new ArrayList<>();
		
		for(final Tile tile:board) {
			if(tile.isOccupied()) {
				final Piece piece = tile.getPiece();
				if(piece.getPieceColor() == color) {
					activePieces.add(piece);
				}
			}
		}
		return Collections.unmodifiableList(activePieces);
	}
	public Tile getTile(final int coordinate) {
		return board.get(coordinate);
	}
	
	private static List<Tile> createGameBoard(final Builder builder){
		final Tile[] tiles = new Tile[BoardUtil.NUM_TILES];
		for(int i = 0; i<BoardUtil.NUM_TILES; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		return Collections.unmodifiableList(Arrays.asList(tiles));
	}
	
	public static Board createStandardBoard() {
		final Builder builder = new Builder();
		//Black
		builder.setPiece(new Rook(0, Color.BLACK));
		builder.setPiece(new Knight(1,Color.BLACK));
		builder.setPiece(new Bishop(2,Color.BLACK));
		builder.setPiece(new Queen(3,Color.BLACK));
		builder.setPiece(new King(4,Color.BLACK, true, true));
		builder.setPiece(new Bishop(5,Color.BLACK));
		builder.setPiece(new Knight(6,Color.BLACK));
		builder.setPiece(new Rook(7,Color.BLACK));
		
		builder.setPiece(new Pawn(8,Color.BLACK));
		builder.setPiece(new Pawn(9,Color.BLACK));
		builder.setPiece(new Pawn(10,Color.BLACK));
		builder.setPiece(new Pawn(11,Color.BLACK));
		builder.setPiece(new Pawn(12,Color.BLACK));
		builder.setPiece(new Pawn(13,Color.BLACK));
		builder.setPiece(new Pawn(14,Color.BLACK));
		builder.setPiece(new Pawn(15,Color.BLACK));
		//White
		builder.setPiece(new Pawn(48,Color.WHITE));
		builder.setPiece(new Pawn(49,Color.WHITE));
		builder.setPiece(new Pawn(50,Color.WHITE));
		builder.setPiece(new Pawn(51,Color.WHITE));
		builder.setPiece(new Pawn(52,Color.WHITE));
		builder.setPiece(new Pawn(53,Color.WHITE));
		builder.setPiece(new Pawn(54,Color.WHITE));
		builder.setPiece(new Pawn(55,Color.WHITE));
		builder.setPiece(new Rook(56,Color.WHITE));
		builder.setPiece(new Knight(57,Color.WHITE));
		builder.setPiece(new Bishop(58,Color.WHITE));
		builder.setPiece(new Queen(59,Color.WHITE));
		builder.setPiece(new King(60,Color.WHITE, true , true));
		builder.setPiece(new Bishop(61,Color.WHITE));
		builder.setPiece(new Knight(62,Color.WHITE));
		builder.setPiece(new Rook(63,Color.WHITE));
		
		builder.setMoveMaker(Color.WHITE);
		return builder.build();
	}
	
	public Player whitePlayer() {
		return this.whitePlayer;
	}
	
	public Player blackPlayer() {
		return this.blackPlayer;
	}
	
	public static class Builder{
		
		Map<Integer, Piece> boardConfig;
		Color nextMoveMaker;
		Pawn enPassantPawn;
		
		public Builder() {
			
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		public Builder setMoveMaker(final Color color) {
			this.nextMoveMaker = color;
			return this;
		}
		public Board build() {
			return new Board(this);
		}

		public void setEnPassantPawn(Pawn movedPawn) {
			this.enPassantPawn = movedPawn;
		}
	}

	public Collection<Move> getAllLegalMoves() {
		Collection<Move> allLegalMoves = new ArrayList<>();
		
		for(Move move : this.whitePlayer.getLegalMoves()) {
			allLegalMoves.add(move);
		}
		//Collection<Move> allLegalMoves = this.whitePlayer.getLegalMoves();
		for(Move move : this.blackPlayer.getLegalMoves()) {
			allLegalMoves.add(move);
		}
		return Collections.unmodifiableCollection(allLegalMoves);
	}
	
}
