package com.LuDik.EvoAI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class DARWINAutoTester {
	
	private DARWINAutoTester dat;
	private DARWIN darwin;
	private Board board;
	private TimeKeeper timeKeeper;

	private long stepsPerRun = 10000;
	private long stepsPerImage = 5000;
	private int runsCompleted = 0;
	private int runsToComplete = 10;

	private File mainDirectory;
	private File currentTestDirectory;

	private File currentRunDirectory;
	
	private File currentImageDirectory;
	private File currentGraphDirectory;
	
	
	public DARWINAutoTester(){
		mainDirectory = new File("C:\\Users\\Luuk\\Desktop\\DARWIN_data\\DATv1");
		mainDirectory.mkdirs();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
		
		currentTestDirectory = new File(mainDirectory.getPath() + File.separator + "test" + dateFormat.format(date));
		currentTestDirectory.mkdirs();
		dat = this;
		startRun();		
		
		

	}

	private void startDARWIN() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					darwin = new DARWIN();
					darwin.setVisible(true);
					board = new Board(darwin);
					darwin.getInfoPanel().setBoard(board);
					darwin.getCameraPanel().setDisplayBoard(false);
					
					configureActionPanel();
					
					timeKeeper = board.getTimeKeeper();
					timeKeeper.setDARWINAutoTester(dat);
					timeKeeper.setDelay(0);
					timeKeeper.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	

	private void configureActionPanel() {
		ActionPanel ap = darwin.getActionPanel();
		ap.setInfoPanel(darwin.getInfoPanel());
		ap.setCameraPanel(darwin.getCameraPanel());
		ap.setTimeKeeper(timeKeeper);
		ap.setDisplayBoard(false);
		ap.getDisplayBoardBtn().setLabel("displayBoard: " + false);
		ap.getStartBoardBtn().setEnabled(false);
		
		ap.getDelaySlider().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (source.getValueIsAdjusting()) {
					if (timeKeeper != null) {
						timeKeeper.setDelay((int) source.getValue());
						ap.getDelaySliderLbl().setText("Delay: " + ap.getDelaySlider().getValue() + " ms " + "Fps: "
								+ (int) (1000 / Math.max(ap.getDelaySlider().getValue(), Double.MIN_VALUE)));
					}
				}
			}

		});
		
		ap.getFollowCrtrBtn().setEnabled(true);
		ap.getShowBrainBtn().setEnabled(true);
		ap.getPauseBtn().setEnabled(true);
		
		ap.getPauseBtn().removeActionListener(ap.getPauseBtn().getActionListeners()[0]);
		ap.getPauseBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				ap.setPaused(!ap.isPaused());

				synchronized (timeKeeper) {
					timeKeeper.setPaused(ap.isPaused());
				}

				if (!ap.isPaused()) {
					synchronized (timeKeeper) {
						timeKeeper.notify();
					}
				}

				ap.getPauseBtn().setLabel("Paused: " + ap.isPaused());

			};
		});
		
	}

	public void doStep(long currentStep) {
		if (currentStep % stepsPerImage == 0) 
			Saver.savePNG(darwin.getCameraPanel().getBufferedImageOfBoard(),
				currentImageDirectory.getPath(), "image" + currentStep);
		if (currentStep >= stepsPerRun) endRun();
		
	}
	
	private void startRun() {
		currentRunDirectory = new File(currentTestDirectory.getPath() + File.separator + "run" + (runsCompleted + 1));
		currentRunDirectory.mkdirs();
		
		currentImageDirectory = new File(currentRunDirectory.getPath() + File.separator + "images");
		currentImageDirectory.mkdirs();
		
		currentGraphDirectory = new File(currentRunDirectory.getPath() + File.separator + "graphs");
		currentGraphDirectory.mkdirs();

		Saver.savePathObject(new CheatyConfig(), currentRunDirectory.getPath(), "ConfigSingleton");

		
		startDARWIN();
	}

	private void endRun() {
		
		Saver.savePathObject(new CheatyConfig(), currentRunDirectory.getPath(), "ConfigSingleton");
		
		for(LineChartPanel lcp : LineChartPanel.getLineChartPanels()) {
			Saver.savePNG(lcp.getBufferedImageOfChart(800, 400), currentGraphDirectory.getPath(), lcp.getChartTitle());
		}
		
		runsCompleted++;
		
		darwin.setVisible(false);
		darwin.dispose();
		darwin = null;
		board = null;
		timeKeeper.terminate();
		timeKeeper = null;
		
		if(runsCompleted < runsToComplete) {
			ConfigSingleton.INSTANCE.maxFood += -10;
			startRun();
		}
		
		
	}
	
	public static void main(String[] args) {
		new DARWINAutoTester();
	}
	
}
