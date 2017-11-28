package com.LuDik.EvoAI;

import java.util.Random;

public enum ConfigSingleton {
	INSTANCE;

	/**
	 * Variables that determine the map generation and layout:
	 */

	public double mapSmoothness = 0.1; // how smooth the transition of fertilities is
	public long mainSeed = 0; // the seed for generating the evolutionseed. If left 0, a random seed will be
								// generated
	public long mapGenSeed = 0; // the seed for generating the map. If left 0, a random seed will be generated
								// with the mainSeed
	public int waterPercentage = 20; // how large of a percentage of the map is approximately covered with
										// waterTiles.
	public int tileSize = 40; // the length of all side of a tile
	public int mapSizeInTiles = 100; // how many tiles long all sides of the map are

	public double evolutionFactor = 0.002; // determines how big the mutations of the neural network are after
											// reproduction, 1 is the max value, 0 the minimum

	/**
	 * Variables that determine the spawning of the creatures
	 */

	public int beginAmountCreatures = 400; // how many creatures are spawned at the start of each generation
	public int ratioChildrenPerParent = 2; // how many children a chosen creature gets
	public int randomCreaturesPerGeneration = 5; // how many random creatures are added each generation
	public int minAmountCreatures = 100; // The minimal amount of creatures there should be on the world
	public int maturityAge = 5; // How many steps the creature should live before being granted the ability to
								// give birth

	public double maxFertility = 0.01; // the highest value for fertility a tile can have
	public double maxFood = 40; // the highest foodValue a tile can have

	/**
	 * Variables that determine the appearance of the creatures:
	 */

	public double crtrSize = 10; // the length of the diameter of all creatures
	public double eyeLength = 35; // the distance between the eyes of a creature and the spot from which it gets
									// its colour
	public double eyeDeviation = 35; // how many degrees there are between the eyes of a creature

	/**
	 * Variables that determine the way in which creatures eat, interact with food,
	 * and burn up food, or other variables that pertain to food and creatures:
	 */

	public double startingFat = 10; // how much fat a newly born creature has
	public double maxFoodInMouth = 3; // the maximum amount of food a creature can eat in a step
	public double eatEfficiencySteepness = 200; // how well a creature can eat while moving, the higher the value, the
												// worse the efficie
	public double weightPerFat = 0.001; // how much one unit of fat weighs
	public double baseFatConsumption = 0.1; // how much fat the creature uses up per step regardless of conditions
	public double agePerStep = 0.0001; // how much the age of a creature goes up per step

	/**
	 * Variables that determine the manoeuvrability of the creatures:
	 */

	public double maxSpeed = 5; // the heighest amount of units a creature can move per step
	public double maxDeltaDirection = 10; // the heighest amount of degrees a creature can turn per step

	/**
	 * Variables that determine the brain, or the neural network of the creatures
	 */

	public int brainWidth = 3; // how many layers a creature's neural network has (input and ouputlayer
								// included)
	public int inputLayerHeight = 13; // how many neurons there are in the inputlayer
	public int hiddenLayerHeight = 13; // how many neurons there are in the hidden layer(s)

	/**
	 * These variables are only relevant if the needDrinking boolean is set to true,
	 * as they determine determine the way in which creatures interact with water:
	 */
	public boolean needDrinking = false; // if Creatures need to drink or not
	public double maxWater = 100; // maximum amount of water that can be taken from a waterTile in a step
	public double weightPerWater = 0.001; // how much one unit of water weighs
	public double startingWater = 10; // how much water a newly born creature has
	public double baseWaterConsumption = 0.5; // how much water the creature uses up per step regardless of conditions
	public double maxWaterInMouth = 5; // the maximum amount of water a creature can drink in a step (not used)

/**
	 * Variables that determine statistics
	 */
	public int periodLength = 1000; // At which interval statistics happen

	/**
	 * This method ensures that all values are ready to start the simulation. It
	 * must be called before the board class is created.
	 */

	public void finalizeVariables() {

		if (mainSeed == 0) {
			mainSeed = new Random().nextLong(); // generates a random seed
		}

		if (mapGenSeed == 0) {
			Random mainSeedRNG = new Random(mainSeed);
			mainSeedRNG.nextLong(); // makes sure that the second number this RNG generates is used for the
									// mapGenSeed
			mapGenSeed = mainSeedRNG.nextLong();
		}
	}

}
