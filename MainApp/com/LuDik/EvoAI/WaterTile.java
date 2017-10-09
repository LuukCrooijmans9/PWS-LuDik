package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class WaterTile extends Tile {

	boolean isActive;

	public WaterTile(int upperLeftCornerX, int upperLeftCornerY) {

		isWaterTile = true;
		tileColor = new Color(0f, 0f, 1f);
		setShapeAndPosition(upperLeftCornerX, upperLeftCornerY);
		isActive = Configuration.NEED_DRINKING;
	}

	public double drinkWaterTile(double amount) {
		if (isActive) {
			return Math.min(amount, Configuration.DEFAULT_MAX_WATER);
		}
		else {
			return 0;
		}
	}
}
