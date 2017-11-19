package com.LuDik.EvoAI;

/**
 * An instance of this class keeps track of time. It notifies its board object each time a certain period of time has passed.
 * This period of time is determined by the delay variable and can be modified during code execution. 
 *
 */

public class TimeKeeper implements Runnable {

	private Board board; // an instance of board this timeKeeper belongs to
	private Thread timeKeeper;
	private long step = 0; // a counter for how many times the delay has passed and the c
	private long delay = 25; // the amount of time in ms (miliseconds) between two steps
	private long timeDiff; // the time that has passed during code execution in ms, the amount of time the thread has to wait
	private InfoPanel infoPanel; // the infoPanel GUI object this timeKeeper notifies.
	
	private boolean paused; // if this timekeeper is paused or not
	private long timeDiffNano;

	public TimeKeeper(Board board) {	

		this.board = board;
		timeKeeper = new Thread(this);
		
		
		
	}

	public void start() {
		timeKeeper.start();
	}

	/**
	 * This method notifies the board and the infoPanel object so that they execute their code.
	 */
	
	private void makeStep() {
		board.doStep();
		getInfoPanel().update();
	}

	@Override
	public void run() {

		long beforeTime, sleep;

		/**
		 * The entire simulation is executed from within this while loop:
		 */
		
		while (true) {
			
			beforeTime = System.nanoTime();
			
			makeStep();

			timeDiffNano = System.nanoTime() - beforeTime;
			timeDiff = (long) timeDiffNano / (long) Math.pow(10, 6) ; //calculates timeDiff and converts it to ms
			sleep = delay - timeDiff;
			


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

	public long getTimeDiff() {
		return timeDiff;
	}

	public long getTimeDiffNano() {
		return timeDiffNano;
	}

	

}
