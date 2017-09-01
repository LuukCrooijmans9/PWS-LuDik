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

	private static int tileSize = 5;
	private static double smoothness = 0.1; // Lower is higher smoothness
	private double changeOverTime = 0.01;

	InfoPanel infoPanel;

	private Thread timer;
	private long DELAY = 200;

	private boolean graphicalMode = true;
	private long step;
	double totTimeAverage, totTime = 0;

	// private Shape biggestRect = new Rectangle2D.Double(0d, 0d,
	// (double) BWidth - 1, (double) BHeight - 1);

	public Board() {

		initBoard();
	}

	void initBoard() {
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(BWidth, BHeight));

	}

	public void start() {
		timer = new Thread(this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);

	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		changeOverTime = changeOverTime + 0.01;
		double seed = Math.random() * 255;
		for (int i = 0; i < 100; i++) {
			for (int k = 0; k < 100; k++) {
				
				
				float perlin = (float) 5f * ((float) ImprovedNoise.noise((float) i * smoothness, (float) k * smoothness,
						seed));
				perlin = (float) (1 / (2 + Math.expm1(-perlin)));
				if (k + i * 100 == 5625) {
					System.out.println(perlin);
				}
				g2d.setColor(new Color(perlin, 0f, 0f));
				
				// System.out.println(ImprovedNoise.noise(i, k, math.random));
				// g2d.setColor(new Color((float) ImprovedNoise.noise((i * 0.01) * smoothness,
				// (k * 0.01) * smoothness, Math.random()) / 2f + 0.5f, 0, 0, 1));
				// if (ImprovedNoise.noise((i * smoothness) * smoothness, (k * 0.01) *
				// smoothness, Math.random()) > 1) {
				// System.out.println(ImprovedNoise.noise(i * smoothness, k * smoothness,
				// Math.random()));
				// }
				g2d.fillRect(i * tileSize, k * tileSize, tileSize, tileSize);
				g2d.setColor(Color.BLUE);

				// g2d.drawRect(i * tileSize, k * tileSize, tileSize, tileSize);
			}
		}

		// Area a1 = new Area(biggestRect);

		// g2d.drawRect(0, 0, BWidth - 1, BHeight - 1);

		g2d.setColor(Color.BLACK);

	}

	@Override
	public void run() {

		long beforeTime, timeDiff, sleep;

		beforeTime = System.nanoTime();

		while (true) {

			if (graphicalMode) {
				repaint();
			}

			timeDiff = (long) (System.nanoTime() - beforeTime) / (long) Math.pow(10, 6);
			// sleep = DELAY - timeDiff;
			sleep = DELAY;

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
