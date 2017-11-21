package com.LuDik.EvoAI;

public enum ConfigSingleton {
	INSTANCE;

	// Map generation
	public double mapSmoothness = 0.1; // how smooth the transition of fertilities is
	public long mainSeed = 0; // the seed for generating the evolutionseed. If left 0, a random seed will be generated
	public long mapGenSeed = 0; // the seed for generating the map. If left 0, a random seed will be generated with the mainSeed

	public int tileSize = 40; // the length of all side of a tile
	public int mapSizeInTiles = 100; // how many tiles long all sides of the map are

	public double evolutionFactor = 0.002; // determines how big the mutations of the neural network are, 1 is the max value, 0 the minimum

	public int beginAmountCreatures = 400; // how many creatures are spawned at the start of each generation
	public int ratioChildrenPerParent = 2; // how many children a chosen creature gets
	public int randomCreaturesPerGeneration = 5; // how many 

	public double maxFertility = 0.001; // the highest value for fertility a tile can have
	public double maxWater = 100; 
	public double maxFood = 10; // the highest foodValue a tile can have
	public boolean needDrinking = false; // if Creatures need to drink or not

	public double crtrSize = 10; // the length of the diameter of all creatures
	public double maxFoodInMouth = 10; // the maximum amount of food a creature can eat in a step
	public double maxWaterInMouth = 5; // the maximum amount of water a creature can drink in a step
	public double eyeLength = 35; // the distance between the eyes of a creature and the spot from which it gets its colour
	public double eyeDeviation = 35; // how many degrees there are between the eyes of a creature
	public double eatEfficiencySteepness = 200; // how well a creature can eat while moving, the higher the value, the worse the efficie
	public double startingFat = 10; // how much fat a newly born creature has
	public double startingWater = 10; // how much water a newly born creature has
	public double weightPerFat = 0.001; // how much one unit of fat weighs
	public double weightPerWater = 0.001; // how much one unit of water weighs
	public double baseFatConsumption = 0.1; // how much fat the creature uses up per step regardless of conditions
	public double baseWaterConsumption = 0.5; // how much water the creature uses up per step regardless of conditions
	public double maxSpeed = 5; // the heighest amount of units a creature can move per step
	public double maxDeltaDirection = 10; // the heighest amount of degrees a creature can turn per step
	public double agePerStep = 0.0001; // how much the age of a creature goes up per step
	public int brainWidth = 3; // how many layers a creature's neural network has (input and ouputlayer included)
	public int inputLayerHeight = 13; // how many neurons there are in the inputlayer
	public int hiddenLayerHeight = 13; // how many neurons there are in the hidden layer(s)
}
