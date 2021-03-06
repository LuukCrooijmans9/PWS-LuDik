package com.LuDik.EvoAI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * This class is the panel that contains all the interactive graphical
 * components that interact with EvoAI or its components. Some examples of
 * interactive graphical components are buttons, sliders, textfields, etc.
 * 
 *
 */

public class ActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// These constants determine the behaviour and the appearance of the delaySlider
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
	private static final int APWidth = 250;

	private Button startBoardBtn;
	private Button saveBtn;
	private Button loadBtn;
	private Button pauseBtn;
	private Button followCrtrBtn;
	private Button controlCrtrBtn;
	private Button displayBoardBtn;
	private Button showBrainBtn;
	private Button takeImageBtn;

	private JSlider delaySlider;
	private JLabel delaySliderLbl;

	private boolean paused;
	private boolean followCrtr;
	private boolean controlCrtr;

	private boolean displayBoard;

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

			boolean alreadyPressed = false;

			@Override
			public void actionPerformed(ActionEvent evt) {
				if (!alreadyPressed) {
					board = new Board(mainFrame);

					infoPanel = mainFrame.getInfoPanel();
					cameraPanel = mainFrame.getCameraPanel();
					showBrainBtn.setEnabled(true);

					if (timeKeeper != null) {
						timeKeeper = null;
					}

					infoPanel.setBoard(board);

					board.spawnFirstCreatures();
					cameraPanel.update();

					timeKeeper = board.getTimeKeeper();
					timeKeeper.start();

					pauseBtn.setEnabled(true);
					saveBtn.setEnabled(true);
					loadBtn.setEnabled(false);

					alreadyPressed = true;
					startBoardBtn.setLabel("Quit simulation and go to launcher");
					return;
				}
				
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
				
				synchronized (timeKeeper) {
					timeKeeper.terminate();
				}
				mainFrame.setVisible(false);
				mainFrame.dispose();

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

		saveBtn = new Button("Save");
		saveBtn.setEnabled(false);
		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {

				synchronized (timeKeeper) {
					timeKeeper.setSaving(true);
				}

			};
		});

		loadBtn = new Button("Load");

		loadBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {

				Gson gson = new Gson();
				Board brd = null;
				LocalDate localDate = LocalDate.now();
				String date = (DateTimeFormatter.ofPattern("dd.MM").format(localDate));
				try {
					brd = gson.fromJson(new FileReader("DARWINSAVE\\Board\\Board.json"), Board.class);
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonIOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (brd != null) {
					board = brd;
				} else {
					System.out.println("Couldn't Find File");
					return;
				}
				System.out.println(board);
				mainFrame.setBoard(board);

				infoPanel = mainFrame.getInfoPanel();
				cameraPanel = mainFrame.getCameraPanel();
				showBrainBtn.setEnabled(true);

				if (timeKeeper != null) {
					timeKeeper = null;
				}

				infoPanel.setBoard(board);

				board.reloadSimulation();
				board.loadBoard(mainFrame);
				cameraPanel.update();
				timeKeeper = board.getTimeKeeper();
				timeKeeper.start();

				pauseBtn.setEnabled(true);
				saveBtn.setEnabled(true);

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
		 * The code below creates a button that opens a window that visually represents
		 * the brain, the neural network of the selected creature.
		 */

		showBrainBtn = new Button("show brain");
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
		 * The code below creates a button that takes an image of the current state of
		 * board.
		 */

		takeImageBtn = new Button("take image of board");

		takeImageBtn.addActionListener(new ActionListener() {

			private int imagesTaken = 0;

			@Override
			public void actionPerformed(ActionEvent evt) {
				imagesTaken++;

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Please select a folder in which the image can be saved.");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

					BufferedImage buffImg = cameraPanel.getBufferedImageOfBoard();
					String imageName = "darwinimage" + imagesTaken;

					if (Saver.savePNG(buffImg, chooser.getSelectedFile().getPath(), imageName)) {
						JOptionPane.showMessageDialog(null, "Image successfully saved.", "Action successful",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Could not save image.", "Action unsuccessful",
								JOptionPane.INFORMATION_MESSAGE);
					}

				} else {
					System.out.println("No Selection ");
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
		add(loadBtn);
		add(displayBoardBtn);
		add(followCrtrBtn);
		add(controlCrtrBtn);
		add(showBrainBtn);
		add(takeImageBtn);
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

	public DARWIN getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(DARWIN mainFrame) {
		this.mainFrame = mainFrame;
	}

	public CameraPanel getCameraPanel() {
		return cameraPanel;
	}

	public void setCameraPanel(CameraPanel cameraPanel) {
		this.cameraPanel = cameraPanel;
	}

	public InfoPanel getInfoPanel() {
		return infoPanel;
	}

	public void setInfoPanel(InfoPanel infoPanel) {
		this.infoPanel = infoPanel;
	}

	public static int getAPHeight() {
		return APHeight;
	}

	public static void setAPHeight(int aPHeight) {
		APHeight = aPHeight;
	}

	public Button getStartBoardBtn() {
		return startBoardBtn;
	}

	public void setStartBoardBtn(Button startBoardBtn) {
		this.startBoardBtn = startBoardBtn;
	}

	public Button getSaveBtn() {
		return saveBtn;
	}

	public void setSaveBtn(Button saveBtn) {
		this.saveBtn = saveBtn;
	}

	public Button getLoadBtn() {
		return loadBtn;
	}

	public void setLoadBtn(Button loadBtn) {
		this.loadBtn = loadBtn;
	}

	public Button getPauseBtn() {
		return pauseBtn;
	}

	public void setPauseBtn(Button pauseBtn) {
		this.pauseBtn = pauseBtn;
	}

	public Button getFollowCrtrBtn() {
		return followCrtrBtn;
	}

	public void setFollowCrtrBtn(Button followCrtrBtn) {
		this.followCrtrBtn = followCrtrBtn;
	}

	public Button getControlCrtrBtn() {
		return controlCrtrBtn;
	}

	public void setControlCrtrBtn(Button controlCrtrBtn) {
		this.controlCrtrBtn = controlCrtrBtn;
	}

	public Button getDisplayBoardBtn() {
		return displayBoardBtn;
	}

	public void setDisplayBoardBtn(Button displayBoardBtn) {
		this.displayBoardBtn = displayBoardBtn;
	}

	public Button getShowBrainBtn() {
		return showBrainBtn;
	}

	public void setShowBrainBtn(Button showBrainBtn) {
		this.showBrainBtn = showBrainBtn;
	}

	public Button getTakeImageBtn() {
		return takeImageBtn;
	}

	public void setTakeImageBtn(Button takeImageBtn) {
		this.takeImageBtn = takeImageBtn;
	}

	public JSlider getDelaySlider() {
		return delaySlider;
	}

	public void setDelaySlider(JSlider delaySlider) {
		this.delaySlider = delaySlider;
	}

	public JLabel getDelaySliderLbl() {
		return delaySliderLbl;
	}

	public void setDelaySliderLbl(JLabel delaySliderLbl) {
		this.delaySliderLbl = delaySliderLbl;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isFollowCrtr() {
		return followCrtr;
	}

	public void setFollowCrtr(boolean followCrtr) {
		this.followCrtr = followCrtr;
	}

	public boolean isDisplayBoard() {
		return displayBoard;
	}

	public void setDisplayBoard(boolean displayBoard) {
		this.displayBoard = displayBoard;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getDelaySliderStartvalue() {
		return DELAY_SLIDER_STARTVALUE;
	}

	public static int getDelaySliderMinvalue() {
		return DELAY_SLIDER_MINVALUE;
	}

	public static int getDelaySliderMaxvalue() {
		return DELAY_SLIDER_MAXVALUE;
	}

	public static int getDelaySliderTickingspace() {
		return DELAY_SLIDER_TICKINGSPACE;
	}

	public static int getApwidth() {
		return APWidth;
	}

	public void setBrainDialog(BrainVisualisedWindow brainDialog) {
		this.brainDialog = brainDialog;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setTimeKeeper(TimeKeeper timeKeeper) {
		this.timeKeeper = timeKeeper;
	}
}
