package com.LuDik.EvoAI;

public class Configuration {

	// Map generation
	public static double mapSmoothness = 0.1; // hoe hoger hoe ruwer de overgang van fertility/water, eigenlijk hoe
													// ver je ingezoomt zit op de perlin noise.
	public static  long mainSeed = 0; // Seed is een double van 0 tot 255. negatief laten voor random seed
	public static  long mapGenSeed = 0; // Seed is een double van 0 tot 255. negatief laten voor random seed

	public static int tileSize = 40;
	public static  int mapSizeInTiles = 100;

	public static double evolutionFactor = 0.002; // 1 is maximale evolution 0 is geen evolutie
	
	public static int beginAmountCreatures = 400;
	public static int ratioChildsPerParent = 2; // 2 wil zeggen dat elke parent 2 childs krijgt
	public static int randomCreaturesPerGeneration = 5; // hoeveel volledig nieuwe creatures er bij komen
																		// per generatie

	// globalFertility bepaalt
	public static double fertilityMultiplier = 0.001;
	public static double maxWater = 100;
	public static double maxFood = 10;
	public static double maxFoodPerConsume = 2;
	public static boolean needDrinking = false;

	// Waarde van creature
	public static double crtrSize = 10;
	public static double maxFoodInMouth = 10;
	public static double maxWaterInMouth = 5;
	public static double eyeLength = 35;
	public static double eyeDeviation = 35;
	public static double eatEfficiencySteepness = 200;
	public static double startingFat = 10;
	public static double startingWater = 10;
	public static double weightPerFat = 0.001;
	public static double weightPerWater = 0.001;
	public static double baseFatConsumption = 0.1;
	public static double baseWaterConsumption = 0.5;
	public static double baseCreatureEfficiency = 10;
	public static double maxSpeed = 5;
	public static double maxDeltaDirection = 10;
	public static double agePerStep = 0.0001;
	public static int brainWidth = 3;
	public static int inputLayerHeight = 13;
	public static int hiddenLayerHeight = 13;

	
	
	

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
