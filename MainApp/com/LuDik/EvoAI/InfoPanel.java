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

public class InfoPanel extends JPanel {

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
	private double averageFitnessOfPreviousGeneration;
	
	private JPanel singleLineInfoPanel;
	private JPanel listPanel;
	private JPanel graphPanel;
	
	private JLabel stepLbl;
	private JLabel timePerStepLbl;
	private JLabel crtrAmountLbl;
	private JLabel mousePosLbl;
	private JLabel selectedCrtrPosFatLbl;
	private JLabel selectedCrtrFoodFitnessLbl;
	private JLabel selectedCrtrAgeFatBurnedLbl;

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
		
		singleLineInfoPanel = new JPanel();
//		singleLineInfoPanel.setLayout(new BoxLayout(singleLineInfoPanel, BoxLayout.PAGE_AXIS));
		listPanel = new JPanel();
		graphPanel = new JPanel();
//		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

		
		add(singleLineInfoPanel);
		add(listPanel);
		add(graphPanel);

		stepLbl = new JLabel("Step: " + 0);
		timePerStepLbl = new JLabel("TimePerStep: " + 0);
		crtrAmountLbl = new JLabel("Amount of creatures: " + 0);
		mousePosLbl = new JLabel("mousePosX" + 0 + "mousePosY" + 0);
		selectedCrtrPosFatLbl = new JLabel("selectedCreature: " + 0);
		selectedCrtrFoodFitnessLbl = new JLabel("TotalFoodEaten: " + 0 + " Fitness: " + 0);
		selectedCrtrAgeFatBurnedLbl = new JLabel("Age: " + 0 + " FatBurnedThisStep: " + 0);

		creaturesList = new JList();
		creaturesList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!creaturesList.isSelectionEmpty()) {
					int selectedIndex = creaturesList.getSelectedIndex();
					mainFrame.getCameraPanel().setSelectedCreature(creatures.get(selectedIndex));
				}
			}
		});

		singleLineInfoPanel.add(stepLbl);
		singleLineInfoPanel.add(timePerStepLbl);
		singleLineInfoPanel.add(crtrAmountLbl);
		singleLineInfoPanel.add(mousePosLbl);
		singleLineInfoPanel.add(selectedCrtrPosFatLbl);
		singleLineInfoPanel.add(selectedCrtrFoodFitnessLbl);
		singleLineInfoPanel.add(selectedCrtrAgeFatBurnedLbl);
		listPanel.add(creaturesList);

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

		creatures = board.getAllCreaturesOfGeneration();

		// selectedCreature = board.getCreatures().get(0);

		if (timeKeeper != null) {
			stepLbl.setText("Step: " + timeKeeper.getStep());
			timePerStepLbl.setText("NanoTimePerStep: " + timeKeeper.getTimeDiffNano() / Math.pow(10, 6));
		}

		if (board != null && board.getCreatures() != null) {
			crtrAmountLbl.setText("Amount of creatures: " + board.getCreatures().size());
		}

		if (selectedCreature != null) {
			selectedCrtrPosFatLbl.setText("selectedCreature: " + (int) selectedCreature.getCreatureShape().getCenterX()
					+ " , " + (int) selectedCreature.getCreatureShape().getCenterY() + " Fat: "
					+ (int) selectedCreature.getFat());
			selectedCrtrFoodFitnessLbl.setText("TotalFoodEaten: " + (int) selectedCreature.getTotalFoodEaten() + " Fitness: "
					+ selectedCreature.getFitness());
			selectedCrtrAgeFatBurnedLbl.setText(
					"Age: " + (int) selectedCreature.getAge() + " FatBurnedThisStep: " + (int) selectedCreature.getFatBurned());
		}

		if (creatures != null) {
			creatures.sort(new Comparator<Creature>() {
				@Override
				public int compare(Creature creature2, Creature creature1) {

					return Double.compare(creature1.getFitness(), creature2.getFitness());
				}

			});

			DefaultListModel lModel = new DefaultListModel();

			int listSize = Math.min(creatures.size(), 10);

			for (int i = 0; i < listSize; i++) {
				// System.out.println("Fitness" + crtr.getFitness());
				lModel.add(i, (int) creatures.get(i).getFitness());
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

	public double getAverageFitnessOfPreviousGeneration() {
		return averageFitnessOfPreviousGeneration;
	}

	public void setAverageFitnessOfPreviousGeneration(double averageFitnessOfPreviousGeneration) {
		this.averageFitnessOfPreviousGeneration = averageFitnessOfPreviousGeneration;
	}

	public JList getCreaturesList() {
		return creaturesList;
	}

	public void setCreaturesList(JList creaturesList) {
		this.creaturesList = creaturesList;
	}

	public List<Creature> getCreatures() {
		return creatures;
	}

	public void setCreatures(List<Creature> creatures) {
		this.creatures = creatures;
	}
	
}
