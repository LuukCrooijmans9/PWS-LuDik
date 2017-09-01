package com.LuDik.EvoAI;

import java.awt.Graphics2D;

public class Map {

	int mapSize;
	int tileSize;
	Tile[][] tiles;

	public Map(int tSize, int mapSizeInTiles) {

		tileSize = tSize;
		Configuration.tileSize = tileSize;
		
		mapSize = mapSizeInTiles;
		Configuration.mapSizeInTiles = mapSizeInTiles;
		tiles = new Tile[mapSize][mapSize];

		double randomValue;

		for (int i = 0; i < mapSize; i++) {

			for (int k = 0; k < mapSize; k++) {

				randomValue = (Math.random() * 1.333) - 0.333;

				if (randomValue >= 0) {
					tiles[i][k] = new LandTile(i * tileSize, k * tileSize, (float) randomValue);
				} else {
					tiles[i][k] = new WaterTile(i * tileSize, k * tileSize);
				}
			}

		}

	}
	
	public void drawMap(Graphics2D g2d) {
		for (Tile[] tileArray : tiles) {
			for (Tile tile : tileArray) {
				tile.draw(g2d);
			}
		}
	}

}
