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
import javax.swing.JTabbedPane;
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
	
	private JTabbedPane graphTP;
	private JPanel fitnessPanel;
	private JPanel populationPanel;
	
	private LineChartPanel fitnessLineChartPanel;
	private LineChartPanel ageLineChartPanel;
	private LineChartPanel totalFoodEatenLineChartPanel;

	private LineChartPanel creaturesBornLineChartPanel;
	private LineChartPanel creaturesSpawnedLineChartPanel;
	
	private LineChartPanel avgTotalDistanceTravelledLineChartPanel;
	private LineChartPanel avgNettoDistanceTravelledLineChartPanel;
	
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
	private JPanel distancePanel;
	private JPanel metaPanel;
	private LineChartPanel realTimePerPeriodLineChartPanel;
	private LineChartPanel creaturesAliveLineChartPanel;

	

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
		
		listPanel = new JPanel();
		listPanel.setPreferredSize(new Dimension(IPWidth, IPHeight/200));
		
		add(singleLineInfoPanel);
		add(listPanel);
		
		

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
		

		singleLineInfoPanel.add(stepLbl);
		singleLineInfoPanel.add(timePerStepLbl);
		singleLineInfoPanel.add(crtrAmountLbl);
		singleLineInfoPanel.add(selectedCrtrFatLbl);
		singleLineInfoPanel.add(selectedCrtrFoodFitnessLbl);
		singleLineInfoPanel.add(selectedCrtrAgeFatBurnedLbl);
		singleLineInfoPanel.add(mainSeed);
		singleLineInfoPanel.add(mapSeed);
		
		listPanel.add(creaturesList);
		
		/**
		 * Graphs and their components are created here:
		 */
		
		graphTP = new JTabbedPane();
		graphTP.setPreferredSize(new Dimension(IPWidth, IPHeight/2));
		add(graphTP);
		
		fitnessPanel = new JPanel();
		graphTP.add(fitnessPanel, "Fitness");
		
		
		fitnessLineChartPanel = new LineChartPanel(this, "Fitness in relation to Period", "Period", "Fitness");
		ageLineChartPanel = new LineChartPanel(this, "Age in relation to Period", "Period", "Age");
		totalFoodEatenLineChartPanel = new LineChartPanel(this, "TotalFoodEaten in relation to Period", "Period", "TotalFoodEaten");
		
		fitnessPanel.add(fitnessLineChartPanel);		
		fitnessPanel.add(ageLineChartPanel);
		fitnessPanel.add(totalFoodEatenLineChartPanel);
		
		populationPanel = new JPanel();
		graphTP.add(populationPanel, "Population");
		
		creaturesBornLineChartPanel = new LineChartPanel(this, "Amount of creatures born in relation to Period", "Period", "Born Creatures");
		creaturesSpawnedLineChartPanel = new LineChartPanel(this, "Amount of creatures spawned in relation to Period", "Period", "Spawned Creatures");
		creaturesAliveLineChartPanel = new LineChartPanel(this, "Amount of creatures Alive in relation to Period", "Period", "Alive Creatures");

		populationPanel.add(creaturesBornLineChartPanel);
		populationPanel.add(creaturesSpawnedLineChartPanel);
		populationPanel.add(creaturesAliveLineChartPanel);
		
		distancePanel = new JPanel();
		graphTP.add(distancePanel, "Distance");
		
		avgTotalDistanceTravelledLineChartPanel = new LineChartPanel(this, "avgTotalDistanceTravelled in relation to Period", "Period", "avgTotalDistanceTravelled");
		avgNettoDistanceTravelledLineChartPanel = new LineChartPanel(this, "avgNettoDistanceTravelled in relation to Period", "Period", "avgNettoDistanceTravelled");
		
		distancePanel.add(avgTotalDistanceTravelledLineChartPanel);
		distancePanel.add(avgNettoDistanceTravelledLineChartPanel);;
		
		metaPanel = new JPanel();
		graphTP.add(metaPanel, "Meta");
		
		realTimePerPeriodLineChartPanel = new LineChartPanel(this, "realTimePerPeriod (ms) in relation to Period", "Period", "realTimePerPeriod");

		metaPanel.add(realTimePerPeriodLineChartPanel);
		
		timeDiffArray = new ArrayList<Long>();
		
		

	}


	public void update() {


		updateTimeKeeperRelatedComponents();

		updateBoardRelatedComponents();

		updateSelectedCreatureRelatedComponents();
		
		updateCreaturesList();
		
		if (currentPeriod != board.getPeriod()) {
			
			XYSeries avgFitnessSeries = convertFloatArrayListToXYSeries(board.getBoardStats().getAvgFitnessArray(), "averageFitness");
			XYSeries avgAgeSeries = convertFloatArrayListToXYSeries(board.getBoardStats().getAvgAgeArray(), "averageAge");
			XYSeries avgTotalFoodEatenSeries = convertFloatArrayListToXYSeries(board.getBoardStats().getAvgTotalFoodEatenArray(), "averageTotalFoodEaten");
			fitnessLineChartPanel.updateDataset(avgFitnessSeries);
			ageLineChartPanel.updateDataset(avgAgeSeries);
			totalFoodEatenLineChartPanel.updateDataset(avgTotalFoodEatenSeries);
			
			XYSeries creaturesBornSeries = convertIntegerArrayListToXYSeries(board.getBoardStats().getCreaturesBornArray(), "CreaturesBorn");
			XYSeries creaturesSpawnedSeries = convertIntegerArrayListToXYSeries(board.getBoardStats().getCreaturesSpawnedArray(), "CreaturesSpawned");
			XYSeries creaturesAliveSeries = convertIntegerArrayListToXYSeries(board.getBoardStats().getCreaturesAliveArray(), "CreaturesAlive");
			
			creaturesBornLineChartPanel.updateDataset(creaturesBornSeries);
			creaturesSpawnedLineChartPanel.updateDataset(creaturesSpawnedSeries);
			creaturesAliveLineChartPanel.updateDataset(creaturesAliveSeries);

			XYSeries avgTotalDistanceTravelledSeries = convertFloatArrayListToXYSeries(board.getBoardStats().getAvgTotalDistanceTravelledArray(), "avgTotalDistanceTravelled");
			XYSeries avgNettoDistanceTravelledSeries = convertFloatArrayListToXYSeries(board.getBoardStats().getAvgNettoDistanceTravelledArray(), "avgNettoDistanceTravelled");

			avgTotalDistanceTravelledLineChartPanel.updateDataset(avgTotalDistanceTravelledSeries);
			avgNettoDistanceTravelledLineChartPanel.updateDataset(avgNettoDistanceTravelledSeries);

			XYSeries realTimePerPeriodSeries = convertFloatArrayListToXYSeries(board.getBoardStats().getRealTimePerPeriodArray(), "avgTotalDistanceTravelled");
			realTimePerPeriodLineChartPanel.updateDataset(realTimePerPeriodSeries);
			
			
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

	private XYSeries convertFloatArrayListToXYSeries(ArrayList<Float> arrayList, Comparable comparable) {		
		
		XYSeries xySeries = new XYSeries(comparable);
		
		for (int i = 0; i < arrayList.size(); i++) {
			xySeries.add(
					Float.valueOf(i), 
					Float.valueOf(arrayList.get(i)));
		}
		
		return xySeries;
	}
	
	private XYSeries convertIntegerArrayListToXYSeries(ArrayList<Integer> arrayList, Comparable comparable) {		
		
		XYSeries xySeries = new XYSeries(comparable);
		
		for (int i = 0; i < arrayList.size(); i++) {
			xySeries.add(
					Integer.valueOf(i), 
					Integer.valueOf(arrayList.get(i)));
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
