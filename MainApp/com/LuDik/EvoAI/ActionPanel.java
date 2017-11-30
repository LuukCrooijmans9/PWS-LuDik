package com.LuDik.EvoAI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class is the panel that contains all the interactive graphical
 * components that interact with EvoAI or its components. Some examples of
 * interactive graphical components are buttons, sliders, textfields, etc.
 * 
 *
 */

public class ActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// These contants determine the behaviour and the appearance of the delaySlider
	private static final int DELAY_SLIDER_STARTVALUE = 25;
	private static final int DELAY_SLIDER_MINVALUE = 0;
	private static final int DELAY_SLIDER_MAXVALUE = 200;
	private static final int DELAY_SLIDER_TICKINGSPACE = 20;

	// The other main graphical containers:
	private DARWIN mainFrame;
	private CameraPanel cameraPanel;
	private InfoPanel infoPanel;

	private BrainVisualisedWindow brainDialog;

	private Board board;
	private TimeKeeper timeKeeper;

	private static int APHeight = CameraPanel.getCPHEIGHT();
	private static final int APWidth = 400;

	private Button startBoardBtn;
	private Button saveBtn;
	private Button pauseBtn;
	private Button followCrtrBtn;
	private Button controlCrtrBtn;
	private Button displayBoardBtn;

	private JSlider delaySlider;
	private JLabel delaySliderLbl;

	private boolean paused;
	private boolean followCrtr;
	private boolean controlCrtr;

	private boolean displayBoard;

	private Button showBrainBtn;

	public ActionPanel(DARWIN parent) {
		initActionPanel(parent);
	}

	private void initActionPanel(DARWIN evoAI) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(APWidth, APHeight));
		mainFrame = evoAI;

		/**
		 * The code below creates a slider with which the delay in ms of the timeKeeper
		 * can be adjusted during runtime.
		 */

		delaySlider = new JSlider(DELAY_SLIDER_MINVALUE, DELAY_SLIDER_MAXVALUE, DELAY_SLIDER_STARTVALUE);
		delaySlider.setPaintTicks(true);
		delaySlider.setPaintLabels(true);
		delaySlider.setMajorTickSpacing(DELAY_SLIDER_TICKINGSPACE);

		delaySliderLbl = new JLabel("Delay: " + delaySlider.getValue() + " ms Fps: " + 1000 / delaySlider.getValue());

		delaySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (source.getValueIsAdjusting()) {
					if (timeKeeper != null) {
						timeKeeper.setDelay((int) source.getValue());
						delaySliderLbl.setText("Delay: " + delaySlider.getValue() + " ms " + "Fps: "
								+ (int) (1000 / Math.max(delaySlider.getValue(), Double.MIN_VALUE)));
					}
				}
			}

		});

		/**
		 * The code below creates a button with which the simulation of the board can be
		 * started.
		 */

		startBoardBtn = new Button("Start board");

		startBoardBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				infoPanel = mainFrame.getInfoPanel();
				cameraPanel = mainFrame.getCameraPanel();
				showBrainBtn.setEnabled(true);

				if (timeKeeper != null) {
					timeKeeper = null;
				}

				board = new Board(mainFrame);

				infoPanel.setBoard(board);

				board.spawnFirstCreatures();
				cameraPanel.update();

				timeKeeper = board.getTimeKeeper();
				timeKeeper.start();

				pauseBtn.setEnabled(true);
				saveBtn.setEnabled(true);

			};
		});
		
		saveBtn = new Button("Save");

		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {


				synchronized (timeKeeper) {
					timeKeeper.setSaving(true);
				}

			};
		});

		/**
		 * The code below creates a button with which the simulation of the board can be
		 * paused and resumed.
		 */

		paused = false;
		pauseBtn = new Button("Paused: " + paused);
		pauseBtn.setEnabled(false);

		pauseBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				paused = !paused;

				synchronized (timeKeeper) {
					timeKeeper.setPaused(paused);
				}

				if (!paused) {
					synchronized (timeKeeper) {
						timeKeeper.notify();
					}
				}

				pauseBtn.setLabel("Paused: " + paused);

			};
		});

		/**
		 * The code below creates a button with which the drawing of the board can be
		 * stopped and resumed.
		 */

		displayBoard = true;
		displayBoardBtn = new Button("DisplayBoard: " + displayBoard);

		displayBoardBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				displayBoard = !displayBoard;

				cameraPanel.setDisplayBoard(displayBoard);
				displayBoardBtn.setLabel("displayBoard: " + displayBoard);

			};

		});
		/**
		 * The code below creates a button that determines if the cameraPanel follows
		 * the selected creature.
		 */

		followCrtr = false;
		followCrtrBtn = new Button("followCreature: " + followCrtr);
		followCrtrBtn.setEnabled(false);

		followCrtrBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				followCrtr = !followCrtr;

				cameraPanel.setFollowSelectedCreature(followCrtr);

				followCrtrBtn.setLabel("followCreature: " + followCrtr);

			};
		});

		/**
		 * The code below creates a button that determines if the selected creature can
		 * be controlled by the user using keyboard inputs.
		 */

		controlCrtrBtn = new Button("controlCreature: " + isControlCrtr());
		controlCrtrBtn.setEnabled(false);
		setControlCrtr(false);

		controlCrtrBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				setControlCrtr(!isControlCrtr());

				cameraPanel.setControlCrtr(isControlCrtr());

				controlCrtrBtn.setLabel("controlCreature: " + isControlCrtr());

			};
		});

		/**
		 * The code below creates a butten that opens a window that visually represents
		 * the brain, the neural network of the selected creature.
		 */

		showBrainBtn = new Button("show brain" );
		showBrainBtn.setEnabled(false);

		showBrainBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				
				if (brainDialog == null || !brainDialog.isVisible()) {
					System.out.println("test: " + brainDialog);
					try {
						brainDialog = new BrainVisualisedWindow(cameraPanel);
						brainDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						brainDialog.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					
				}
			};
		});

		/**
		 * The code below adds the buttons and sliders to the actionPanel in a specific
		 * order.
		 */

		add(delaySliderLbl);
		add(delaySlider);

		add(startBoardBtn);
		add(pauseBtn);
		add(saveBtn);
		add(displayBoardBtn);
		add(followCrtrBtn);
		add(controlCrtrBtn);
		add(showBrainBtn);
	}

	public BrainVisualisedWindow getBrainDialog() {
		return brainDialog;
	}

	public TimeKeeper getTimeKeeper() {

		return timeKeeper;
	}

	public Board getBoard() {
		return board;
	}

	public void setFollowCrtrBtnEnabled(boolean bool) {
		followCrtrBtn.setEnabled(bool);
	}

	public void setControlCrtrBtnEnabled(boolean bool) {
		controlCrtrBtn.setEnabled(bool);
	}

	public boolean isControlCrtr() {
		return controlCrtr;
	}

	public void setControlCrtr(boolean controlCrtr) {
		this.controlCrtr = controlCrtr;
		controlCrtrBtn.setLabel("controlCreature: " + isControlCrtr());
	}
}
