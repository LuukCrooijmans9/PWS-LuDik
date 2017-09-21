package com.LuDik.EvoAI;

public class TimeKeeper implements Runnable {

	private Board board;
	private Thread timeKeeper;
	private long step = 0;
	private long delay = 25;
	private InfoPanel infoPanel;
	
	private boolean paused;

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

		while (true) {
			
			beforeTime = System.nanoTime();
			
			makeStep();

			timeDiff = (long) (System.nanoTime() - beforeTime) / (long) Math.pow(10, 6) ;
			sleep = delay - timeDiff;
			
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
			
			if (paused) {
				try {
					synchronized(this) {
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			
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

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

}
