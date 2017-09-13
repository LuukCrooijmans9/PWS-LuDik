package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class LandTile extends Tile {
	private double fertility, foodValue, foodColor;

	public LandTile(int upperLeftCornerX, int upperLeftCornerY, float frtlty) {

		isWaterTile = false;
		fertility = frtlty;
		foodValue = (double) (fertility * Configuration.globalFertility + 100);
		foodColor = (double) Math.min(foodValue / (Configuration.globalMaxFood), 1);
		tileColor = new Color((float) fertility, (float) foodColor, 0f);
		setShapeAndPosition(upperLeftCornerX, upperLeftCornerY);

	}

	public void calculateNextFood() {

		foodValue = (float) (foodValue + fertility * Configuration.globalFertility);

		if (foodValue >= Configuration.globalMaxFood * fertility) {
			foodValue = Configuration.globalMaxFood * fertility;
		}
		foodColor = foodValue / (Configuration.globalMaxFood);
		//System.out.println(foodColor);
		tileColor = new Color((float) fertility, (float) foodColor, 0f);
	}

	public double eatFoodTile(double DesiredFood) {
		double tempFood = Math.min(foodValue, DesiredFood);
		//System.out.println("foodValue before eaten = " + foodValue);
		double foodEaten = Math.min(tempFood, Configuration.MaxFoodPerConsume);
		//System.out.println("foodEaten = " + foodEaten);
		foodValue -= foodEaten;
		//System.out.println("foodValue after eaten = " + foodValue);
		
		return foodEaten;
	}

}
