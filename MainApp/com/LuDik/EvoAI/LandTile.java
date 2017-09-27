package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class LandTile extends Tile {
	private double fertility, foodValue, foodColor;

	public LandTile(int upperLeftCornerX, int upperLeftCornerY, float frtlty) {

		isWaterTile = false;
		fertility = frtlty;
		foodValue = (double) (fertility * Configuration.globalMaxFood + 100);
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
//	desiredFood moet hier een getal tussen -1 en 1 zijn
	public double eatFoodTile(double desiredFood) {
		double tempFood = desiredFood * Configuration.MaxFoodPerConsume;
		//System.out.println("foodEaten = " + foodEaten);
		double foodEaten = Math.min(foodValue, tempFood);
		//System.out.println("foodValue before eaten = " + foodValue);
		foodValue -= foodEaten;
		//System.out.println("foodValue after eaten = " + foodValue);
		
		return foodEaten;
	}

}
