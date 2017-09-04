package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class LandTile extends Tile {
	private double fertility, foodValue;

	public LandTile(int upperLeftCornerX, int upperLeftCornerY, float frtlty) {

		isWaterTile = false;
		fertility = frtlty;
		foodValue = (double) (fertility * Configuration.globalFertility * Configuration.globalMaxFood);
		tileColor = new Color((float) fertility, (float) foodValue, 0f);
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

	double eatFoodTile(double DesiredFood) {
		double tempMin = Math.min(foodValue, DesiredFood);
		return Math.min(tempMin, Configuration.MaxFoodPerConsume);
	}
}
