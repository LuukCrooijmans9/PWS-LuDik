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
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// UI containers:
	private EvoAI mainFrame;
	private CameraPanel cameraPanel;
	private InfoPanel infoPanel;

	private Board board;
	private TimeKeeper timeKeeper;

	private static int APHeight;
	private static final int APWidth = 400;

	private Button startBoardBtn;
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




	public ActionPanel(EvoAI parent) {
		initActionPanel(parent);
	}

	private void initActionPanel(EvoAI parent) {
		APHeight = parent.getCameraPanel().getCPHEIGHT();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(APWidth, APHeight));

		mainFrame = parent;
		
		delaySlider = new JSlider(0, 200, 25);
		delaySlider.setPaintTicks(true);
		delaySlider.setPaintLabels(true);
		delaySlider.setMajorTickSpacing(20);		
		
		delaySliderLbl = new JLabel("Delay: " + delaySlider.getValue() + " ms Fps: " + 1000/delaySlider.getValue());

		paused = false;
		startBoardBtn = new Button("Start board");

		pauseBtn = new Button("Paused: " + paused);
		pauseBtn.setEnabled(false);
		
		displayBoard = true;
		displayBoardBtn = new Button("DisplayBoard: " + displayBoard);
		

		followCrtr = false;
		followCrtrBtn = new Button("followCreature: " + followCrtr);
		followCrtrBtn.setEnabled(false);
		
		controlCrtrBtn = new Button("controlCreature: " + isControlCrtr());
		controlCrtrBtn.setEnabled(false);
		setControlCrtr(false);
		
		add(delaySliderLbl);
		add(delaySlider);
		
		add(startBoardBtn);
		add(pauseBtn);
		add(displayBoardBtn);
		add(followCrtrBtn);
		add(controlCrtrBtn);
		
		

		startBoardBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				infoPanel = mainFrame.getInfoPanel();
				cameraPanel = mainFrame.getCameraPanel();
				
				if (timeKeeper != null) {
					timeKeeper = null;
				}
				
			
					board = new Board(
							Configuration.DEFAULT_TILE_SIZE,
							Configuration.DEFAULT_MAP_SIZE_IN_TILES,
							Configuration.DEFAULT_SEED,
							Configuration.DEFAULT_SMOOTHNESS,
							mainFrame
							);

				
				infoPanel.setBoard(board);
				
				board.spawnFirstCreatures();
				cameraPanel.update();
				
				timeKeeper = board.getTimeKeeper();				
				timeKeeper.start();
				
				pauseBtn.setEnabled(true);


			};
		});
		
		displayBoardBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				displayBoard = !displayBoard;	
				
				cameraPanel.setDisplayBoard(displayBoard);
				displayBoardBtn.setLabel("displayBoard: " + displayBoard);
				
				
				
			};
		});
		
		pauseBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				paused = !paused;
				
				synchronized(timeKeeper) {
					timeKeeper.setPaused(paused);
				}
				
				if(!paused) {
					synchronized(timeKeeper) {
						timeKeeper.notify();
					}
				}
				
				pauseBtn.setLabel("Paused: " + paused);
				
				
			};
		});
		
		followCrtrBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				followCrtr = !followCrtr;
				
				cameraPanel.setFollowSelectedCreature(followCrtr);
				
				followCrtrBtn.setLabel("followCreature: " + followCrtr);
				
				
			};
		});
		
		controlCrtrBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				setControlCrtr(!isControlCrtr());
				
				cameraPanel.setControlCrtr(isControlCrtr());
				
				controlCrtrBtn.setLabel("controlCreature: " + isControlCrtr());
				
				
			};
		});
		
		delaySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    if (source.getValueIsAdjusting()) {
			    	timeKeeper.setDelay((int) source.getValue());
			    	delaySliderLbl.setText("Delay: " + delaySlider.getValue() + " ms "
			    			+ "Fps: " + (int) (1000/Math.max(delaySlider.getValue(), Double.MIN_VALUE)));
			    }
			}
			
		});
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
