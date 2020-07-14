package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import Board.*;
import Board.Move.MoveFactory;
import Pieces.Piece;
import Player.MoveTransition;

public class Table {
	
	private Color lightTileColor = Color.decode("#FFFACD");
    private Color darkTileColor = Color.decode("#593E1A");

    private final GameLogPanel gameLog;
    private final TakenPiecesPanel takenPiecePanel;
    private final MoveLog moveLog;
    
	private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
	private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
	private static String defaultPieceImagePath = "simpleArt/medium/";
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;
	
	private boolean highlightLegalMoves;
	
	public Table() {
		this.chessBoard =  Board.createStandardBoard();
		this.takenPiecePanel = new TakenPiecesPanel();
		this.gameLog = new GameLogPanel();
		this.gameFrame = new JFrame("JChess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = createTableMenuBar();	
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);	
		this.boardPanel = new BoardPanel();
		this.moveLog = new MoveLog();
		this.boardDirection = BoardDirection.NORMAL;
		this.highlightLegalMoves = true;
		this.gameFrame.add(this.takenPiecePanel,BorderLayout.WEST);
		this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
		this.gameFrame.add(this.gameLog,BorderLayout.EAST);
		this.gameFrame.setVisible(true);
	}

	private JMenuBar createTableMenuBar() {
		final JMenuBar tableMenuBar = new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		return tableMenuBar;
	}

	private JMenu createFileMenu() {
		
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Open the PGN");
			}
		});
		fileMenu.add(openPGN);
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}
	
	private JMenu createPreferencesMenu() {
		final JMenu preferencesMenu = new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");
		flipBoardMenuItem.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				boardDirection = boardDirection.opposite();
				boardPanel.drawBoard(chessBoard);
			}
			
		});
		preferencesMenu.add(flipBoardMenuItem);
		
		preferencesMenu.addSeparator();
		final JCheckBoxMenuItem legalMoveHighlight = new JCheckBoxMenuItem("Highlight legal moves", true);
		legalMoveHighlight.addActionListener(new ActionListener() {
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				highlightLegalMoves = legalMoveHighlight.isSelected();
			}
		});
		preferencesMenu.add(legalMoveHighlight);
		
		return preferencesMenu;
	}
	
	
	public static class MoveLog{
		
		private final List<Move> moves;
		MoveLog(){
			this.moves = new ArrayList<>();
		}
		
		public List<Move> getMoves(){
			return this.moves;
		}
		
		public void addMove(final Move move) {
			this.moves.add(move);
		}	
		
		public int size() {
			return this.moves.size();
		}
		
		public void clear() {
			this.moves.clear();
		}
		
		public Move removeMove(final int index) {
			return this.moves.remove(index);
		}
		
		public boolean removeMove(final Move move) {
			return this.moves.remove(move);
		}
	}
	
	private class BoardPanel extends JPanel {
		
		
		final List<TilePanel> boardTiles;
		
		BoardPanel(){
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			for(int i = 0; i<BoardUtil.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this,i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
		
		public void drawBoard(final Board board) {
			removeAll();
			for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
		}
	}
	
	private class TilePanel extends JPanel{
		
		private final int tileId;
		
		TilePanel(final BoardPanel boardPanel, 
				  final int tileId){
			
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);
			
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					
					if(SwingUtilities.isRightMouseButton(e)) {
						//cancel selected
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
						
					}
					else if(SwingUtilities.isLeftMouseButton(e)) {
						
						if(sourceTile == null) {
							sourceTile = chessBoard.getTile(tileId);
							humanMovedPiece = sourceTile.getPiece();
							if(humanMovedPiece == null) {
								sourceTile = null;
								System.out.println("yes");
							}
						} 
						else {
							destinationTile = chessBoard.getTile(tileId);
							final Move move = MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
							if(transition.getMoveStatus().isDone()) {
								chessBoard = transition.getTransitionBoard();
								moveLog.addMove(move);
							}
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
						}
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								gameLog.redo(chessBoard, moveLog);
								takenPiecePanel.redo(moveLog);
								boardPanel.drawBoard(chessBoard);
								
							}
							
						});
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					
				}
				
			});
			
			validate();
		}

		public void drawTile(final Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			validate();
			highlightLegals(board);
			repaint();
		}
		
		private void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if(board.getTile(this.tileId).isOccupied()) {
				try {
					final BufferedImage image = ImageIO.read(new File(defaultPieceImagePath  + board.getTile(this.tileId).getPiece().getPieceColor().toString().substring(0,1)+board.getTile(this.tileId).getPiece().toString()+ ".gif"));
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					System.out.println("could not load the image");
				}
			}
		}
		
		private void assignTileColor() {
			if(BoardUtil.EIGHTH_RANK[this.tileId] ||
			   BoardUtil.SIXTH_RANK[this.tileId] ||
			   BoardUtil.FOURTH_RANK[this.tileId] ||
			   BoardUtil.SECOND_RANK[this.tileId]) {
				
				setBackground(this.tileId%2 == 0 ? lightTileColor : darkTileColor);
			}
			else if(BoardUtil.SEVENTH_RANK[this.tileId] ||
					BoardUtil.FIFTH_RANK[this.tileId] ||
					BoardUtil.THIRD_RANK[this.tileId] ||
					BoardUtil.FIRST_RANK[this.tileId]) {
				
				setBackground(this.tileId%2 != 0 ? lightTileColor : darkTileColor);
			}
			
		}
		
		private void highlightLegals(final Board board) {
			if(highlightLegalMoves) {// if user wants to see them
				for(final Move move : pieceLegalMoves(board)) {
					if(move.getDestinationCoordinate() == this.tileId) {
						try {
							add(new JLabel( new ImageIcon(ImageIO.read(new File("simpleArt/Legal/green_dot.png")))));
						} catch(Exception e) {
							System.out.println("Could not load green dot");
						}
					}
				}
			}
		}
		
		private Collection<Move> pieceLegalMoves(final Board board){
			if(humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.getCurrentPlayer().getColor()) {
				if(humanMovedPiece.getPieceType().isKing()) {
					Collection<Move> moves = humanMovedPiece.findLegalMoves(board);
					for(final Move move : board.getCurrentPlayer().calculateKingCastles(board.getCurrentPlayer().getLegalMoves(),board.getCurrentPlayer().getOpponent().getLegalMoves())) {
						moves.add(move);
					}
					return moves;
				}else {
					return humanMovedPiece.findLegalMoves(board);
				}	
			}
			return Collections.emptyList();
		}
		
	}
	
	public enum BoardDirection{
		
		NORMAL{

			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				//Collections.reverse(boardTiles);
				return boardTiles;
			}

			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}
			
		},
		FLIPPED{

			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				// Maybe mistake here in reverse
				return reverse(boardTiles);
			}

			@Override
			BoardDirection opposite() {
				return NORMAL;
			}
			
		};
		
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection opposite();
		
		public List<TilePanel> reverse(final List<TilePanel> boardTiles){
			
			List<TilePanel> copy = new ArrayList<>();
			for(TilePanel tilePanel:boardTiles) {
				copy.add(tilePanel);
			}
			Collections.reverse(copy);
			return copy;
		}
	}
	
	
	
}
