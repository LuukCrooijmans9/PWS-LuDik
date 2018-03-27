package com.LuDik.EvoAI;

/**
 * An instance of this class keeps track of time. It notifies its board object
 * each time a certain period of time has passed. This period of time is
 * determined by the delay variable and can be modified during code execution.
 *
 */

public class TimeKeeper implements Runnable {

	private Board board; // an instance of board this timeKeeper belongs to
	private Thread timeKeeper;
	private long step = 0; // a counter for how many times the delay has passed and the c
	private long delay = 25; // the amount of time in ms (miliseconds) between two steps
	private long timeDiff; // the time that has passed during code execution in ms, the amount of time the
							// thread has to wait
	private InfoPanel infoPanel; // the infoPanel GUI object this timeKeeper notifies.

	private boolean paused; // if this timekeeper is paused or not
	private boolean saving;
	private boolean running;
	private long timeDiffNano;

	private DARWINAutoTester DARWINAutoTester;
	

	public TimeKeeper(Board board) {

		this.board = board;
		timeKeeper = new Thread(this);

	}

	public void start() {
		running = true;
		timeKeeper.start();
	}
	
	public void terminate() {
		paused = false;
		running = false;
	}

	/**
	 * This method notifies the board and the infoPanel object and, if it exist, the
	 * DARWINAutoTester so that they execute their code.
	 */

	private void makeStep() {
		board.doStep();
		getInfoPanel().update();
		if (step % ConfigSingleton.INSTANCE.periodLength == 0) {
			board.doStatistics(step);
		}
		
		if (DARWINAutoTester != null) {
			DARWINAutoTester.doStep(step);
		}
		
	}

	@Override
	public void run() {

		long beforeTime, sleep;

		/**
		 * The entire simulation is executed from within this while loop:
		 */

		while (running) {

			beforeTime = System.nanoTime();

			makeStep();

			timeDiffNano = System.nanoTime() - beforeTime;
			timeDiff = (long) timeDiffNano / (long) Math.pow(10, 6); // calculates timeDiff and converts it to ms
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
			
			if(saving) {
				board.saveSimulation();
				saving = false;
			}

			if (paused) {
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				Saver.saveObject(board, "Board", "Board");
//				Saver.saveObject(board.getLivingCreatures().get(0), "Creatures", 0+"_Creature");
//				Saver.saveObject(board.getLivingCreatures().get(0).getBrain(), "Creatures", 0 + "_Brain");

			}

		}
		System.out.println("Thread shutting down");
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

	public boolean isSaving() {
		return saving;
	}

	public void setSaving(boolean saving) {
		this.saving = saving;
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

	public DARWINAutoTester getDARWINAutoTester() {
		return DARWINAutoTester;
	}

	public void setDARWINAutoTester(DARWINAutoTester dARWINAutoTester) {
		DARWINAutoTester = dARWINAutoTester;
	}

}
