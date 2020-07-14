package Player;

import Board.BoardUtil;

public enum Color {

	WHITE{
		@Override
		public int getDirection() {
			return -1;
		}

		@Override
		public boolean isWhite() {
			return true;
		}

		@Override
		public boolean isBlack() {
			return false;
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
		
			return whitePlayer;
		}

		@Override
		public int getOppositeDirection() {
			return 1;
		}

		@Override
		public boolean isPawnPromotionSquare(int position) {
			return BoardUtil.EIGHTH_RANK[position];
		}
	},
	BLACK {
		@Override
		public int getDirection() {
			return 1;
		}

		@Override
		public boolean isWhite() {
			return false;
		}

		@Override
		public boolean isBlack() {
			return true;
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer,final BlackPlayer blackPlayer) {
			return blackPlayer;
		}

		@Override
		public int getOppositeDirection() {
			return -1;
		}

		@Override
		public boolean isPawnPromotionSquare(int position) {
			
			return BoardUtil.FIRST_RANK[position];
		}
	};
	
	public abstract int getDirection();
	public abstract boolean isWhite();
	public abstract boolean isBlack();
	public abstract boolean isPawnPromotionSquare(int position);
	public abstract int getOppositeDirection();
	public  abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
