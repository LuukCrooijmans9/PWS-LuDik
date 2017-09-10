package com.LuDik.EvoAI;

public class TimeKeeper implements Runnable {
	
	private Board board;
	private Thread timeKeeper;
	private long step;
	private long DELAY = 2500;
	private boolean paused;
	
	
	public TimeKeeper(Board brd) {
		board = brd;
		timeKeeper = new Thread(this);
	}
	
	public void start() {
		paused = false;
		timeKeeper.start();
	}

	private void makeStep() {
		board.updateStep();
	}
	
	@Override
	public void run() {
		
		long beforeTime, timeDiff, sleep;
		
		while (true) {
			
			beforeTime = System.nanoTime();

			while (true) {

				makeStep();
				System.out.println("successful");
				
				
				timeDiff = System.nanoTime() - beforeTime;
				sleep = DELAY - timeDiff;
				
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
		
		
	}
	
}

