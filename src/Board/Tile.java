package Board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Pieces.Piece;

public abstract class Tile {

	protected final int tileCoordinate;//protected to be accessed by subclasses
	private static final Map<Integer, EmptyTile> EMPTY_TILES = createALlEmptyTiles();
	
	public static Tile createTile(final int tilecoord, final Piece piece) {
		return piece != null ? new OccupiedTile(tilecoord,  piece) : EMPTY_TILES.get(tilecoord);
	}
	
	private Tile(final int tileCoord){
		this.tileCoordinate = tileCoord;
	}
	
	private static Map<Integer, EmptyTile> createALlEmptyTiles() {
		
		final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();
		for(int i = 0; i<BoardUtil.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		return Collections.unmodifiableMap(emptyTileMap);//TODO: look if I can make this immutable (2, 15:00)
	}

	public abstract boolean isOccupied();
	
	public abstract Piece getPiece();
	
	public int getTileCoordinate() {
		return this.tileCoordinate;
	}
	
	public static final class EmptyTile extends Tile{
		
		
		@Override
		public String toString() {
			return "-";	
		}
		
		private EmptyTile(int coord){
			super(coord);
		}
		
		@Override
		public boolean isOccupied() {
			return false;
		}
		
		@Override
		public Piece getPiece() {
			return null; //nothing to return on an empty tile
		}
	}
	
	public static final class OccupiedTile extends Tile{
		
		private final Piece pieceOnTile;
		
		
		@Override
		public String toString() {
			return getPiece().getPieceColor().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
		}
		
		private OccupiedTile(int coord, Piece piece){
			super(coord);
			this.pieceOnTile = piece;
		}
		
		@Override
		public boolean isOccupied() {
			return true;
		}
		
		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}
		
	}
	
}
