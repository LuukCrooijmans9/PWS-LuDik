package com.LuDik.EvoAI;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
	EvoAI mainFrame;
	private InfoPanel infoPanel;

	/**
	 * Map specific variables
	 */
	private Map map;
	private ArrayList<LandTile> landTiles;
	private ArrayList<Point2D> spawnPoints; // this variable is also dependant on the CREATURE_SIZE variable

	/**
	 * This board's TimeKeeper object
	 */
	private TimeKeeper timeKeeper;

	/**
	 * Variables for the creatures:
	 */
	private ArrayList<Creature> livingCreatures; // all creatures currently alive in this Board
	private ArrayList<Creature> allCreaturesOfGeneration; // all creatures that are part of the current generation, dead
															// or alive

	/**
	 * Statistics about previous generations:
	 */
	private int generation;
	private ArrayList<Double> averageFitnessArray;
	private ArrayList<Double> averageAgeArray;
	private ArrayList<Double> averageTotalFoodEatenArray;

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
	private long mainSeed;

	/**
	 * Constructor for Board. Creates the map and the spawnpoints and the timekeeper, and sets up variables for . 
	 * It doesn't create the creature objects, this is done in a different method.
	 * @param evoAI
	 */
	
	public Board(EvoAI evoAI) {

		evoAI.setBoard(this);
		mainFrame = evoAI;
		infoPanel = mainFrame.getInfoPanel();
		
		mainSeed = ConfigSingleton.INSTANCE.mainSeed;
		BEGIN_AMOUNT_CREATURES = ConfigSingleton.INSTANCE.beginAmountCreatures;
		CREATURE_SIZE = ConfigSingleton.INSTANCE.crtrSize;
		EVOLUTION_FACTOR = ConfigSingleton.INSTANCE.evolutionFactor;
		RATIO_CHILDS_PER_PARENT = ConfigSingleton.INSTANCE.ratioChildrenPerParent;
		AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION = ConfigSingleton.INSTANCE.randomCreaturesPerGeneration;
		TILE_SIZE = ConfigSingleton.INSTANCE.tileSize;
		
		Random mainSeedRNG = new Random(mainSeed);
		evolutionSeed = mainSeedRNG.nextLong();
		
		map = new Map(); // creates a new map according to the values in ConfigSingleton.INSTANCE

		landTiles = map.getLandTiles();

		spawnPoints = this.generateSpawnPoints();

		timeKeeper = new TimeKeeper(this);
		timeKeeper.setInfoPanel(infoPanel);
		infoPanel.setTimeKeeper(timeKeeper);

		livingCreatures = new ArrayList<Creature>();
		allCreaturesOfGeneration = new ArrayList<Creature>();

		setAverageFitnessArray(new ArrayList<Double>());
		setAverageAgeArray(new ArrayList<Double>());
		setAverageTotalFoodEatenArray(new ArrayList<Double>());

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
			Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
			Creature nextCreature = new Creature(point.getX(), point.getY(), this, i);
			livingCreatures.add(nextCreature);
			allCreaturesOfGeneration.add(nextCreature);
			availableSpawnPoints.remove(point); // makes sure that no creatures are spawned in the same location.
		}
		generation = 0;
		System.out.println("Generation: " + generation + " spawned!");
	}

	/**
	 * This method 
	 */
	public void spawnCreatures() {
		int evolvingCreatures = 0;
		int randomCreatures = 0;
		ArrayList<Point2D> availableSpawnPoints = spawnPoints;
		ArrayList<Creature> parentCreatures = new ArrayList<Creature>();
		ArrayList<Creature> newAllCreaturesOfGeneration = new ArrayList<Creature>();
		ArrayList<Creature> sortedCreaturesOfGeneration = new ArrayList<Creature>(infoPanel.getCreatures());

		int creaturesToSpawn = Math.min(BEGIN_AMOUNT_CREATURES, spawnPoints.size());

		/**
		 * The code below selects the parentCreatures, the creatures with the highest fitness of the previous generation.
		 * The number of parent creatures is dependent upon creaturesToSpawn and RATIO_CHILDS_PER_PARENT.
		 */
		
		for (int i = 0; i < creaturesToSpawn / RATIO_CHILDS_PER_PARENT; i++) {
			Creature parentCreature = sortedCreaturesOfGeneration.get(i);
			parentCreatures.add(parentCreature);
		}

		/**
		 * The code below spawns a number of creatures for each parent. This number is
		 * determined by RATIO_CHILDS_PER_PARENT. The spawned creatures are based upon their parent.
		 */
		
		for (int i = 0; i < (creaturesToSpawn - AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION)
				/ RATIO_CHILDS_PER_PARENT; i++) {
			parentCreatures.get(i);
			for (int j = 0; j < RATIO_CHILDS_PER_PARENT; j++) {
				Point2D point = availableSpawnPoints
						.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
				Creature nextCreature = new Creature(
						parentCreatures.get(i), 
						point.getX(), point.getY(),
						generation, RATIO_CHILDS_PER_PARENT * i + j,
						EVOLUTION_FACTOR);
				livingCreatures.add(nextCreature);
				newAllCreaturesOfGeneration.add(nextCreature);
				evolvingCreatures++;
			}
		}

		/**
		 * The code below adds newAllcreaturesOfGeneration to the cleared 
		 * allCreaturesOfGeneration arraylist, and spawns a number of random creatures, 
		 * determined by the AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION constant.
		 */
		
		allCreaturesOfGeneration.clear();
		for (int i = 0; i < newAllCreaturesOfGeneration.size(); i++) {
			allCreaturesOfGeneration.add(newAllCreaturesOfGeneration.get(i));
		}
		for (int i = newAllCreaturesOfGeneration.size(); i < creaturesToSpawn; i++) {
			Creature nextCreature = spawnRandomCreature(i);
			allCreaturesOfGeneration.add(nextCreature);
			livingCreatures.add(nextCreature);
		}

		generation++;
		System.out.println(" ");
		System.out.println("Generation: " + generation + " spawned!");
	}

	/**
	 * This method creates a creature with the given id that is not based on another creature, as its features are fully random.
	 * @param id
	 * @return randomCreature
	 */
	
	public Creature spawnRandomCreature(int id) {
		ArrayList<Point2D> availableSpawnPoints = spawnPoints;
		Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
		Creature randomCreature = new Creature(point.getX(), point.getY(), this, id);
		return randomCreature;
	}

	/**
	 * This method returns an array of all the points in the map that a creature can spawn on
	 * @return Points2D array with spawnpoints
	 */
	
	private ArrayList<Point2D> generateSpawnPoints() {

		ArrayList<Point2D> spawnPoints = new ArrayList<Point2D>();
		spawnPoints.ensureCapacity(
				(int) (landTiles.size() * (TILE_SIZE / (CREATURE_SIZE + 2)) * (TILE_SIZE / (CREATURE_SIZE + 2))));

		/**
		 * The code below checks for each given tile how many creatures can spawn on it and where, and adds these
		 * to the spawnPoints arraylist.
		 */
		
		for (LandTile landTile : landTiles) {
			for (int i = 0; i < TILE_SIZE / (CREATURE_SIZE + 2) - 1; i++) {
				for (int k = 0; k < TILE_SIZE / (CREATURE_SIZE + 2) - 1; k++) {
					spawnPoints
							.add(new Point2D.Double(landTile.getTileRect().getX() + (i + 0.5d) * ((CREATURE_SIZE + 2d)),
									landTile.getTileRect().getY() + (k + 0.5d) * ((CREATURE_SIZE + 2d))));
				}
			}
		}

		return spawnPoints;
	}

	public void doStep() {
		
		CameraPanel cameraPanel = mainFrame.getCameraPanel();
		
		/**
		 * The code below checks if the generation has ended, and handles the switch from old generation to new generation.
		 */
		
		if (livingCreatures.size() == 0) {
			this.updateStatistics(); 
			
			map.refillLandTiles(); // makes sure that all landTiles have the maximum amount of food, it resets them.

			this.spawnCreatures(); // spawns a new generation of creatures based off of the old generation.
		}

		/**
		 * The code below invokes doStep() on all currently alive creatures, 
		 * and checks if they're still alive after their step.
		 */
		
		for (Creature crtr : new ArrayList<Creature>(livingCreatures)) {

			if (crtr.isControlled()) {			
				crtr.doStep(
						cameraPanel.getRcDeltaSpeed(),
						cameraPanel.getRcDeltaDirection(),
						cameraPanel.getRcFoodAmount());
			} else {
				crtr.doStep();
			} if (crtr.isDead()) {
				livingCreatures.remove(crtr);
			}
		}

		/**
		 * The code below makes all tiles of map grow (increases their food value based on their fertility value)
		 */
		
		for (Tile[] tileArray : map.getTiles()) {
			for (Tile tile : tileArray) {
				tile.calculateNextFood();
			}
		}

		cameraPanel.update();

	}
	
	/**
	 * This method updates the various statistics variables in Board after a generation has ended.
	 */
	
	private void updateStatistics() {
		
		/**
		 * The code below calculates the average age, fitness and totalfoodeaten of all creatures of the generation
		 * that has now ended, and puts it in arrays.
		 */
		
		double averageFitness = 0;
		double averageAge = 0;
		double averageTotalFoodEaten = 0;
		
		for (int i = 0; i < allCreaturesOfGeneration.size(); i++) {
			averageFitness += allCreaturesOfGeneration.get(i).getFitness();
			averageAge += allCreaturesOfGeneration.get(i).getAge();
			averageTotalFoodEaten += allCreaturesOfGeneration.get(i).getTotalFoodEaten();
		}
		averageFitness /= BEGIN_AMOUNT_CREATURES;
		averageAge /= BEGIN_AMOUNT_CREATURES;
		averageTotalFoodEaten /= BEGIN_AMOUNT_CREATURES;

		this.getAverageFitnessArray().add(generation, averageFitness);
		this.getAverageAgeArray().add(generation, averageAge);
		this.getAverageTotalFoodEatenArray().add(generation, averageTotalFoodEaten);

		infoPanel.setAverageFitnessOfPreviousGeneration(averageFitness); //updates infoPanel
		System.out.println("averageFitness: " + (int) averageFitness);

		/**
		 * The code below calculates the improvement in fitness and indexes the fitness score of this generation.
		 */
		
		if (generation > 2) {
			double improvement = (double) this.getAverageFitnessArray().get(generation)
					- (double) this.getAverageFitnessArray().get(generation - 1);
			System.out.println("improvement: " + (int) improvement);
			improvement = (double) this.getAverageFitnessArray().get(generation)
					- (double) this.getAverageFitnessArray().get(0);
			System.out.println("Total improvement: " + (int) improvement);
			System.out.println("Index: "
					+ ((this.getAverageFitnessArray().get(generation) / this.getAverageFitnessArray().get(0))
							* 100));

		}
	}

	/**
	 * Draws the components of this method (all the tiles of the map and the creatures) with the given Graphics2D object.
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

	public EvoAI getEvoAI() {
		return mainFrame;
	}

	public ArrayList<Creature> getAllCreaturesOfGeneration() {
		return allCreaturesOfGeneration;
	}

	public void setAllCreaturesOfGeneration(ArrayList<Creature> allCreaturesOfGeneration) {
		this.allCreaturesOfGeneration = allCreaturesOfGeneration;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
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

}
