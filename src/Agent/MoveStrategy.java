package Agent;

import Board.*;

public interface MoveStrategy {
	
	default Move execute(Board board) {
		return null;
	}
}
