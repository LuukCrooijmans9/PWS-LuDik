package com.LuDik.EvoAI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	// UI containers:
	private EvoAI mainFrame;
	private CameraPanel cameraPanel;

	private Board board;
	private TimeKeeper timeKeeper;
	
	private Creature selectedCreature;

	private static int IPHeight;
	private static final int IPWidth = 400;

	private JLabel stepLbl;
	private JLabel timePerStepLbl;
	private JLabel crtrAmountLbl;
	private JLabel mousePosLbl;
	private JLabel selectedCrtrPosFatLbl;
	private JLabel selectedCrtrFoodFitnessLbl;
	

	public InfoPanel(EvoAI parent) {
		initInfoPanel(parent);
	}

	private void initInfoPanel(EvoAI parent) {
		IPHeight = parent.getCameraPanel().getCPHEIGHT();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(IPWidth, IPHeight));

		mainFrame = parent;

		
		stepLbl = new JLabel("Step: " + 0);
		timePerStepLbl = new JLabel("TimePerStep: " + 0);
		crtrAmountLbl = new JLabel("Amount of creatures: " + 0);
		mousePosLbl = new JLabel("mousePosX" + 0 + "mousePosY" + 0);
		selectedCrtrPosFatLbl = new JLabel("selectedCreature: " + 0 );
		selectedCrtrFoodFitnessLbl = new JLabel("TotalFoodEaten: " + 0 + " Fitness: " + 0);
		
		add(stepLbl);
		add(timePerStepLbl);
		add(crtrAmountLbl);
		add(mousePosLbl);
		add(selectedCrtrPosFatLbl);
		add(selectedCrtrFoodFitnessLbl);
		
		
		
	}
	
	public void setTimeKeeper(TimeKeeper tmkpr) {
		timeKeeper = tmkpr;

	}

	public void setBoard(Board brd) {
		board = brd;
	}
	
	public void updateMousePos(int x, int y) {
		mousePosLbl.setText("mousePosX " + x + " mousePosY " + y);
	}
	
	public void update() {
		
//		selectedCreature = board.getCreatures().get(0);

		
		if (timeKeeper != null) {
			stepLbl.setText("Step: " + timeKeeper.getStep());
			timePerStepLbl.setText("NanoTimePerStep: " + timeKeeper.getTimeDiffNano()/Math.pow(10, 6));
		}
		
		if (board != null && board.getCreatures() != null) {
			crtrAmountLbl.setText("Amount of creatures: " + board.getCreatures().size());
		}
		
		if (selectedCreature != null) {
			selectedCrtrPosFatLbl.setText("selectedCreature: " + (int) selectedCreature.getCreatureShape().getCenterX() + " , " + (int) selectedCreature.getCreatureShape().getCenterY() + " Fat: " + selectedCreature.getFat());
			selectedCrtrFoodFitnessLbl.setText("TotalFoodEaten: " + selectedCreature.getTotalFoodEaten() + " Fitness: " + selectedCreature.getFitness());
		}
	}

	public Creature getSelectedCreature() {
		return selectedCreature;
	}

	public void setSelectedCreature(Creature selectedCreature) {
		this.selectedCreature = selectedCreature;
	}
}
