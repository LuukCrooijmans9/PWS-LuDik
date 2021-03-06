package com.LuDik.EvoAI;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class coordinates the interaction between a Map object, a TimeKeeper
 * object, and an array of Creature objects. The board class can be seen as a
 * world, in which the Map object is this world's space, the Creature objects
 * are its inhabitants, and the TimeKeeper object is its time. It only has one
 * Map object, one TimeKeeper object, but a varying amount of Creature objects,
 * who are constantly being replaced by new objects.
 * 
 * 
 */

public class Board {

	/**
	 * UI elements:
	 */
	transient DARWIN mainFrame;
	transient private InfoPanel infoPanel;

	/**
	 * Map specific variables
	 */
	private Map map;
	private ArrayList<LandTile> landTiles;
	transient private ArrayList<Point2D> spawnPoints; // this variable is also dependant on the CREATURE_SIZE variable

	/**
	 * This board's TimeKeeper object
	 */
	transient private TimeKeeper timeKeeper;

	/**
	 * Variables for the creatures:
	 */
	ArrayList<Creature> livingCreatures; // all creatures currently alive in this Board
	ArrayList<Creature> allCreaturesOfCurrentPeriod; // all creatures that are part of the current
														// Period, dead or alive
	transient ArrayList<Creature> deadCreaturesOfCurrentPeriod;

	/**
	 * Statistics about previous Period:
	 */
	private int period;
	private ArrayList<Double> averageFitnessArray;
	private ArrayList<Double> averageAgeArray;
	private ArrayList<Double> averageTotalFoodEatenArray;
	private ArrayList<double[]> accomplishmentsOfTheDead;
	private int amountOfCreaturesBornInLastPeriod;
	private int amountOfRandomCreaturesAddedInLastPeriod;
	private int totalFoodEatenInLastPeriod;
	private int amountOfCreaturesDiedInLastPeriod;
	private Statistics boardStats;
	/**
	 * Constants determined by the ConfigSingleton.INSTANCE enumeration:
	 */
	private final int BEGIN_AMOUNT_CREATURES;
	private final double CREATURE_SIZE;
	private final double EVOLUTION_FACTOR;
	private final int RATIO_CHILDS_PER_PARENT;
	private final int AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION;
	private final int TILE_SIZE;

	private long evolutionSeed;
	private long mapGenSeed;
	private long currentSeed;
	private Random mainRNG;
	private ArrayList<Double> foodValues;

	/**
	 * Constructor for Board. Creates the map and the spawnpoints and the
	 * timekeeper, and sets up variables for . It doesn't create the creature
	 * objects, this is done in a different method.
	 * 
	 * @param evoAI
	 */

	public Board(DARWIN evoAI) {

		evoAI.setBoard(this);
		mainFrame = evoAI;
		infoPanel = mainFrame.getInfoPanel();

		currentSeed = ConfigSingleton.INSTANCE.mainSeed;
		setMapGenSeed(ConfigSingleton.INSTANCE.mapGenSeed);
		BEGIN_AMOUNT_CREATURES = ConfigSingleton.INSTANCE.beginAmountCreatures;
		CREATURE_SIZE = ConfigSingleton.INSTANCE.crtrSize;
		EVOLUTION_FACTOR = ConfigSingleton.INSTANCE.evolutionFactor;
		RATIO_CHILDS_PER_PARENT = ConfigSingleton.INSTANCE.ratioChildrenPerParent;
		AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION = ConfigSingleton.INSTANCE.randomCreaturesPerGeneration;
		TILE_SIZE = ConfigSingleton.INSTANCE.tileSize;

		

		map = new Map(); // creates a new map according to the values in ConfigSingleton.INSTANCE

		landTiles = map.getLandTiles();

		spawnPoints = this.generateSpawnPoints();

		timeKeeper = new TimeKeeper(this);
		timeKeeper.setInfoPanel(infoPanel);
		infoPanel.setTimeKeeper(timeKeeper);

		livingCreatures = new ArrayList<Creature>();
		allCreaturesOfCurrentPeriod = new ArrayList<Creature>();
		deadCreaturesOfCurrentPeriod = new ArrayList<Creature>();

		setAverageFitnessArray(new ArrayList<Double>());
		setAverageAgeArray(new ArrayList<Double>());
		setAverageTotalFoodEatenArray(new ArrayList<Double>());
		accomplishmentsOfTheDead = new ArrayList<double[]>();
		foodValues = new ArrayList<Double>();
		
		boardStats = new Statistics(this);

		amountOfCreaturesBornInLastPeriod = 0;
		amountOfRandomCreaturesAddedInLastPeriod = 0;
		totalFoodEatenInLastPeriod = 0;
		amountOfCreaturesDiedInLastPeriod = 0;

	}

	/**
	 * This method creates the first creature objects in the simulation.
	 */
	public void spawnFirstCreatures() {

		ArrayList<Point2D> availableSpawnPoints = spawnPoints;

		int creaturesToSpawn = Math.min(BEGIN_AMOUNT_CREATURES, spawnPoints.size()); // makes sure that not more
																						// creatures are spawned than
																						// there are spawnpoints

		for (int i = 0; i < creaturesToSpawn; i++) {
			Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * randomDouble() + 0.5));
			Creature nextCreature = spawnRandomCreature(i);
			livingCreatures.add(nextCreature);
			allCreaturesOfCurrentPeriod.add(nextCreature);
			availableSpawnPoints.remove(point); // makes sure that no creatures are spawned in the same location.
		}
		period = 0;
		System.out.println("Period: " + period + " spawned!");
	}

	/**
	 * This method
	 */
//	public void spawnCreatures() {
//		int evolvingCreatures = 0;
//		int randomCreatures = 0;
//		ArrayList<Point2D> availableSpawnPoints = spawnPoints;
//		ArrayList<Creature> parentCreatures = new ArrayList<Creature>();
//		ArrayList<Creature> newAllCreaturesOfPeriod = new ArrayList<Creature>();
//		ArrayList<Creature> sortedCreaturesOfPeriod = new ArrayList<Creature>(infoPanel.getCreatures());
//
//		int creaturesToSpawn = Math.min(BEGIN_AMOUNT_CREATURES, spawnPoints.size());
//
//		/**
//		 * The code below selects the parentCreatures, the creatures with the highest
//		 * fitness of the previous Period. The number of parent creatures is dependent
//		 * upon creaturesToSpawn and RATIO_CHILDS_PER_PARENT.
//		 */
//
//		for (int i = 0; i < creaturesToSpawn / RATIO_CHILDS_PER_PARENT; i++) {
//			Creature parentCreature = sortedCreaturesOfPeriod.get(i);
//			parentCreatures.add(parentCreature);
//		}
//
//		allCreaturesOfCurrentPeriod.clear();
//
//		/**
//		 * The code below spawns a number of creatures for each parent. This number is
//		 * determined by RATIO_CHILDS_PER_PARENT. The spawned creatures are based upon
//		 * their parent.
//		 */
//
//		for (int i = 0; i < (creaturesToSpawn - AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION)
//				/ RATIO_CHILDS_PER_PARENT; i++) {
//			Creature parentCreature = parentCreatures.get(i);
//			for (int j = 0; j < RATIO_CHILDS_PER_PARENT; j++) {
//				// Point2D point = availableSpawnPoints
//				// .get((int) ((availableSpawnPoints.size() - 1) * randomDouble() + 0.5));
//				Creature nextCreature = spawnSingleParentCreature(parentCreature, EVOLUTION_FACTOR);
//				// livingCreatures.add(nextCreature);
//				newAllCreaturesOfPeriod.add(nextCreature);
//				evolvingCreatures++;
//			}
//		}

//		/**
//		 * The code below adds newAllcreaturesOfPeriod to the cleared
//		 * allCreaturesOfPeriod arraylist, and spawns a number of random creatures,
//		 * determined by the AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION constant.
//		 */
//
//		// for (int i = 0; i < newAllCreaturesOfPeriod.size(); i++) {
//		// allCreaturesOfPeriod.add(newAllCreaturesOfPeriod.get(i));
//		// }
//		for (int i = newAllCreaturesOfPeriod.size(); i < creaturesToSpawn; i++) {
//			spawnRandomCreature(i);
//		}
//
//		period++;
//		System.out.println(" ");
//		System.out.println("Period: " + period + " spawned!");
//	}

	/**
	 * This method creates a creature with the given id that is not based on another
	 * creature, as its features are fully random.
	 * 
	 * @param id
	 * @return randomCreature
	 */

	public Creature spawnRandomCreature(int id) {
		ArrayList<Point2D> availableSpawnPoints = spawnPoints;
		Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * randomDouble() + 0.5));
		Creature randomCreature = new Creature(point.getX(), point.getY(), this, id);
		allCreaturesOfCurrentPeriod.add(randomCreature);
		livingCreatures.add(randomCreature);
		amountOfRandomCreaturesAddedInLastPeriod++;
		return randomCreature;
	}

	/**
	 * This method creates a creature based on a single other creature, as its
	 * features resembles this parentCreature
	 * 
	 * @param parentCreature,
	 *            Deviation
	 * @return Creature which is based on the parentCreautue
	 */
	public Creature spawnSingleParentCreature(Creature parentCreature, double deviation) {
		double x = parentCreature.getXPos() + randomDouble(-1, 1);
		double y = parentCreature.getYPos() + randomDouble(-1, 1);
		Creature crtr = new Creature(parentCreature, x, y, (int) parentCreature.getCreatureID(), deviation);
		livingCreatures.add(crtr);
		allCreaturesOfCurrentPeriod.add(crtr);
		amountOfCreaturesBornInLastPeriod++;
		return crtr;
	}

	/**
	 * This method returns an array of all the points in the map that a creature can
	 * spawn on
	 * 
	 * @return Points2D array with spawnpoints
	 */

	private ArrayList<Point2D> generateSpawnPoints() {
		landTiles = map.getLandTiles();

		ArrayList<Point2D> spawnPoints = new ArrayList<Point2D>();
		spawnPoints.ensureCapacity(
				(int) (landTiles.size() * (TILE_SIZE / (CREATURE_SIZE + 2)) * (TILE_SIZE / (CREATURE_SIZE + 2))));

		/**
		 * The code below checks for each given tile how many creatures can spawn on it
		 * and where, and adds these to the spawnPoints arraylist.
		 */
		//System.out.println(landTiles.get(0));
		for (LandTile landTile : landTiles) {
			for (int i = 0; i < TILE_SIZE / (CREATURE_SIZE + 2) - 1; i++) {
				for (int k = 0; k < TILE_SIZE / (CREATURE_SIZE + 2) - 1; k++) {
					//System.out.println(landTile.getTileRect());
					Point2D.Double point = new Point2D.Double(
							landTile.getTileRect().getX() + (i + 0.5d) * ((CREATURE_SIZE + 2d)),
							landTile.getTileRect().getY() + (k + 0.5d) * ((CREATURE_SIZE + 2d)));
					//System.out.println(point);
					spawnPoints.add(point);
				}
			}
		}

		return spawnPoints;
	}

	public void doStep() {

		CameraPanel cameraPanel = mainFrame.getCameraPanel();

		/**
		 * The code below checks if the period has ended, and handles the switch from
		 * old period to new period.
		 */
		
//		if (livingCreatures.size() == 0) {
//			// this.updateStatistics((long) generation);
//		
//
//			map.refillLandTiles(); // makes sure that all landTiles have the maximum amount of food, it resets
//									// them.
//
//			this.spawnCreatures(); // spawns a new generation of creatures based off of the old generation.
//		}
	
		if (livingCreatures.size() < ConfigSingleton.INSTANCE.minAmountCreatures) {
			spawnRandomCreature((int) randomLong());
			
		}

		/**
		 * The code below invokes doStep() on all currently alive creatures, and checks
		 * if they're still alive after their step.
		 */
	
		for (Creature crtr : new ArrayList<Creature>(livingCreatures)) {

			if (crtr.isControlled()) {
				crtr.doStep(cameraPanel.getRcDeltaSpeed(), cameraPanel.getRcDeltaDirection(),
						cameraPanel.getRcFoodAmount());
			} else {
				crtr.doStep();
			}
			if (crtr.isDead()) {
				accomplishmentsOfTheDead.add(crtr.funeral());
				livingCreatures.remove(crtr);
				deadCreaturesOfCurrentPeriod.add(crtr);
				boardStats.getDeadCreaturesOfCurrentPeriod().add(crtr);
				amountOfCreaturesDiedInLastPeriod++;
			}
		}
	

		/**
		 * The code below makes all tiles of map grow (increases their food value based
		 * on their fertility value)
		 */

		for (Tile[] tileArray : map.getTiles()) {
			for (Tile tile : tileArray) {
				tile.calculateNextFood();
			}
		}

		cameraPanel.update();


	}

	public void doStatistics(long step) {

		updateStatistics(step);

	}

	/**
	 * This method updates the various statistics variables in Board after a period
	 * has ended.
	 */

	private void updateStatistics(long step) {

		int pStep = ((int) (step / ConfigSingleton.INSTANCE.periodLength) - 1);
		if (pStep < 0) {
			return;
		}

		for (Creature crtr : deadCreaturesOfCurrentPeriod) {
			allCreaturesOfCurrentPeriod.remove(crtr);
		}
		deadCreaturesOfCurrentPeriod.clear();

		boardStats.update();
		
		amountOfCreaturesBornInLastPeriod = 0;
		amountOfRandomCreaturesAddedInLastPeriod = 0;
		amountOfCreaturesDiedInLastPeriod = 0;
		
		period++;

	}

	public int getAmountOfCreaturesBornInLastPeriod() {
		return amountOfCreaturesBornInLastPeriod;
	}

	public void setAmountOfCreaturesBornInLastPeriod(int amountOfCreaturesBornInLastPeriod) {
		this.amountOfCreaturesBornInLastPeriod = amountOfCreaturesBornInLastPeriod;
	}

	public int getAmountOfRandomCreaturesAddedInLastPeriod() {
		return amountOfRandomCreaturesAddedInLastPeriod;
	}

	public void setAmountOfRandomCreaturesAddedInLastPeriod(int amountOfRandomCreaturesAddedInLastPeriod) {
		this.amountOfRandomCreaturesAddedInLastPeriod = amountOfRandomCreaturesAddedInLastPeriod;
	}

	public int getAmountOfCreaturesDiedInLastPeriod() {
		return amountOfCreaturesDiedInLastPeriod;
	}

	public void setAmountOfCreaturesDiedInLastPeriod(int amountOfCreaturesDiedInLastPeriod) {
		this.amountOfCreaturesDiedInLastPeriod = amountOfCreaturesDiedInLastPeriod;
	}

	/**
	 * Draws the components of this method (all the tiles of the map and the
	 * creatures) with the given Graphics2D object.
	 * 
	 * @param g2d
	 */
	public void drawBoard(Graphics2D g2d) {
		map.drawMap(g2d);

		if (livingCreatures != null) {
			for (Creature crtr : new ArrayList<Creature>(livingCreatures)) {
				if (crtr != null) {
					crtr.draw(g2d);
				} else {
					System.out.println(crtr);

				}
			}
		}
	}

	/**
	 * Generates a random double between 0(inclusive) and 1 (exclusive) and keeps
	 * track of the seed used for reproduction/saving.
	 *
	 * @return value between 0 and 1
	 */

	double randomDouble() {
		mainRNG = new Random(currentSeed);
		double randomDouble = mainRNG.nextDouble();
		currentSeed = mainRNG.nextLong();
		return randomDouble;
	}

	/**
	 * Generates a random double between 0(inclusive) and 1 (exclusive) and keeps
	 * track of the seed used for reproduction/saving.
	 * 
	 * @return value between lowest and highest
	 */
	double randomDouble(double rangeLow, double rangeHigh) {
		mainRNG = new Random(currentSeed);
		double randomDouble = (mainRNG.nextDouble() * (rangeHigh - rangeLow) + rangeLow);
		currentSeed = mainRNG.nextLong();
		return randomDouble;
	}

	long randomLong() {
		mainRNG = new Random(currentSeed);
		long randomLong = mainRNG.nextLong();
		currentSeed = mainRNG.nextLong();
		return randomLong;
	}

	public void saveSimulation() {
		if(foodValues == null) {
			foodValues = new ArrayList<Double>();
		}
		for (int i = 0; i < map.getLandTiles().size(); i++) {
			foodValues.add(map.getLandTiles().get(i).getFoodValue());
		}
		System.out.println("Saving foodValues: " + foodValues);
		
		Saver.saveObject(ConfigSingleton.INSTANCE, "Configuration", "ConfigSingleton");
		for (int i = 0; i < allCreaturesOfCurrentPeriod.size(); i++) {
			allCreaturesOfCurrentPeriod.get(i).prepareSave();
		}
		for (int i = 0; i < livingCreatures.size(); i++) {
			livingCreatures.get(i).prepareSave();
		}
		map.prepareSave();
		Saver.saveObject(this, "Board", "Board");
		reloadSimulation();
	}

	public void reloadSimulation() {

		map.reloadSave();
		for (int i = 0; i < allCreaturesOfCurrentPeriod.size(); i++) {
			allCreaturesOfCurrentPeriod.get(i).reloadSave(this);
		}
		for (int i = 0; i < livingCreatures.size(); i++) {
			livingCreatures.get(i).reloadSave(this);
		}
	}

	public void loadBoard(DARWIN mainFrame) {
		if (this.mainFrame == null) {
			this.mainFrame = mainFrame;
		}
		if (deadCreaturesOfCurrentPeriod == null) {
			deadCreaturesOfCurrentPeriod = new ArrayList<Creature>();
		}
		if (timeKeeper == null) {
			timeKeeper = new TimeKeeper(this);
			infoPanel = mainFrame.getInfoPanel();
			timeKeeper.setInfoPanel(infoPanel);
		}
		System.out.println("foodValues: " + foodValues);
		
		map = new Map(mapGenSeed);
		map.loadMap(foodValues);
		for (int i = 0; i < livingCreatures.size(); i++) {
			livingCreatures.get(i).loadCreature();
		}
		if (spawnPoints == null) {
			spawnPoints = generateSpawnPoints();
		}

		if (landTiles == null) {
			landTiles = map.getLandTiles();
		}
		
		amountOfCreaturesBornInLastPeriod = 0;
		amountOfRandomCreaturesAddedInLastPeriod = 0;
		amountOfCreaturesDiedInLastPeriod = 0;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public ArrayList<Creature> getLivingCreatures() {
		return livingCreatures;
	}

	public TimeKeeper getTimeKeeper() {
		return timeKeeper;
	}

	public void setTimeKeeper(TimeKeeper timeKeeper) {
		this.timeKeeper = timeKeeper;
	}

	public DARWIN getEvoAI() {
		return mainFrame;
	}

	public ArrayList<Creature> getAllCreaturesOfPeriod() {
		return allCreaturesOfCurrentPeriod;
	}

	public void setAllCreaturesOfPeriod(ArrayList<Creature> allCreaturesOfPeriod) {
		this.allCreaturesOfCurrentPeriod = allCreaturesOfPeriod;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int Period) {
		this.period = Period;
	}

	public ArrayList<Double> getAverageFitnessArray() {
		return averageFitnessArray;
	}

	public void setAverageFitnessArray(ArrayList<Double> averageFitness) {
		this.averageFitnessArray = averageFitness;
	}

	public ArrayList<Double> getAverageAgeArray() {
		return averageAgeArray;
	}

	public void setAverageAgeArray(ArrayList<Double> averageAgeArray) {
		this.averageAgeArray = averageAgeArray;
	}

	public ArrayList<Double> getAverageTotalFoodEatenArray() {
		return averageTotalFoodEatenArray;
	}

	public void setAverageTotalFoodEatenArray(ArrayList<Double> averageTotalFoodEatenArray) {
		this.averageTotalFoodEatenArray = averageTotalFoodEatenArray;
	}

	public long getMapGenSeed() {
		return mapGenSeed;
	}

	public void setMapGenSeed(long mapGenSeed) {
		this.mapGenSeed = mapGenSeed;
	}

	public Statistics getBoardStats() {
		return boardStats;
	}

	public void setBoardStats(Statistics boardStats) {
		this.boardStats = boardStats;
	}

}
