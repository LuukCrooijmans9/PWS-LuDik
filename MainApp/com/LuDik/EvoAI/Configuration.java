package com.LuDik.EvoAI;

public class Configuration {
	
	public static double  crtrSize;
	
	// Map generation
	public static int tileSize, mapSizeInTiles;
	public static double smoothness = 0.1; // hoe hoger hoe ruwer de overgang van fertility/water
	public static final double DEFAULT_SEED = -1; // Seed is een double van 0 tot 255. negatief laten voor random seed
	
	public static final int DEFAULTTILESIZE = 40, DEFAULTMAPSIZEINTILES = 25; 
	
	// globalFertility bepaalt
	public static double globalFertility, globalMaxFood;
	
	
	
}
