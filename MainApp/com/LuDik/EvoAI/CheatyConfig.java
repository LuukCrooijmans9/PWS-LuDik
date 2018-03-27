package com.LuDik.EvoAI;

/**
 * This class exists only because converting the values from ConfigSingleton to Gson proved
 * difficult. This class is a bit cheaty, hence the name "CheatyConfig".
 *
 */

public class CheatyConfig {
	private double mapSmoothness;
	private long mainSeed;	
	private long mapGenSeed;
	private int waterPercentage;		
	private int tileSize;
	private int mapSizeInTiles;
	private double evolutionFactor;											
	private int beginAmountCreatures;
	private int ratioChildrenPerParent;
	private int randomCreaturesPerGeneration;
	private int minAmountCreatures;
	private int maturityAge;
	private double maxFertility;
	private double maxFood;
	private double crtrSize;
	private double eyeLength;		
	private double eyeDeviation;
	private double startingFat;
	private double maxFoodInMouth;
	private double eatEfficiencySteepness;				
	private double weightPerFat;
	private double baseFatConsumption;
	private double agePerStep;
	private double maxSpeed;
	private double maxDeltaDirection;
	private int brainWidth;
	private int inputLayerHeight;
	private int hiddenLayerHeight;
	private boolean needDrinking;
	private double maxWater;
	private double weightPerWater;
	private double startingWater;
	private double baseWaterConsumption;
	private double maxWaterInMouth;
	private int periodLength;
	
	public CheatyConfig() {

		mapSmoothness = ConfigSingleton.INSTANCE.mapSmoothness;
		mainSeed = ConfigSingleton.INSTANCE.mainSeed;
		mapGenSeed = ConfigSingleton.INSTANCE.mapGenSeed;			
		waterPercentage = ConfigSingleton.INSTANCE.waterPercentage;
		tileSize = ConfigSingleton.INSTANCE.tileSize;
		mapSizeInTiles = ConfigSingleton.INSTANCE.mapSizeInTiles;
		evolutionFactor = ConfigSingleton.INSTANCE.evolutionFactor;
		beginAmountCreatures = ConfigSingleton.INSTANCE.beginAmountCreatures;
		ratioChildrenPerParent = ConfigSingleton.INSTANCE.ratioChildrenPerParent;
		randomCreaturesPerGeneration = ConfigSingleton.INSTANCE.randomCreaturesPerGeneration;
		minAmountCreatures = ConfigSingleton.INSTANCE.minAmountCreatures;
		maturityAge = ConfigSingleton.INSTANCE.maturityAge;
		maxFertility = ConfigSingleton.INSTANCE.maxFertility;
		maxFood = ConfigSingleton.INSTANCE.maxFood;
		crtrSize = ConfigSingleton.INSTANCE.crtrSize;
		eyeLength = ConfigSingleton.INSTANCE.eyeLength;
		eyeDeviation = ConfigSingleton.INSTANCE.eyeDeviation;
		startingFat = ConfigSingleton.INSTANCE.startingFat;
		maxFoodInMouth = ConfigSingleton.INSTANCE.maxFoodInMouth;
		eatEfficiencySteepness = ConfigSingleton.INSTANCE.eatEfficiencySteepness;
		weightPerFat = ConfigSingleton.INSTANCE.weightPerFat;
		baseFatConsumption = ConfigSingleton.INSTANCE.baseFatConsumption;
		agePerStep = ConfigSingleton.INSTANCE.agePerStep;
		maxDeltaDirection = ConfigSingleton.INSTANCE.maxDeltaDirection;
		brainWidth = ConfigSingleton.INSTANCE.brainWidth;
		inputLayerHeight = ConfigSingleton.INSTANCE.inputLayerHeight;
		hiddenLayerHeight = ConfigSingleton.INSTANCE.hiddenLayerHeight;
		maxWater = ConfigSingleton.INSTANCE.maxWater;
		weightPerWater = ConfigSingleton.INSTANCE.weightPerWater;
		startingWater = ConfigSingleton.INSTANCE.startingWater;
		baseWaterConsumption = ConfigSingleton.INSTANCE.baseWaterConsumption;
		maxWaterInMouth = ConfigSingleton.INSTANCE.maxWaterInMouth;
		periodLength = ConfigSingleton.INSTANCE.periodLength;
		
	}
}
