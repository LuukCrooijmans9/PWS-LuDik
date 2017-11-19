package com.LuDik.EvoAI;

public class Configuration {

	public static double crtrSize;

	// Map generation
	public static int tileSize, mapSizeInTiles;
	public static double DEFAULT_SMOOTHNESS = 0.1; // hoe hoger hoe ruwer de overgang van fertility/water, eigenlijk hoe
													// ver je ingezoomt zit op de perlin noise.
	public static final long DEFAULT_MAIN_SEED = 0; // Seed is een double van 0 tot 255. negatief laten voor random seed
	public static final long DEFAULT_MAPGEN_SEED = 0; // Seed is een double van 0 tot 255. negatief laten voor random seed

	public static final int DEFAULT_TILE_SIZE = 40, DEFAULT_MAP_SIZE_IN_TILES = 100;

	public static final double DEFAULT_EVOLUTION_FACTOR = 0.002; // 1 is maximale evolution 0 is geen evolutie
	
	public static int BEGIN_AMOUNT_CREATURES = 400;
	public static int RATIO_CHILDS_PER_PARENT = 2; // 2 wil zeggen dat elke parent 2 childs krijgt
	public static int AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION = 5; // hoeveel volledig nieuwe creatures er bij komen
																		// per generatie

	// globalFertility bepaalt
	public static double DEFAULT_FERTILITY_MULTIPLIER = 0.001;
	public static double DEFAULT_MAX_WATER = 100;
	public static double DEFAULT_MAX_FOOD = 10;
	public static double MAX_FOOD_PER_CONSUME = 2;
	public static boolean NEED_DRINKING = false;

	// Waarde van creature
	public static double DEFAULT_CREATURE_SIZE = 10;
	public static double DEFAULT_MAX_FOOD_IN_MOUTH = 10;
	public static double DEFAULT_MAX_WATER_IN_MOUTH = 5;
	public static double DEFAULT_EYE_LENGTH = 35;
	public static double DEFAULT_EYE_DEVIATION = 35;
	public static double EAT_EFFICIENCY_STEEPNESS = 200;
	public static double DEFAULT_STARTING_FAT = 10;
	public static double DEFAULT_STARTING_WATER = 10;
	public static double WEIGHT_PER_FAT = 0.001;
	public static double WEIGHT_PER_WATER = 0.001;
	public static double BASE_FAT_CONSUMPTION = 0.1;
	public static double BASE_WATER_CONSUMPTION = 0.5;
	public static double BASE_CREATURE_EFFICIENCY = 10;
	public static double DEFAULT_MAX_SPEED = 5;
	public static double MAX_DELTA_DIRECTION_PER_STEP = 10;
	public static double AGE_PER_STEP = 0.0001;
	public static int DEFAULT_BRAIN_WIDTH = 3;
	public static int DEFAULT_INPUT_HEIGHT = 13;
	public static int DEFAULT_HIDDEN_HEIGHT = 13;

	
	
	

	// een aantal rekenkundige functies.

	// geeft een nummer tussen de min en max waarde. als distribution 1 is wordt
	// alles gelijk verdeeld, bij > 1 wordt er vaker een kleinere waarde gegeven en
	// bij < 1 wordt vaker een grotere waarde gegeven
	public static int distributedRandomNumber(int max, int min, double distribution) {
		int number = (int) (min + (max + 1 - min) * Math.pow(Math.random(), distribution));
		if (number < min || number > max) {
			return min;
		} else {
			return number;
		}
	}

}
