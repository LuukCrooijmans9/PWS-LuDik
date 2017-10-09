package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class LandTile extends Tile {
	private double fertility, foodValue, foodColor;
	private double tileMaxFoodValue;

	public LandTile(int upperLeftCornerX, int upperLeftCornerY, float frtlty) {

		isWaterTile = false;
		fertility = frtlty;
		tileMaxFoodValue = (double) (fertility * Configuration.DEFAULT_MAX_FOOD);
		foodValue = tileMaxFoodValue;
		foodColor = (double) Math.min(foodValue / Configuration.DEFAULT_MAX_FOOD, 1);
		tileColor = new Color((float) fertility, (float) foodColor, 0f);
		setShapeAndPosition(upperLeftCornerX, upperLeftCornerY);

	}

	public void calculateNextFood() {

		foodValue = (float) (foodValue + fertility * Configuration.DEFAULT_FERTILITY_BONUS);

		if (foodValue >= tileMaxFoodValue) {
			foodValue = tileMaxFoodValue;
		}
		
		foodColor = foodValue / (Configuration.DEFAULT_MAX_FOOD);
		

		tileColor = new Color((float) fertility, (float) foodColor, 0f);

	}
//	desiredFood moet hier een getal tussen -1 en 1 zijn
	public double eatFoodTile(double foodAmount) {
		double tempFood = foodAmount * Configuration.MAX_FOOD_PER_CONSUME;
		//System.out.println("foodEaten = " + foodEaten);
		double foodEaten = Math.min(foodValue, tempFood);
		//System.out.println("foodValue before eaten = " + foodValue);
		foodValue -= foodEaten;
		//System.out.println("foodValue after eaten = " + foodValue);
		
		return foodEaten;
	}

	public void refill() {
		foodValue = tileMaxFoodValue;
		tileColor = new Color((float) fertility, (float) foodColor, 0f);
	}
	
	

}
