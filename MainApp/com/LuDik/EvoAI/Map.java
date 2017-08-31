package com.LuDik.EvoAI;

public class Map {

	
	int mapSize;
	int tileSize;
	Tile[][] tiles;
	
	
	
	public Map(int tSize, int mapSizeInTiles, boolean isTrueRandom) {
		
		tileSize = tSize;
		mapSize = mapSizeInTiles;
		
		tiles = new Tile[mapSize][mapSize];
		
		if (isTrueRandom) {
			
			double randomValue;
			
			for (int i = 0; i < mapSize; i++) {
				
				for(int k = 0; k < mapSize; k++) {
					
					randomValue = (Math.random * 2) - 1;
					
					if (randomValue >= 0) {
						tiles[i][k] = new LandTile();
					}else {
						tiles[i][k] = new WaterTile90
					}
				}
				
			}
		}
	}
	
	
	
}
