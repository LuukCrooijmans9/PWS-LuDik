package com.LuDik.EvoAI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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

import org.jfree.data.xy.XYSeries;

public class InfoPanel extends JPanel {


	private static final long serialVersionUID = 1L;
	private static final int CREATURELIST_LENGTH = 5;
	private static final int IPWidth = 400;
	private static final int TIMEDIFF_SAMPLESIZE = 100;

	// UI containers:
	private DARWIN mainFrame;
	private CameraPanel cameraPanel;

	private Board board;
	private TimeKeeper timeKeeper;

	private List<Creature> creatures;
	private Creature selectedCreature;
	private int currentPeriod;

	private static int IPHeight;
	private double averageFitnessOfPreviousPeriod;
	
	private JPanel singleLineInfoPanel;
	private JPanel listPanel;
	private JPanel graphPanel;
	
	private LineChartPanel fitnessLineChartPanel;
	private LineChartPanel ageLineChartPanel;
	private LineChartPanel totalFoodEatenLineChartPanel;
	
	private JLabel stepLbl;
	private JLabel timePerStepLbl;
	private JLabel crtrAmountLbl;
	private JLabel selectedCrtrFatLbl;
	private JLabel selectedCrtrFoodFitnessLbl;
	private JLabel selectedCrtrAgeFatBurnedLbl;
	private JLabel mainSeed;
	private JLabel mapSeed;

	private JList<String> creaturesList;
	private ArrayList<Long> timeDiffArray;
	private double timeDiff;

	

	public InfoPanel(DARWIN parent) {
		initInfoPanel(parent);
	}

	private void initInfoPanel(DARWIN parent) {
		IPHeight = parent.getCameraPanel().getCPHEIGHT();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(IPWidth, IPHeight));

		mainFrame = parent;
		
		singleLineInfoPanel = new JPanel();
		singleLineInfoPanel.setPreferredSize(new Dimension(IPWidth, IPHeight/200));
		
//		singleLineInfoPanel.setLayout(new BoxLayout(singleLineInfoPanel, BoxLayout.PAGE_AXIS));
		listPanel = new JPanel();
		listPanel.setPreferredSize(new Dimension(IPWidth, IPHeight/200));


		graphPanel = new JPanel();
		graphPanel.setPreferredSize(new Dimension(IPWidth, IPHeight/2));

//		singleLineInfoPanel.setBackground(Color.blue);
//		listPanel.setBackground(Color.red);
//		graphPanel.setBackground(Color.green);

//		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

		
		
		
		add(singleLineInfoPanel);
		add(listPanel);
		add(graphPanel);
		

		stepLbl = new JLabel("Step: " + 0);
		timePerStepLbl = new JLabel("TimePerStep: " + 0);
		crtrAmountLbl = new JLabel("Amount of creatures: " + 0);
		selectedCrtrFatLbl = new JLabel("selectedCreature: " + 0);
		selectedCrtrFoodFitnessLbl = new JLabel("TotalFoodEaten: " + 0 + " Fitness: " + 0);
		selectedCrtrAgeFatBurnedLbl = new JLabel("Age: " + 0 + " FatBurnedThisStep: " + 0);
		mainSeed = new JLabel("MainSeed: " + ConfigSingleton.INSTANCE.mainSeed);
		mapSeed = new JLabel("MapSeed: "+ ConfigSingleton.INSTANCE.mapGenSeed);

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
		
		fitnessLineChartPanel = new LineChartPanel(this, "Fitness in relation to Period", "Period", "Fitness");
		ageLineChartPanel = new LineChartPanel(this, "Age in relation to Period", "Period", "Age");
		totalFoodEatenLineChartPanel = new LineChartPanel(this, "TotalFoodEaten in relation to Period", "Period", "TotalFoodEaten");

		singleLineInfoPanel.add(stepLbl);
		singleLineInfoPanel.add(timePerStepLbl);
		singleLineInfoPanel.add(crtrAmountLbl);
		singleLineInfoPanel.add(selectedCrtrFatLbl);
		singleLineInfoPanel.add(selectedCrtrFoodFitnessLbl);
		singleLineInfoPanel.add(selectedCrtrAgeFatBurnedLbl);
		singleLineInfoPanel.add(mainSeed);
		singleLineInfoPanel.add(mapSeed);
		
		listPanel.add(creaturesList);
		graphPanel.add(fitnessLineChartPanel);
		graphPanel.add(ageLineChartPanel);
		graphPanel.add(totalFoodEatenLineChartPanel);
		
		timeDiffArray = new ArrayList<Long>();
		
		

	}


	public void update() {


		updateTimeKeeperRelatedComponents();

		updateBoardRelatedComponents();

		updateSelectedCreatureRelatedComponents();
		
		updateCreaturesList();
		
		if (currentPeriod != board.getPeriod()) {
			
			XYSeries averageFitnessSeries = convertArrayListToXYSeries(board.getAverageFitnessArray(), "averageFitness");
			XYSeries averageAgeSeries = convertArrayListToXYSeries(board.getAverageAgeArray(), "averageAge");
			XYSeries averageTotalFoodEatenSeries = convertArrayListToXYSeries(board.getAverageTotalFoodEatenArray(), "averageTotalFoodEaten");
			fitnessLineChartPanel.updateDataset(averageFitnessSeries);
			ageLineChartPanel.updateDataset(averageAgeSeries);
			totalFoodEatenLineChartPanel.updateDataset(averageTotalFoodEatenSeries);
			
			currentPeriod = board.getPeriod();
		}
	}

	private void updateCreaturesList() {
		creatures = board.getAllCreaturesOfPeriod();

		if (creatures != null) {
			creatures.sort(new Comparator<Creature>() {
				@Override
				public int compare(Creature creature2, Creature creature1) {

					return Double.compare(creature1.getFitness(), creature2.getFitness());
				}

			});

			DefaultListModel<String> lModel = new DefaultListModel<String>();

			int listSize = Math.min(creatures.size(), CREATURELIST_LENGTH);

			for (int i = 0; i < listSize; i++) {
				Creature crtr = creatures.get(i);
				lModel.add(i, "Creature" + crtr.getCreatureID() + " Fitness: " + (int) crtr.getFitness() + "Fat: " + (int) crtr.getFat() + " " + (int) crtr.getDesiredFood());
			}

			creaturesList.setModel(lModel);

			
			
		}
	}

	private void updateSelectedCreatureRelatedComponents() {
		if (selectedCreature != null) {
			
			selectedCrtrFatLbl.setText("selectedCreature: " + (int) selectedCreature.getCreatureShape().getCenterX()
					+ " , " + (int) selectedCreature.getCreatureShape().getCenterY() + " Fat: "
					+ (int) selectedCreature.getFat());
			
			selectedCrtrFoodFitnessLbl.setText("TotalFoodEaten: " + (int) selectedCreature.getTotalFoodEaten() + " Fitness: "
					+ selectedCreature.getFitness());
			
			selectedCrtrAgeFatBurnedLbl.setText(
					"Age: " + (int) selectedCreature.getAge() + " FatBurnedThisStep: " + (int) selectedCreature.getFatBurned());
		}
	}

	private void updateTimeKeeperRelatedComponents() {
		if (timeKeeper != null) {
			
			if (timeDiffArray.size() > TIMEDIFF_SAMPLESIZE) {
				timeDiff -= timeDiffArray.get(0);
				timeDiffArray.remove(0);
			}
			
			timeDiffArray.add(timeKeeper.getTimeDiffNano());
			timeDiff += timeKeeper.getTimeDiffNano();
			
			double avgTimeDiff = timeDiff / timeDiffArray.size();
			
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			stepLbl.setText("Step: " + timeKeeper.getStep());
			if (timeKeeper.getStep() % TIMEDIFF_SAMPLESIZE == 0){
				timePerStepLbl.setText("MillisecondsPerStep: " + df.format(avgTimeDiff / Math.pow(10, 6)));
			}
		}
	}

	private void updateBoardRelatedComponents() {
		if (board != null && board.getLivingCreatures() != null) {
			crtrAmountLbl.setText("Amount of creatures: " + board.getLivingCreatures().size());
		}
	}
	
	private XYSeries convertArrayListToXYSeries(ArrayList<Double> arrayList, Comparable comparable) {		
				
		XYSeries xySeries = new XYSeries(comparable);
		
		for (int i = 0; i < arrayList.size(); i++) {
			xySeries.add(
					Double.valueOf(i), 
					Double.valueOf(arrayList.get(i)));
		}
		
		return xySeries;
	}

	public void setTimeKeeper(TimeKeeper tmkpr) {
		timeKeeper = tmkpr;
		
	}
	
	public void setBoard(Board brd) {
		board = brd;
		currentPeriod = board.getPeriod();
		
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

	public double getAverageFitnessOfPreviousPeriod() {
		return averageFitnessOfPreviousPeriod;
	}

	public void setAverageFitnessOfPreviousPeriod(double averageFitnessOfPreviousPeriod) {
		this.averageFitnessOfPreviousPeriod = averageFitnessOfPreviousPeriod;
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
