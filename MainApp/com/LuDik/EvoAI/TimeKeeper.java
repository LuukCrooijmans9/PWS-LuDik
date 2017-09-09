package com.LuDik.EvoAI;

public class TimeKeeper implements Runnable {
	
	private Board board;
	private Thread timeKeeper;
	private long step;
	private long DELAY;
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
		
	}
	
	@Override
	public void run() {
		
		while (true) {
			
			long beforeTime, timeDiff, sleep;
			
			
			
			beforeTime = System.nanoTime();

			while (true) {

				makeStep();
				
				
				
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

