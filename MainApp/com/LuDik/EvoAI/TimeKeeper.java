package com.LuDik.EvoAI;

public class TimeKeeper implements Runnable {

	private Board board;
	private Thread timeKeeper;
	private long step = 0;
	private long DELAY = 25;
	private InfoPanel infoPanel;

	public TimeKeeper(Board brd) {
		

		board = brd;
		timeKeeper = new Thread(this);
		
		
		
	}

	public void start() {
		timeKeeper.start();
	}

	private void makeStep() {
		board.updateStep();
		getInfoPanel().update();
	}

	@Override
	public void run() {

		long beforeTime, timeDiff, sleep;

		beforeTime = System.nanoTime();

		while (true) {

			makeStep();

			timeDiff = (long) (System.nanoTime() - beforeTime) / (long) Math.pow(10, 6) ;
			sleep = DELAY - timeDiff;
			
//			System.out.println("Sleep: " + sleep);


			if (sleep < 0) {
				sleep = 0;
			}

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("Interrupted: " + e.getMessage());
			}
			step++;

			beforeTime = System.nanoTime();
		}

	}

	public long getStep() {
		return step;
	}

	public InfoPanel getInfoPanel() {
		return infoPanel;
	}

	public void setInfoPanel(InfoPanel infoPanel) {
		this.infoPanel = infoPanel;
	}

}
