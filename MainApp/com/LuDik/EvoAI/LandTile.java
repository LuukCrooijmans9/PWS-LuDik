package com.LuDik.EvoAI;

import java.awt.Color;

/**
 * An instance of this class is a tile with a fertility and a foodValue. The foodValue can be influenced from without.
 * 
 *
 */

public class LandTile extends Tile {
	private double fertility; // how quickly the foodvalue increases

	private double foodValue; // how much food there is on this LandTile
	private double foodColor; // the green component of the tileColor, which is determined by the foodvalue
	private double tileMaxFoodValue; // the maximum value of foodvalue
	
	private static double GLOBAL_MAX_FOOD;
	private static double GLOBAL_FERTILITY;

	/**
	 * This method initializes a LandTile with the given variables 
	 * @param upperLeftCornerX
	 * @param upperLeftCornerY
	 * @param fertility
	 */
	
	public LandTile(int upperLeftCornerX, int upperLeftCornerY, float fertility) {

		tileSize = ConfigSingleton.INSTANCE.tileSize;
		GLOBAL_MAX_FOOD = ConfigSingleton.INSTANCE.maxFood;
		GLOBAL_FERTILITY = ConfigSingleton.INSTANCE.maxFertility;
		
		isWaterTile = false;
		this.fertility = fertility;
		tileMaxFoodValue = (double) (fertility * GLOBAL_MAX_FOOD);
		foodValue = tileMaxFoodValue;
		foodColor = (double) Math.min(foodValue / GLOBAL_MAX_FOOD, 1);
		tileColor = new Color((float) fertility, (float) foodColor, 0f);
		setShapeAndPosition(upperLeftCornerX, upperLeftCornerY);

	}

	/**
	 * This method lets the LandTile know that a step has passed and it is time to check
	 * how much food needs to be added according to the fertility and the tileMaxFoodValue
	 */
	
	public void calculateNextFood() {

		foodValue += fertility * GLOBAL_FERTILITY;

		if (foodValue >= tileMaxFoodValue) {
			foodValue = tileMaxFoodValue;
		}
		
		foodColor = foodValue / GLOBAL_MAX_FOOD;
		

		tileColor = new Color((float) fertility, (float) foodColor, 0f);

	}
	
	/**
	 * This method deducts the foodAmount from foodValue and then returns the amount it deducted from foodvalue.
	 * If foodAmount is higher than foodValue, then foodValue is deducted from foodValue and foodValue is returned.
	 */
	public double eatFoodTile(double foodAmount) {
		
		
		double foodEaten = Math.min(foodValue, foodAmount);
		foodValue -= foodEaten;
		
		return foodEaten;
	}

	/**
	 * this method resets the tile to its maximum foodValue.
	 */
	
	public void refill() {
		foodValue = tileMaxFoodValue;
		tileColor = new Color((float) fertility, (float) foodColor, 0f);
	}

	public double getFoodValue() {
		return foodValue;
	}

	public void setFoodValue(double foodValue) {
		this.foodValue = foodValue;
	}
	
	

}
