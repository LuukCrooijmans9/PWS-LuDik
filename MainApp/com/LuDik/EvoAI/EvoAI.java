package com.LuDik.EvoAI;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class EvoAI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EvoAI() {
		initUI();
		
	}
	
	private void initUI() {
		setLayout(new FlowLayout());
		
		Board board = new Board(this);
		
		CameraPanel cameraPanel = new CameraPanel(this);
		add(cameraPanel);
		
		InfoPanel infoPanel = new InfoPanel(this);
		add(infoPanel);
		
		ActionPanel actionPanel = new ActionPanel(this);
		add(actionPanel);
				
		setSize(2000, 2000);
		
		setResizable(false);
		pack();
					
		setTitle("EvoAI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				new EvoAI().setVisible(true); 
			}
		});
	}
	
}
