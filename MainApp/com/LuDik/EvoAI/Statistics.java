package com.LuDik.EvoAI;

import java.util.ArrayList;

public class Statistics {
	
	private Board board;
	
	private long startTime;
	private long totalCreatures;
	private long totalCreaturesSpawned;
	private long totalCreaturesBorn;
	private long totalFoodEaten;
	private long elapsedTime;
	
	private ArrayList<Creature> deadCreaturesOfCurrentPeriod;

	private ArrayList<Float> avgFitnessArray;
	private ArrayList<Float> avgAgeArray;
	private ArrayList<Float> avgTotalDistanceTravelledArray;
	private ArrayList<Float> avgNettoDistanceTravelledArray;
	private ArrayList<Float> avgTotalFoodEatenArray;

	private ArrayList<Integer> creaturesBornArray;
	private ArrayList<Integer> creaturesSpawnedArray;


	public Statistics(Board board) {
		this.board = board;
		startTime = System.nanoTime();
		
		/**
		 * ArrayList initialization:
		 */
		
		deadCreaturesOfCurrentPeriod = new ArrayList<Creature>();
		
		avgFitnessArray = new ArrayList<Float>();
		avgAgeArray = new ArrayList<Float>();
		avgTotalDistanceTravelledArray = new ArrayList<Float>();
		avgNettoDistanceTravelledArray = new ArrayList<Float>();
		avgTotalFoodEatenArray = new ArrayList<Float>();
		
		creaturesBornArray = new ArrayList<Integer>();
		creaturesSpawnedArray = new ArrayList<Integer>();
		
	}
	
	
	public void update() {
		
		/**
		 * Calculation and updating of averages of creatures:
		 */
		
		avgFitnessArray.add(calcAvgFitness(deadCreaturesOfCurrentPeriod));
		avgAgeArray.add(calcAvgAge(deadCreaturesOfCurrentPeriod));
		avgTotalDistanceTravelledArray.add(calcAvgTotalDistanceTravelled(deadCreaturesOfCurrentPeriod));
		avgNettoDistanceTravelledArray.add(calcAvgNettoDistanceTravelled(deadCreaturesOfCurrentPeriod));
		avgTotalFoodEatenArray.add(calcAvgTotalFoodEaten(deadCreaturesOfCurrentPeriod));
		
		/**
		 * Calculation and updating of global, boardwide variables:
		 */
		
		totalCreaturesSpawned += board.getAmountOfRandomCreaturesAddedInLastPeriod();
		creaturesSpawnedArray.add(board.getAmountOfRandomCreaturesAddedInLastPeriod());
		
		totalCreaturesBorn += board.getAmountOfCreaturesBornInLastPeriod();
		creaturesBornArray.add(board.getAmountOfCreaturesBornInLastPeriod());
		
		totalCreatures += totalCreaturesBorn + totalCreaturesSpawned;
		
		deadCreaturesOfCurrentPeriod.clear();
		
		System.out.println("Statistics updated, totalCreaturesSpawned = " + totalCreaturesSpawned
				+ " totalCreaturesBorn = " + totalCreaturesBorn);
		
	}

	public void endStats() {
		elapsedTime = startTime - System.nanoTime();
		
		this.board = null;
		
	}

	/**
	 * Calculates the average Fitness of all the creatures in creatureList.
	 * @return avgFitness
	 */
	
	private float calcAvgFitness(ArrayList<Creature> creatureList) {
		float avgFitness = 0;
		
		if(creatureList.size() != 0) {
			for(Creature crtr : creatureList) {
				avgFitness += crtr.getFitness();
			}
			
			avgFitness /= creatureList.size();
		}
				
		return avgFitness;
	}
	
	/**
	 * Calculates the average TotalDistanceTravelled of all the creatures in creatureList.
	 * @return avgTotalDistanceTravelled
	 */
	
	private float calcAvgTotalDistanceTravelled(ArrayList<Creature> creatureList) {
		float avgTotalDistanceTravelled = 0;
		
		if(creatureList.size() != 0) {
			for(Creature crtr : creatureList) {
				avgTotalDistanceTravelled += crtr.getTotalDistanceTravelled();
			}
			
			avgTotalDistanceTravelled /= creatureList.size();
		}
				
		return avgTotalDistanceTravelled;
	}
	
	/**
	 * Calculates the average NettoDistanceTravelled of all the creatures in creatureList.
	 * @return avgNettoDistanceTravelled
	 */
	
	private float calcAvgNettoDistanceTravelled(ArrayList<Creature> creatureList) {
		float avgNettoDistanceTravelled = 0;
		
		if(creatureList.size() != 0) {
			for(Creature crtr : creatureList) {
				avgNettoDistanceTravelled += crtr.getNettoDistanceTravelled();
			}
			
			avgNettoDistanceTravelled /= creatureList.size();
		}
				
		return avgNettoDistanceTravelled;
	}
	/**
	 * Calculates the average Age of all the creatures in creatureList.
	 * @return avgAge
	 */
	
	private float calcAvgAge(ArrayList<Creature> creatureList) {
		float avgAge = 0;
		
		if(creatureList.size() != 0) {
			for(Creature crtr : creatureList) {
				avgAge += crtr.getAge();
			}
			
			avgAge /= creatureList.size();
		}
				
		return avgAge;
	}
	
	/**
	 * Calculates the average TotalFoodEaten of all the creatures in creatureList.
	 * @return avgTotalFoodEaten
	 */
	
	private float calcAvgTotalFoodEaten(ArrayList<Creature> creatureList) {
		float avgTotalFoodEaten = 0;
		
		if(creatureList.size() != 0) {
			for(Creature crtr : creatureList) {
				avgTotalFoodEaten += crtr.getTotalFoodEaten();
			}
			
			avgTotalFoodEaten /= creatureList.size();
		}
				
		return avgTotalFoodEaten;
	}


	public ArrayList<Creature> getDeadCreaturesOfCurrentPeriod() {
		return deadCreaturesOfCurrentPeriod;
	}


	public void setDeadCreaturesOfCurrentPeriod(ArrayList<Creature> deadCreaturesOfCurrentPeriod) {
		this.deadCreaturesOfCurrentPeriod = deadCreaturesOfCurrentPeriod;
	}


	public ArrayList<Float> getAvgFitnessArray() {
		return avgFitnessArray;
	}


	public void setAvgFitnessArray(ArrayList<Float> avgFitnessArray) {
		this.avgFitnessArray = avgFitnessArray;
	}


	public ArrayList<Float> getAvgAgeArray() {
		return avgAgeArray;
	}


	public void setAvgAgeArray(ArrayList<Float> avgAgeArray) {
		this.avgAgeArray = avgAgeArray;
	}


	public ArrayList<Float> getAvgTotalDistanceTravelledArray() {
		return avgTotalDistanceTravelledArray;
	}


	public void setAvgTotalDistanceTravelledArray(ArrayList<Float> avgTotalDistanceTravelledArray) {
		this.avgTotalDistanceTravelledArray = avgTotalDistanceTravelledArray;
	}


	public ArrayList<Float> getAvgNettoDistanceTravelledArray() {
		return avgNettoDistanceTravelledArray;
	}


	public void setAvgNettoDistanceTravelledArray(ArrayList<Float> avgNettoDistanceTravelledArray) {
		this.avgNettoDistanceTravelledArray = avgNettoDistanceTravelledArray;
	}


	public ArrayList<Float> getAvgTotalFoodEatenArray() {
		return avgTotalFoodEatenArray;
	}


	public void setAvgTotalFoodEatenArray(ArrayList<Float> avgTotalFoodEatenArray) {
		this.avgTotalFoodEatenArray = avgTotalFoodEatenArray;
	}


	public ArrayList<Integer> getCreaturesBornArray() {
		return creaturesBornArray;
	}


	public void setCreaturesBornArray(ArrayList<Integer> creaturesBornArray) {
		this.creaturesBornArray = creaturesBornArray;
	}


	public ArrayList<Integer> getCreaturesSpawnedArray() {
		return creaturesSpawnedArray;
	}


	public void setCreaturesSpawnedArray(ArrayList<Integer> creaturesSpawnedArray) {
		this.creaturesSpawnedArray = creaturesSpawnedArray;
	}
	
}
