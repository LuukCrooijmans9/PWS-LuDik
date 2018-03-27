package com.LuDik.EvoAI;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class DARWINAutoTester {
	
	private long stepsPerRun = 5_000;
	private long stepsPerImage = 1000;
	private DARWIN darwin;
	private Board board;
	private TimeKeeper timeKeeper;
	private int runsCompleted = 0;
	private int runsToComplete = 3;

	private File mainDirectory;
	private File currentTestDirectory;

	private File currentRunDirectory;
	
	private File currentImageDirectory;
	private File currentGraphDirectory;
	
	
	public DARWINAutoTester(){
		mainDirectory = new File("C:\\Users\\Luuk\\Desktop\\DARWIN_data\\DATv1");
		mainDirectory.mkdirs();
		System.out.println("done shit " + mainDirectory.exists());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
		
		currentTestDirectory = new File(mainDirectory.getPath() + File.separator + "test" + dateFormat.format(date));
		currentTestDirectory.mkdirs();
		
		startRun();		
		
		

	}

	private void startDARWIN() {
		darwin = new DARWIN();
		darwin.setVisible(true);
		board = new Board(darwin);
		darwin.getInfoPanel().setBoard(board);
		timeKeeper = board.getTimeKeeper();
		timeKeeper.setDARWINAutoTester(this);
		timeKeeper.setDelay(0);
		timeKeeper.start();
	}
	

	public void doStep(long currentStep) {
		if (currentStep % stepsPerImage == 0) 
			Saver.savePNG(darwin.getCameraPanel().getBufferedImageOfBoard(),
				currentImageDirectory.getPath(), "image" + currentStep);
		if(currentStep % 10 == 0) System.out.println("the step is " + currentStep);
		if (currentStep >= stepsPerRun) endRun();
		
	}
	
	private void startRun() {
		currentRunDirectory = new File(currentTestDirectory.getPath() + File.separator + "run" + (runsCompleted + 1));
		currentRunDirectory.mkdirs();
		
		currentImageDirectory = new File(currentRunDirectory.getPath() + File.separator + "images");
		currentImageDirectory.mkdirs();
		
		currentGraphDirectory = new File(currentRunDirectory.getPath() + File.separator + "graphs");
		currentGraphDirectory.mkdirs();

		
		startDARWIN();
	}

	private void endRun() {
		Saver.savePathObject(ConfigSingleton.INSTANCE, currentRunDirectory.getPath(), "ConfigSingleton");
		
		for(LineChartPanel lcp : LineChartPanel.getLineChartPanels()) {
			Saver.savePNG(lcp.getBufferedImageOfChart(400, 200), currentGraphDirectory.getPath(), lcp.getChartTitle());
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
