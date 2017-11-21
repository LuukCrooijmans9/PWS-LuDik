package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

/**
 * An instance of this class is a tile with a blue color, and if NEED_DRINKING is true, the method drinkWaterTile can return
 * something other than 0.
 *
 */

public class WaterTile extends Tile {

	boolean isActive;

	public WaterTile(int upperLeftCornerX, int upperLeftCornerY) {

		isWaterTile = true;
		tileColor = new Color(0f, 0f, 1f);
		setShapeAndPosition(upperLeftCornerX, upperLeftCornerY);
		isActive = ConfigSingleton.INSTANCE.needDrinking;
	}

	/**
	 * This method returns 0 if drinking is disabled, and it returns the amount parameter if drinking is enabled and
	 * this double is not higher than DEFAULT_MAX_WATER.
	 */
	
	public double drinkWaterTile(double amount) {
		if (isActive) {
			return Math.min(amount, ConfigSingleton.INSTANCE.maxWater);
		}
		else {
			return 0;
		}
	}
}
