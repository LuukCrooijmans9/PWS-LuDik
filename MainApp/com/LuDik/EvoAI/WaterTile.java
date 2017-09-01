package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class WaterTile extends Tile {
	
	public WaterTile(int upperLeftCornerX, int upperLeftCornerY) {
		
		tileColor = new Color(0f, 0f, 1f);
		setShapeAndPosition(upperLeftCornerX, upperLeftCornerY);
	}
}
