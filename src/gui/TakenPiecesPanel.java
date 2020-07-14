package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import Board.Move;
import Pieces.Piece;

public class TakenPiecesPanel extends JPanel{

	private final JPanel northPanel;
	private final JPanel southPanel;
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
	private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40,80);

	public TakenPiecesPanel() {
		super(new BorderLayout());
		setBackground(PANEL_COLOR);
		setBorder(PANEL_BORDER);
		this.northPanel = new JPanel(new GridLayout(8,2));
		this.southPanel = new JPanel(new GridLayout(8,2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.southPanel.setBackground(PANEL_COLOR);
		add(this.northPanel,BorderLayout.NORTH);
		add(this.southPanel,BorderLayout.SOUTH);
		setPreferredSize(TAKEN_PIECES_DIMENSION);
	}
	
	
	public void redo(final Table.MoveLog moveLog) {
		
		this.southPanel.removeAll();
		this.northPanel.removeAll();
		
		final List<Piece> whiteTakenPieces = new ArrayList<>();
		final List<Piece> blackTakenPieces = new ArrayList<>();
		
		for(final Move move: moveLog.getMoves()) {
			
			if(move.isAttack()) {
				final Piece takenPiece = move.getAttackedPiece();
				if(takenPiece.getPieceColor().isWhite()) {
					whiteTakenPieces.add(takenPiece);
				}
				else if(takenPiece.getPieceColor().isBlack()) {
					blackTakenPieces.add(takenPiece);
				}
				else {
					throw new RuntimeException("The piece taken is neither black or white");
				}
			}
		}
		
		Collections.sort(whiteTakenPieces, new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				if(o1.getPieceValue()<o2.getPieceValue()) {
					return -1;
				}else if(o1.getPieceValue() == o2.getPieceValue()) {
					return 0;
				}else if(o1.getPieceValue()>o2.getPieceValue()) {
					return 1;
				}
				else {
					throw new RuntimeException("the piece values are not integers");
				}
				//return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
			}
			
		});
		
		Collections.sort(blackTakenPieces, new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				if(o1.getPieceValue()<o2.getPieceValue()) {
					return -1;
				}else if(o1.getPieceValue() == o2.getPieceValue()) {
					return 0;
				}else if(o1.getPieceValue()>o2.getPieceValue()) {
					return 1;
				}
				else {
					throw new RuntimeException("the piece values are not integers");
				}
			}
			
		});
		
		for(final Piece takenPiece : whiteTakenPieces) {
			try {
				final BufferedImage image = ImageIO.read(new File("simpleArt/medium/"+takenPiece.getPieceColor().toString().substring(0,1)+
						takenPiece.toString()+".gif"));
				 final ImageIcon ic = new ImageIcon(image);
	             final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
	                          ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
				this.southPanel.add(imageLabel);
			}catch(final IOException e) {
				e.printStackTrace();
			}
		}
		
		for(final Piece takenPiece : blackTakenPieces) {
			if(takenPiece == null) {
				continue;
			}
			try {
				final BufferedImage image = ImageIO.read(new File("simpleArt/medium/"+takenPiece.getPieceColor().toString().substring(0,1)+
						takenPiece.toString()+".gif"));
				final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        	 ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
				this.northPanel.add(imageLabel);
			}catch(final IOException e) {
				e.printStackTrace();
			}
		}
		
		validate();
	}
}
