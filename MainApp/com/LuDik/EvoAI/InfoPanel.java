package com.LuDik.EvoAI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InfoPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	// UI containers:
	private EvoAI mainFrame;
	private CameraPanel cameraPanel;

	private Board board;
	private TimeKeeper timeKeeper;
	
	private List<Creature> creatures;
	private Creature selectedCreature;

	private static int IPHeight;
	private static final int IPWidth = 400;

	private JLabel stepLbl;
	private JLabel timePerStepLbl;
	private JLabel crtrAmountLbl;
	private JLabel mousePosLbl;
	private JLabel selectedCrtrPosFatLbl;
	private JLabel selectedCrtrFoodFitnessLbl;

	private JList creaturesList;
	

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
		
		creaturesList = new JList();
		creaturesList.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!creaturesList.isSelectionEmpty()) {
					int selectedIndex = creaturesList.getSelectedIndex();
					mainFrame.getCameraPanel().setSelectedCreature(creatures.get(selectedIndex));
				}
			}
		});
		
		add(stepLbl);
		add(timePerStepLbl);
		add(crtrAmountLbl);
		add(mousePosLbl);
		add(selectedCrtrPosFatLbl);
		add(selectedCrtrFoodFitnessLbl);
		add(creaturesList);
		
		
		
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
		
		creatures = board.getCreatures();
		
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
		
		if (creatures != null) {
			creatures.sort( new Comparator<Creature>() {
				@Override
		        public int compare(Creature creature2, Creature creature1)
		        {

		            return  Double.compare(creature1.getFitness() , creature2.getFitness());
		        }
				
			});
			
			DefaultListModel lModel = new DefaultListModel();
			
			int listSize = Math.min(creatures.size(), 10);
			
			for (int i = 0; i < listSize; i++) {
//				System.out.println("Fitness" + crtr.getFitness());
				lModel.add(i, creatures.get(i).getFitness());
			}
			
			creaturesList.setModel(lModel);
			
			
		}
	}

	public Creature getSelectedCreature() {
		return selectedCreature;
	}

	public void setSelectedCreature(Creature selectedCreature) {
		this.selectedCreature = selectedCreature;
	}

	public CameraPanel getCameraPanel() {
		return cameraPanel;
	}

	public void setCameraPanel(CameraPanel cameraPanel) {
		this.cameraPanel = cameraPanel;
	}
}
