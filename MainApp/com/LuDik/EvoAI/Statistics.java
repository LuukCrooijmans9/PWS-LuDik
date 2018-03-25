package com.LuDik.EvoAI;

import java.util.ArrayList;

public class Statistics {
	
	private Board board;
	
	private long startTime;
	private long totalNumberOfCreatures;
	private long totalNumberOfRandomCreatures;
	private long totalNumberOfBornCreatures;
	private long totalFoodEaten;
	private long elapsedTime;


	public Statistics(Board board) {
		this.board = board;
		startTime = System.nanoTime();
	}
	
	public void periodUpdateStatistics(long periodNumberOfRandomCreatures, long periodNumberOfBornCreatures){
		totalNumberOfRandomCreatures += periodNumberOfRandomCreatures;
		totalNumberOfBornCreatures += periodNumberOfBornCreatures;
		totalNumberOfCreatures += periodNumberOfRandomCreatures + periodNumberOfBornCreatures;
		
	}
		

	public void endStats() {
		elapsedTime = startTime - System.nanoTime();
		
		this.board = null;
		
	}

}
