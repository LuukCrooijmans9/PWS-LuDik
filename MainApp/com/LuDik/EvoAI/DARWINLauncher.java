package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import java.awt.Scrollbar;
import javax.swing.JScrollBar;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import com.sun.glass.events.WindowEvent;

import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This class is a frame that acts a launcher for the main program, DARWIN. In this frame you can
 * adjust the variables that are used in the simulation of DARWIN, stored in the ConfigSingleton enumeration, and
 * start DARWIN with the "start" button when the values for the variables are to your liking
 */

public class DARWINLauncher extends JFrame {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DARWINLauncher frame = new DARWINLauncher();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DARWINLauncher() {
		setTitle("D.A.R.W.I.N. Launcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(842, 534));
		setBackground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
//		tabbedPane_1.setToolTipText("This is a tooltip");
		GridBagConstraints gbc_tabbedPane_1 = new GridBagConstraints();
		gbc_tabbedPane_1.gridwidth = 3;
		gbc_tabbedPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_tabbedPane_1.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane_1.gridx = 0;
		gbc_tabbedPane_1.gridy = 0;
		getContentPane().add(tabbedPane_1, gbc_tabbedPane_1);

		/**
		 * Here the evolution tab and its components are constructed:
		 */
		
		JPanel environmentPanel = new JPanel();
		tabbedPane_1.addTab("Environment", null, environmentPanel, null);
		environmentPanel.setLayout(new BoxLayout(environmentPanel, BoxLayout.Y_AXIS));

		JLabel lbl_mapSmoothness = new JLabel("mapSmoothness");
		lbl_mapSmoothness.setToolTipText("This value determines how smooth the transition of fertilities is");

		JSpinner spinner_mapSmoothness = new JSpinner();
		spinner_mapSmoothness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_mapSmoothness.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.mapSmoothness = value;

			}
		});

		spinner_mapSmoothness.setPreferredSize(new Dimension(100, 20));
		spinner_mapSmoothness.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.mapSmoothness, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.mapSmoothness / 10));

		JPanel panel_mapSmoothness = new JPanel();
		panel_mapSmoothness.add(lbl_mapSmoothness);
		panel_mapSmoothness.add(spinner_mapSmoothness);
		
		JLabel lbl_mainSeed = new JLabel("mainSeed");
		lbl_mainSeed.setToolTipText("toolTipText");

		JSpinner spinner_mainSeed = new JSpinner();
		spinner_mainSeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_mainSeed.getModel();

				long value = (long) numberModel.getValue();
				ConfigSingleton.INSTANCE.mainSeed = value;

			}
		});

		spinner_mainSeed.setPreferredSize(new Dimension(100, 20));
		spinner_mainSeed.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.mainSeed, Long.MIN_VALUE, Long.MAX_VALUE, (long) 1));

		JPanel panel_mainSeed = new JPanel();
		panel_mainSeed.add(lbl_mainSeed);
		panel_mainSeed.add(spinner_mainSeed);

		JLabel lbl_mapGenSeed = new JLabel("mapGenSeed");
		lbl_mapGenSeed.setToolTipText("toolTipText");

		JSpinner spinner_mapGenSeed = new JSpinner();
		spinner_mapGenSeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_mapGenSeed.getModel();

				long value = (long) numberModel.getValue();
				ConfigSingleton.INSTANCE.mapGenSeed = value;

			}
		});

		spinner_mapGenSeed.setPreferredSize(new Dimension(100, 20));
		spinner_mapGenSeed.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.mapGenSeed, Long.MIN_VALUE, Long.MAX_VALUE, (long) 1));

		JPanel panel_mapGenSeed = new JPanel();
		panel_mapGenSeed.add(lbl_mapGenSeed);
		panel_mapGenSeed.add(spinner_mapGenSeed);

		
		
		JLabel lbl_waterPercentage = new JLabel("waterPercentage");
		lbl_waterPercentage.setToolTipText("toolTipText");

		JSpinner spinner_waterPercentage = new JSpinner();
		spinner_waterPercentage.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_waterPercentage.getModel();
				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.waterPercentage = value;
			}
		});

		spinner_waterPercentage.setPreferredSize(new Dimension(100, 20));
		spinner_waterPercentage.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.waterPercentage, 0, 100, 2));

		JPanel panel_waterPercentage = new JPanel();
		panel_waterPercentage.add(lbl_waterPercentage);
		panel_waterPercentage.add(spinner_waterPercentage);

		JLabel lbl_tileSize = new JLabel("tileSize");
		lbl_tileSize.setToolTipText("toolTipText");

		JSpinner spinner_tileSize = new JSpinner();
		spinner_tileSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_tileSize.getModel();

				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.tileSize = value;

			}
		});

		spinner_tileSize.setPreferredSize(new Dimension(100, 20));
		spinner_tileSize.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.tileSize, 0, 2147483647, 4));

		JPanel panel_tileSize = new JPanel();
		panel_tileSize.add(lbl_tileSize);
		panel_tileSize.add(spinner_tileSize);

		JLabel lbl_mapSizeInTiles = new JLabel("mapSizeInTiles");
		lbl_mapSizeInTiles.setToolTipText("toolTipText");

		JSpinner spinner_mapSizeInTiles = new JSpinner();
		spinner_mapSizeInTiles.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_mapSizeInTiles.getModel();

				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.mapSizeInTiles = value;

			}
		});

		spinner_mapSizeInTiles.setPreferredSize(new Dimension(100, 20));
		spinner_mapSizeInTiles.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.mapSizeInTiles, 0, 2147483647, 10));

		JPanel panel_mapSizeInTiles = new JPanel();
		panel_mapSizeInTiles.add(lbl_mapSizeInTiles);
		panel_mapSizeInTiles.add(spinner_mapSizeInTiles);

		JLabel lbl_maxFertility = new JLabel("maxFertility");
		lbl_maxFertility.setToolTipText("toolTipText");

		JSpinner spinner_maxFertility = new JSpinner();
		spinner_maxFertility.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_maxFertility.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.maxFertility = value;

			}
		});

		spinner_maxFertility.setPreferredSize(new Dimension(100, 20));
		spinner_maxFertility.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.maxFertility, 0.0,
				Integer.MAX_VALUE, 0.001));

		JPanel panel_maxFertility = new JPanel();
		panel_maxFertility.add(lbl_maxFertility);
		panel_maxFertility.add(spinner_maxFertility);

		
		JLabel lbl_maxFood = new JLabel("maxFood");
		lbl_maxFood.setToolTipText("toolTipText");

		JSpinner spinner_maxFood = new JSpinner();
		spinner_maxFood.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_maxFood.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.maxFood = value;

			}
		});

		spinner_maxFood.setPreferredSize(new Dimension(100, 20));
		spinner_maxFood.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.maxFood, 0.0,
				Integer.MAX_VALUE, 1));

		JPanel panel_maxFood = new JPanel();
		panel_maxFood.add(lbl_maxFood);
		panel_maxFood.add(spinner_maxFood);

		environmentPanel.add(panel_mapSmoothness);
		environmentPanel.add(panel_mainSeed);
		environmentPanel.add(panel_mapGenSeed);
		environmentPanel.add(panel_waterPercentage);
		environmentPanel.add(panel_tileSize);
		environmentPanel.add(panel_mapSizeInTiles);
		environmentPanel.add(panel_maxFertility);
		environmentPanel.add(panel_maxFood);
		
		/**
		 * Here the evolution tab and its components are constructed:
		 */
		
		JPanel evolutionPanel = new JPanel();
		tabbedPane_1.addTab("Evolution", null, evolutionPanel, null);
		evolutionPanel.setLayout(new BoxLayout(evolutionPanel, BoxLayout.Y_AXIS));
		
		JLabel lbl_evolutionFactor = new JLabel("evolutionFactor");
		lbl_evolutionFactor.setToolTipText("toolTipText");

		JSpinner spinner_evolutionFactor = new JSpinner();
		spinner_evolutionFactor.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_evolutionFactor.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.evolutionFactor = value;

			}
		});

		spinner_evolutionFactor.setPreferredSize(new Dimension(100, 20));
		spinner_evolutionFactor.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.evolutionFactor, 0.0,
				2, 0.001));

		JPanel panel_evolutionFactor = new JPanel();
		panel_evolutionFactor.add(lbl_evolutionFactor);
		panel_evolutionFactor.add(spinner_evolutionFactor);

		JLabel lbl_beginAmountCreatures = new JLabel("beginAmountCreatures");
		lbl_beginAmountCreatures.setToolTipText("toolTipText");

		JSpinner spinner_beginAmountCreatures = new JSpinner();
		spinner_beginAmountCreatures.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_beginAmountCreatures.getModel();

				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.beginAmountCreatures = value;

			}
		});

		spinner_beginAmountCreatures.setPreferredSize(new Dimension(100, 20));
		spinner_beginAmountCreatures.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.beginAmountCreatures, 0, 2147483647, 40));

		JPanel panel_beginAmountCreatures = new JPanel();
		panel_beginAmountCreatures.add(lbl_beginAmountCreatures);
		panel_beginAmountCreatures.add(spinner_beginAmountCreatures);

		JLabel lbl_ratioChildrenPerParent = new JLabel("ratioChildrenPerParent");
		lbl_ratioChildrenPerParent.setToolTipText("toolTipText");

		JSpinner spinner_ratioChildrenPerParent = new JSpinner();
		spinner_ratioChildrenPerParent.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_ratioChildrenPerParent.getModel();

				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.ratioChildrenPerParent = value;

			}
		});

		spinner_ratioChildrenPerParent.setPreferredSize(new Dimension(100, 20));
		spinner_ratioChildrenPerParent.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.ratioChildrenPerParent, 0, 2147483647, 1));

		JPanel panel_ratioChildrenPerParent = new JPanel();
		panel_ratioChildrenPerParent.add(lbl_ratioChildrenPerParent);
		panel_ratioChildrenPerParent.add(spinner_ratioChildrenPerParent);

		JLabel lbl_randomCreaturesPerGeneration = new JLabel("randomCreaturesPerGeneration");
		lbl_randomCreaturesPerGeneration.setToolTipText("toolTipText");

		JSpinner spinner_randomCreaturesPerGeneration = new JSpinner();
		spinner_randomCreaturesPerGeneration.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_randomCreaturesPerGeneration.getModel();

				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.randomCreaturesPerGeneration = value;

			}
		});

		spinner_randomCreaturesPerGeneration.setPreferredSize(new Dimension(100, 20));
		spinner_randomCreaturesPerGeneration.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.randomCreaturesPerGeneration, 0, 2147483647, 1));

		JPanel panel_randomCreaturesPerGeneration = new JPanel();
		panel_randomCreaturesPerGeneration.add(lbl_randomCreaturesPerGeneration);
		panel_randomCreaturesPerGeneration.add(spinner_randomCreaturesPerGeneration);

		evolutionPanel.add(panel_evolutionFactor);
		evolutionPanel.add(panel_beginAmountCreatures);
		evolutionPanel.add(panel_ratioChildrenPerParent);
		evolutionPanel.add(panel_randomCreaturesPerGeneration);
		
		/**
		 * Here the creatureProperties tab and its components are constructed.
		 */
		
		JPanel creaturePropertiesPanel = new JPanel();
		tabbedPane_1.addTab("Creature Properties", null, creaturePropertiesPanel, null);
		creaturePropertiesPanel.setLayout(new BoxLayout(creaturePropertiesPanel, BoxLayout.Y_AXIS));
		
		JLabel lbl_crtrSize = new JLabel("crtrSize");
		lbl_crtrSize.setToolTipText("toolTipText");

		JSpinner spinner_crtrSize = new JSpinner();
		spinner_crtrSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_crtrSize.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.crtrSize = value;

			}
		});

		spinner_crtrSize.setPreferredSize(new Dimension(100, 20));
		spinner_crtrSize.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.crtrSize, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.crtrSize / 10));

		JPanel panel_crtrSize = new JPanel();
		panel_crtrSize.add(lbl_crtrSize);
		panel_crtrSize.add(spinner_crtrSize);

		JLabel lbl_eyeLength = new JLabel("eyeLength");
		lbl_eyeLength.setToolTipText("toolTipText");

		JSpinner spinner_eyeLength = new JSpinner();
		spinner_eyeLength.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_eyeLength.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.eyeLength = value;

			}
		});

		spinner_eyeLength.setPreferredSize(new Dimension(100, 20));
		spinner_eyeLength.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.eyeLength, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.eyeLength / 10));

		JPanel panel_eyeLength = new JPanel();
		panel_eyeLength.add(lbl_eyeLength);
		panel_eyeLength.add(spinner_eyeLength);

		JLabel lbl_eyeDeviation = new JLabel("eyeDeviation");
		lbl_eyeDeviation.setToolTipText("toolTipText");

		JSpinner spinner_eyeDeviation = new JSpinner();
		spinner_eyeDeviation.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_eyeDeviation.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.eyeDeviation = value;

			}
		});

		spinner_eyeDeviation.setPreferredSize(new Dimension(100, 20));
		spinner_eyeDeviation.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.eyeDeviation, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.eyeDeviation / 10));

		JPanel panel_eyeDeviation = new JPanel();
		panel_eyeDeviation.add(lbl_eyeDeviation);
		panel_eyeDeviation.add(spinner_eyeDeviation);
		
		JLabel lbl_maxSpeed = new JLabel("maxSpeed");
		lbl_maxSpeed.setToolTipText("toolTipText");

		JSpinner spinner_maxSpeed = new JSpinner();
		spinner_maxSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_maxSpeed.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.maxSpeed = value;

			}
		});

		spinner_maxSpeed.setPreferredSize(new Dimension(100, 20));
		spinner_maxSpeed.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.maxSpeed, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.maxSpeed / 10));

		JPanel panel_maxSpeed = new JPanel();
		panel_maxSpeed.add(lbl_maxSpeed);
		panel_maxSpeed.add(spinner_maxSpeed);

		JLabel lbl_maxDeltaDirection = new JLabel("maxDeltaDirection");
		lbl_maxDeltaDirection.setToolTipText("toolTipText");

		JSpinner spinner_maxDeltaDirection = new JSpinner();
		spinner_maxDeltaDirection.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_maxDeltaDirection.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.maxDeltaDirection = value;

			}
		});

		spinner_maxDeltaDirection.setPreferredSize(new Dimension(100, 20));
		spinner_maxDeltaDirection.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.maxDeltaDirection, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.maxDeltaDirection / 10));

		JPanel panel_maxDeltaDirection = new JPanel();
		panel_maxDeltaDirection.add(lbl_maxDeltaDirection);
		panel_maxDeltaDirection.add(spinner_maxDeltaDirection);

		
		
		creaturePropertiesPanel.add(panel_maxDeltaDirection);
		creaturePropertiesPanel.add(panel_maxSpeed);
		creaturePropertiesPanel.add(panel_eyeDeviation);
		creaturePropertiesPanel.add(panel_eyeLength);
		creaturePropertiesPanel.add(panel_crtrSize);
		
		/**
		 * Here the neuralNetworkTab and its components are constructed:
		 */
		
		
		JPanel neuralNetworkPanel = new JPanel();
		tabbedPane_1.addTab("Neural Network", null, neuralNetworkPanel, null);
		neuralNetworkPanel.setLayout(new BoxLayout(neuralNetworkPanel, BoxLayout.Y_AXIS));
		
		JLabel lbl_brainWidth = new JLabel("brainWidth");
		lbl_brainWidth.setToolTipText("toolTipText");

		JSpinner spinner_brainWidth = new JSpinner();
		spinner_brainWidth.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_brainWidth.getModel();

				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.brainWidth = value;

			}
		});

		spinner_brainWidth.setPreferredSize(new Dimension(100, 20));
		spinner_brainWidth.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.brainWidth, 0, 2147483647, 1));

		JPanel panel_brainWidth = new JPanel();
		panel_brainWidth.add(lbl_brainWidth);
		panel_brainWidth.add(spinner_brainWidth);

		JLabel lbl_inputLayerHeight = new JLabel("inputLayerHeight");
		lbl_inputLayerHeight.setToolTipText("toolTipText");

		JSpinner spinner_inputLayerHeight = new JSpinner();
		spinner_inputLayerHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_inputLayerHeight.getModel();

				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.inputLayerHeight = value;

			}
		});

		spinner_inputLayerHeight.setPreferredSize(new Dimension(100, 20));
		spinner_inputLayerHeight.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.inputLayerHeight, 0, 2147483647, 1));

		JPanel panel_inputLayerHeight = new JPanel();
		panel_inputLayerHeight.add(lbl_inputLayerHeight);
		panel_inputLayerHeight.add(spinner_inputLayerHeight);

		JLabel lbl_hiddenLayerHeight = new JLabel("hiddenLayerHeight");
		lbl_hiddenLayerHeight.setToolTipText("toolTipText");

		JSpinner spinner_hiddenLayerHeight = new JSpinner();
		spinner_hiddenLayerHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_hiddenLayerHeight.getModel();

				int value = (int) numberModel.getValue();
				ConfigSingleton.INSTANCE.hiddenLayerHeight = value;

			}
		});

		spinner_hiddenLayerHeight.setPreferredSize(new Dimension(100, 20));
		spinner_hiddenLayerHeight.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.hiddenLayerHeight, 0, 2147483647, 1));

		JPanel panel_hiddenLayerHeight = new JPanel();
		panel_hiddenLayerHeight.add(lbl_hiddenLayerHeight);
		panel_hiddenLayerHeight.add(spinner_hiddenLayerHeight);

		neuralNetworkPanel.add(panel_hiddenLayerHeight);
		neuralNetworkPanel.add(panel_inputLayerHeight);
		neuralNetworkPanel.add(panel_brainWidth);
		
		/**
		 * Here the foodConsumption tab and its components are constructed:
		 */
		
		JPanel foodConsumptionPanel = new JPanel();
		tabbedPane_1.addTab("Food Consumption", null, foodConsumptionPanel, null);
		foodConsumptionPanel.setLayout(new BoxLayout(foodConsumptionPanel, BoxLayout.Y_AXIS));
		
		JLabel lbl_startingFat = new JLabel("startingFat");
		lbl_startingFat.setToolTipText("toolTipText");

		JSpinner spinner_startingFat = new JSpinner();
		spinner_startingFat.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_startingFat.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.startingFat = value;

			}
		});

		spinner_startingFat.setPreferredSize(new Dimension(100, 20));
		spinner_startingFat.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.startingFat, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.startingFat / 10));

		JPanel panel_startingFat = new JPanel();
		panel_startingFat.add(lbl_startingFat);
		panel_startingFat.add(spinner_startingFat);

		JLabel lbl_maxFoodInMouth = new JLabel("maxFoodInMouth");
		lbl_maxFoodInMouth.setToolTipText("toolTipText");

		JSpinner spinner_maxFoodInMouth = new JSpinner();
		spinner_maxFoodInMouth.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_maxFoodInMouth.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.maxFoodInMouth = value;

			}
		});

		spinner_maxFoodInMouth.setPreferredSize(new Dimension(100, 20));
		spinner_maxFoodInMouth.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.maxFoodInMouth, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.maxFoodInMouth / 10));

		JPanel panel_maxFoodInMouth = new JPanel();
		panel_maxFoodInMouth.add(lbl_maxFoodInMouth);
		panel_maxFoodInMouth.add(spinner_maxFoodInMouth);

		JLabel lbl_eatEfficiencySteepness = new JLabel("eatEfficiencySteepness");
		lbl_eatEfficiencySteepness.setToolTipText("toolTipText");

		JSpinner spinner_eatEfficiencySteepness = new JSpinner();
		spinner_eatEfficiencySteepness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_eatEfficiencySteepness.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.eatEfficiencySteepness = value;

			}
		});

		spinner_eatEfficiencySteepness.setPreferredSize(new Dimension(100, 20));
		spinner_eatEfficiencySteepness.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.eatEfficiencySteepness, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.eatEfficiencySteepness / 10));

		JPanel panel_eatEfficiencySteepness = new JPanel();
		panel_eatEfficiencySteepness.add(lbl_eatEfficiencySteepness);
		panel_eatEfficiencySteepness.add(spinner_eatEfficiencySteepness);

		JLabel lbl_weightPerFat = new JLabel("weightPerFat");
		lbl_weightPerFat.setToolTipText("toolTipText");

		JSpinner spinner_weightPerFat = new JSpinner();
		spinner_weightPerFat.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_weightPerFat.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.weightPerFat = value;

			}
		});

		spinner_weightPerFat.setPreferredSize(new Dimension(100, 20));
		spinner_weightPerFat.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.weightPerFat, 0.0,
				Integer.MAX_VALUE, 0.001));

		JPanel panel_weightPerFat = new JPanel();
		panel_weightPerFat.add(lbl_weightPerFat);
		panel_weightPerFat.add(spinner_weightPerFat);

		JLabel lbl_baseFatConsumption = new JLabel("baseFatConsumption");
		lbl_baseFatConsumption.setToolTipText("toolTipText");

		JSpinner spinner_baseFatConsumption = new JSpinner();
		spinner_baseFatConsumption.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_baseFatConsumption.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.baseFatConsumption = value;

			}
		});

		spinner_baseFatConsumption.setPreferredSize(new Dimension(100, 20));
		spinner_baseFatConsumption.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.baseFatConsumption, 0.0,
				Integer.MAX_VALUE, ConfigSingleton.INSTANCE.baseFatConsumption / 10));

		JPanel panel_baseFatConsumption = new JPanel();
		panel_baseFatConsumption.add(lbl_baseFatConsumption);
		panel_baseFatConsumption.add(spinner_baseFatConsumption);

		JLabel lbl_agePerStep = new JLabel("agePerStep");
		lbl_agePerStep.setToolTipText("toolTipText");

		JSpinner spinner_agePerStep = new JSpinner();
		spinner_agePerStep.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				SpinnerModel numberModel = spinner_agePerStep.getModel();

				double value = (Double) numberModel.getValue();
				ConfigSingleton.INSTANCE.agePerStep = value;

			}
		});

		spinner_agePerStep.setPreferredSize(new Dimension(100, 20));
		spinner_agePerStep.setModel(new SpinnerNumberModel(ConfigSingleton.INSTANCE.agePerStep, 0.0,
				Integer.MAX_VALUE, 0.001));

		JPanel panel_agePerStep = new JPanel();
		panel_agePerStep.add(lbl_agePerStep);
		panel_agePerStep.add(spinner_agePerStep);

		foodConsumptionPanel.add(panel_startingFat);
		foodConsumptionPanel.add(panel_maxFoodInMouth);
		foodConsumptionPanel.add(panel_eatEfficiencySteepness);
		foodConsumptionPanel.add(panel_weightPerFat);
		foodConsumptionPanel.add(panel_baseFatConsumption);
		foodConsumptionPanel.add(panel_agePerStep);

		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 3;
		gbc_panel_2.gridy = 0;
		getContentPane().add(panel_2, gbc_panel_2);

		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						setVisible(false);
						dispose();
						try {
							ConfigSingleton.INSTANCE.finalizeVariables();
							new DARWIN().setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
			}
		});
		panel_2.add(btnStart);
	}

}
