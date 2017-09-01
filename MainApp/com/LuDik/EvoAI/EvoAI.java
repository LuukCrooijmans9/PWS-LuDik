package com.LuDik.EvoAI;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class EvoAI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CameraPanel cameraPanel;
	private InfoPanel infoPanel;
	private ActionPanel actionPanel;
	private Board board;
	
	
	public EvoAI() {
		initUI();
		
	}
	
	private void initUI() {
		setLayout(new FlowLayout());
		
		
		cameraPanel = new CameraPanel(this);
		
		
//		InfoPanel infoPanel = new InfoPanel(this);
//		add(infoPanel);
		
		actionPanel = new ActionPanel(this);
		add(actionPanel);
		add(cameraPanel);
				
		setSize(2000, 2000);
		
		setResizable(false);
		pack();
					
		setTitle("EvoAI");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				new EvoAI().setVisible(true); 
			}
		});
	}

	public CameraPanel getCameraPanel() {
		return cameraPanel;
	}

	public InfoPanel getInfoPanel() {
		return infoPanel;
	}

	public ActionPanel getActionPanel() {
		return actionPanel;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
}
