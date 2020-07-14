package Board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardUtil {
	public static final boolean[] SEVENTH_RANK = initRow(8);
	public static final boolean[] SECOND_RANK = initRow(48);
	public static final boolean[] FIRST_COLUMN = initCol(0);
	public static final boolean[] SECOND_COL = initCol(1);
	public static final boolean[] SEVENTH_COL = initCol(6);
	public static final boolean[] EIGTH_COL = initCol(7);
	public static final int NUM_TILES = 64;
	
	public static final boolean[] EIGHTH_RANK = initRow(0);
	public static final boolean[] SIXTH_RANK = initRow(16);
	public static final boolean[] FIFTH_RANK = initRow(24);
	public static final boolean[] FOURTH_RANK = initRow(32);
	public static final boolean[] THIRD_RANK = initRow(40);
	public static final boolean[] FIRST_RANK = initRow(56);
	
	public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
	public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
	
	
	private BoardUtil() {
		throw new RuntimeException("Cannot be instantiated");
	}
	
	private static boolean[] initRow(int rowNum) {
		
		final boolean[] row = new boolean[NUM_TILES];
		do {
			row[rowNum] = true;
			rowNum++;
		}while(rowNum %8 !=0);
		
		return row;
	}
	
	private static boolean[] initCol(int columnNum) {
		
		final boolean[] column = new boolean[64];
		do {
			column[columnNum] = true;
			columnNum +=8;
		}while(columnNum<NUM_TILES);
			
		return column;
	}

	public static boolean isValidCoordinate(final int coord) {
		return coord>= 0 && coord<NUM_TILES;
	}
	
	private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }

	public static int getCoordinateAtPosition(final String position) {
		return POSITION_TO_COORDINATE.get(position);
	}
	
	public static String getPositionAtCoord(final int coordinate) {
		return ALGEBRAIC_NOTATION.get(coordinate);
	}
}
