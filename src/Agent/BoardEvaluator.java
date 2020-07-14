package Agent;

import Board.*;

public interface BoardEvaluator {

	default int evaluate(Board board, int depth) {
		return 0;
	}
}
