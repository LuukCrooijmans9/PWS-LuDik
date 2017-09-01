package com.LuDik.EvoAI;

public class Configuration {
	
	public static double  crtrSize;
	
	// Map generation
	public static int tileSize, mapSizeInTiles;
	public static double smoothness = 0.1; // hoe hoger hoe ruwer de overgang van fertility/water
	public static final double DEFAULT_SEED = -1; // Seed is een double van 0 tot 255. negatief laten voor random seed
	
	public static final int DEFAULT_TILE_SIZE = 40, DEFAULT_MAP_SIZE_IN_TILES = 25; 
	
	// globalFertility bepaalt
	public static double globalFertility = 1, globalMaxFood = 1;
	
	
	
}
