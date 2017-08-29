package com.simplemove;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;

public class SimpleMove extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InfoPanel infoPanel;
	
	public SimpleMove() {
        
        initUI();
   }

	private void initUI() {
		
		setLayout(new FlowLayout());
		
		Board board = new Board();
		
		infoPanel = new InfoPanel(board);
		add(infoPanel);
		
		add(board);
		
		board.setInfoPanel(infoPanel);
		
		setSize(1000, 1000);
		
		setResizable(false);
		pack();
		
//		setSize(Board.getBWidth() + InfoPanel.getIPWidth(),
//				Board.getBHeight() + InfoPanel.getIPHeight());
		
		
		
		setTitle("SimpleMove");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
	}

	

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				SimpleMove game = new SimpleMove();
				game.setVisible(true);
			}
		});
	}
}