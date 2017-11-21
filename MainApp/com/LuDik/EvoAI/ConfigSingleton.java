package com.LuDik.EvoAI;

public enum ConfigSingleton {
	INSTANCE;

	// Map generation
	public double mapSmoothness = 0.1; // hoe hoger hoe ruwer de overgang van
										// fertility/water, eigenlijk hoe
										// ver je ingezoomt zit op de perlin
										// noise.
	public long mainSeed = 0; // Seed is een double van 0 tot 255. negatief
								// laten voor random seed
	public long mapGenSeed = 0; // Seed is een double van 0 tot 255. negatief
								// laten voor random seed

	public int tileSize = 40;
	public int mapSizeInTiles = 100;

	public double evolutionFactor = 0.002; // 1 is maximale evolution 0 is geen
											// evolutie

	public int beginAmountCreatures = 400;
	public int ratioChildsPerParent = 2; // 2 wil zeggen dat elke parent 2
											// childs krijgt
	public int randomCreaturesPerGeneration = 5; // hoeveel volledig nieuwe
													// creatures er bij komen
													// per generatie

	// globalFertility bepaalt
	public double fertilityMultiplier = 0.001;
	public double maxWater = 100;
	public double maxFood = 10;
	public boolean needDrinking = false;

	// Waarde van creature
	public double crtrSize = 10;
	public double maxFoodInMouth = 10;
	public double maxWaterInMouth = 5;
	public double eyeLength = 35;
	public double eyeDeviation = 35;
	public double eatEfficiencySteepness = 200;
	public double startingFat = 10;
	public double startingWater = 10;
	public double weightPerFat = 0.001;
	public double weightPerWater = 0.001;
	public double baseFatConsumption = 0.1;
	public double baseWaterConsumption = 0.5;
	public double maxSpeed = 5;
	public double maxDeltaDirection = 10;
	public double agePerStep = 0.0001;
	public int brainWidth = 3;
	public int inputLayerHeight = 13;
	public int hiddenLayerHeight = 13;
}
