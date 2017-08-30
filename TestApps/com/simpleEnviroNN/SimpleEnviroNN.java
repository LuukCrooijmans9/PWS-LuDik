package com.simpleEnviroNN;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;

class SimpleEnviroNN extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InfoPanel infoPanel;
	
	  SimpleEnviroNN() {
        
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
		
		
		
		setTitle("SimpleEnviroNN");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
	}

	

	  static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				SimpleEnviroNN game = new SimpleEnviroNN();
				game.setVisible(true);
			}
		});
	}
}