package com.simpleEnviroNN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


import javax.swing.JPanel;

  class Board extends JPanel implements Runnable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int BWidth = 500;
	private static final int BHeight = 500;

	InfoPanel infoPanel;
	
	private Thread timer;
	private long DELAY = 0;
		
	private boolean graphicalMode = true;
	private long step;
	double totTimeAverage, totTime = 0;
	
	
//	private Shape biggestRect = new Rectangle2D.Double(0d, 0d, 
//			(double) BWidth - 1, (double) BHeight - 1);
	

	  Board() {

		initBoard();
	}

	  void initBoard() {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(BWidth, BHeight));		
		
		
	}
	
	private ArrayList<double[]> getStartPositions(int width, int height) {
		double crtrSize = Creature.getCrtrSize(); 
		double widthMax = (int) (width / crtrSize);
		double heightMax = (int) (height / crtrSize);
		
		int maxCreatures = (int) (widthMax * heightMax);
		
		if (totalCreatures > maxCreatures || totalCreatures <= 0) {
			totalCreatures = maxCreatures;
		}
		
		System.out.println("widthMax" + widthMax);
		System.out.println("heightMax" + heightMax);
		
		ArrayList<double[]> startPositions = new ArrayList<double[]>();
//		double[] tempCont = new double[2];
		
		
		for (int i = 0; i < widthMax; i++) {
			for (int k = 0; k < heightMax; k++) {
//				tempCont[0] = (((double)i) + 0.5d) * crtrSize;
//				tempCont[1] = (((double)k) + 0.5d) * crtrSize;
//				System.out.println("tempcont x at k and i= " + k + ","+ i + ","+ tempCont[0]);
//				System.out.println("tempcont y at k and i= " + k + ","+ i + ","+ tempCont[1]);

				startPositions.add(new double[]{ (((double)i) + 0.5d) * crtrSize, (((double)k) + 0.5d) * crtrSize});
				
				
//				System.out.println("startPositions" + startPositions);
			}
		}
		
		
		return startPositions;
	}

	@Override
	  void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		doDrawing(g);
		
	}
	
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		

		
//		Area a1 = new Area(biggestRect);
		
//		g2d.drawRect(0, 0, BWidth - 1, BHeight - 1);
		
		g2d.setColor(Color.BLACK);
		
	}

	private void cycle() {
		
		for (Creature crtr : allCreatures) {
			crtr.notifyTimePassed(DELAY);
		}
		
		infoPanel.notifyTimePassed();

	}

	  void start() {
		timer = new Thread(this);
		timer.start();
	}

	@Override
	  void run() {

		long beforeTime, timeDiff, sleep;
		
		
		
		beforeTime = System.nanoTime();

		while (true) {

			cycle();
			
			if (graphicalMode) {
				repaint();
			}
			
			timeDiff = (long) (System.nanoTime() - beforeTime) / (long) Math.pow(10, 6) ;
//			sleep = DELAY - timeDiff;
			sleep = DELAY ;

			if (sleep < 0) {
				sleep = 0;
			}

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("Interrupted: " + e.getMessage());
			}
			step++;
			totTime = totTime + timeDiff;
			totTimeAverage = totTime / step;
			beforeTime = System.nanoTime();
		}
	}
	
	  void setInfoPanel(InfoPanel iPanel) {
		infoPanel = iPanel;
	}
	
	

	static int getBHeight() {
		return BHeight;
	}

	static int getBWidth() {
		return BWidth;
	}
	
	  ArrayList<Creature> getAllCreatures() {
		return allCreatures;
	}

	  int getTotalCreatures() {
		return totalCreatures;
	}

	  void setTotalCreatures(int totalCreatures) {
		this.totalCreatures = totalCreatures;
	}

	  long getStep() {
		return step;
	}

	  void setStep(long step) {
		this.step = step;
	}
	  long getDelay() {
		return DELAY;
	}

	  void setDelay(long dELAY) {
		DELAY = dELAY;
	}

	  boolean isGraphicalMode() {
		return graphicalMode;
	}

	  void setGraphicalMode(boolean graphicalMode) {
		this.graphicalMode = graphicalMode;
	}
	  double getTotTimeAverage() {
		return totTimeAverage;
	}

	  void setTotTimeAverage(double totTimeAverage) {
		this.totTimeAverage = totTimeAverage;
	}

}

