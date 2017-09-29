package com.LuDik.EvoAI;

public class Configuration {

	public static double crtrSize;

	// Map generation
	public static int tileSize, mapSizeInTiles;
	public static double DEFAULT_SMOOTHNESS = 0.1; // hoe hoger hoe ruwer de overgang van fertility/water, eigenlijk hoe
													// ver je ingezoomt zit op de perlin noise.
	public static final double DEFAULT_SEED = -1; // Seed is een double van 0 tot 255. negatief laten voor random seed

	public static final int DEFAULT_TILE_SIZE = 40, DEFAULT_MAP_SIZE_IN_TILES = 25;

	public static final double DEFAULT_EVOLUTION_FACTOR = 0.02; // 1 is maximale evolutio 0 is geen evolutie

	// globalFertility bepaalt
	public static double globalFertility = 1, globalMaxFood = 1000;
	public static double MaxFoodPerConsume = 5; // exacte waarde later bepalen

	// Max waarde van creature
	public static double DEFAULT_CREATURE_SIZE = 10, DEFAULT_MAX_FOOD_IN_MOUTH = 5, DEFAULT_MAX_WATER_IN_MOUTH = 5,
			DEFAULT_EYE_LENGTH = 10;

	// Evolutie waarde creature
	public static int BEGIN_AMOUNT_CREATURES = 100;

	// een aantal rekenkundige functies.

	// geeft een nummer tussen de min en max waarde. als distribution 1 is wordt
	// alles gelijk verdeeld, bij > 1 wordt er vaker een kleinere waarde gegeven en
	// bij < 1 wordt vaker een grotere waarde gegeven
	public static int distributedRandomNumber(int max, int min, double distribution) {
		int number = (int) (min + (max + 1 - min) * Math.pow(Math.random(), distribution));
		if(number < min || number > max) {
			return min;
		}else {
			return number;
		}
	}

}
