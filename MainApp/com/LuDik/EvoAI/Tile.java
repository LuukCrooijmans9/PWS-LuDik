package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Tile {
	int upperLeftX, upperLeftY;
	static int tileSize = Configuration.tileSize;
	boolean isWaterTile;

	Rectangle2D tileRect = new Rectangle2D.Double();

	Color tileColor;

	void setShapeAndPosition(int x, int y) {
		upperLeftX = x;
		upperLeftY = y;
		tileRect = new Rectangle2D.Float((float) x, (float) y, (float) tileSize, (float) tileSize);
	}

	void draw(Graphics2D g2d) {

		g2d.setColor(tileColor);
		g2d.fill(tileRect);
		g2d.setColor(Color.BLACK);
		g2d.draw(tileRect);
	}


	public Rectangle2D getTileRect() {
		return tileRect;
	}

	public double eatFoodTile(double DesiredFood) {
//		System.out.println("WaterTile");
		return 0;
	}
	

	public void calculateNextFood() {
		
	}
}
