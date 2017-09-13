package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class LandTile extends Tile {
	private double fertility, foodValue, foodColor;

	public LandTile(int upperLeftCornerX, int upperLeftCornerY, float frtlty) {

		isWaterTile = false;
		fertility = frtlty;
		foodValue = (double) (fertility * Configuration.globalFertility * Configuration.globalMaxFood + 100);
		foodColor = (double) (foodValue/500);
		tileColor = new Color((float) fertility, (float) foodColor, 0f);
		setShapeAndPosition(upperLeftCornerX, upperLeftCornerY);
		
	}

	public void calculateNextFood() {

		if (foodValue < fertility * Configuration.globalFertility * Configuration.globalMaxFood) {
			foodValue = (float) (foodValue + fertility * Configuration.globalFertility);
		}
		if (foodValue > fertility * Configuration.globalFertility * Configuration.globalMaxFood) {
			foodValue = (float) (fertility * Configuration.globalFertility * Configuration.globalMaxFood);
		}
	}
	
	
	
	public double eatFoodTile(double DesiredFood) {
		DesiredFood *= 10;
		double tempFood = Math.min(foodValue, DesiredFood);
		System.out.println(foodValue);
		return Math.min(tempFood, Configuration.MaxFoodPerConsume);
	}
	
	public void update() {
		foodValue += fertility;
	}
}
