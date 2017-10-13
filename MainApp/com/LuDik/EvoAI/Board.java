package com.LuDik.EvoAI;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

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
	private ArrayList<Creature> creatures; // all creatures currently alive in this Board
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
	 * Constants determined by the Configuration class:
	 */
	private int BEGIN_AMOUNT_CREATURES = Configuration.BEGIN_AMOUNT_CREATURES;
	private double CREATURE_SIZE = Configuration.DEFAULT_CREATURE_SIZE;
	private double EVOLUTION_FACTOR = Configuration.DEFAULT_EVOLUTION_FACTOR;
	private int RATIO_CHILDS_PER_PARENT = Configuration.RATIO_CHILDS_PER_PARENT;
	private int AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION = Configuration.AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION;
	private final int TILE_SIZE = Configuration.DEFAULT_TILE_SIZE;

	public Board(int mapSize, double seed, double smoothness, EvoAI evoAI) {

		evoAI.setBoard(this);
		mainFrame = evoAI;
		infoPanel = mainFrame.getInfoPanel();

		// this.TILE_SIZE = tileSize;

		map = new Map(TILE_SIZE, mapSize, seed, smoothness);

		landTiles = map.getLandTiles();

		spawnPoints = this.generateSpawnPoints();

		timeKeeper = new TimeKeeper(this);
		timeKeeper.setInfoPanel(infoPanel);
		infoPanel.setTimeKeeper(timeKeeper);

		creatures = new ArrayList<Creature>();
		allCreaturesOfGeneration = new ArrayList<Creature>();

		setAverageFitnessArray(new ArrayList<Double>());
		setAverageAgeArray(new ArrayList<Double>());
		setAverageTotalFoodEatenArray(new ArrayList<Double>());

	}

	/**
	 * This method
	 */
	public void spawnFirstCreatures() {

		ArrayList<Point2D> availableSpawnPoints = spawnPoints;

		int creaturesToSpawn = Math.min(BEGIN_AMOUNT_CREATURES, spawnPoints.size()); // makes sure that not more
																						// creatures are spawned than
																						// there are spawnpoints

		for (int i = 0; i < creaturesToSpawn; i++) {
			Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
			Creature nextCreature = new Creature(point.getX(), point.getY(), this, i);
			creatures.add(nextCreature);
			allCreaturesOfGeneration.add(nextCreature);
			availableSpawnPoints.remove(point);
		}
		generation = 0;
		System.out.println("Generation: " + generation + " spawned!");
	}

	public void spawnCreatures() {
		int evolvingCreatures = 0;
		int randomCreatures = 0;
		// ArrayList<Point2D> spawnPoints = this.generateSpawnPoints();
		ArrayList<Point2D> availableSpawnPoints = spawnPoints;
		ArrayList<Creature> parentCreatures = new ArrayList<Creature>();
		ArrayList<Creature> newAllCreaturesOfGeneration = new ArrayList<Creature>();
		ArrayList<Creature> sortedCreaturesOfGeneration = new ArrayList<Creature>(infoPanel.getCreatures());

		int creaturesToSpawn = Math.min(BEGIN_AMOUNT_CREATURES, spawnPoints.size());

		for (int i = 0; i < creaturesToSpawn / RATIO_CHILDS_PER_PARENT; i++) {
			Creature parentCreature = sortedCreaturesOfGeneration.get(i);
			parentCreatures.add(parentCreature);
			sortedCreaturesOfGeneration.remove(parentCreature);
		}

		for (int i = 0; i < (creaturesToSpawn - AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION)
				/ RATIO_CHILDS_PER_PARENT; i++) {
			parentCreatures.get(i);
			for (int j = 0; j < RATIO_CHILDS_PER_PARENT; j++) {
				Point2D point = availableSpawnPoints
						.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
				Creature nextCreature = new Creature(parentCreatures.get(i), point.getX(), point.getY(), generation, i,
						EVOLUTION_FACTOR);
				creatures.add(nextCreature);
				newAllCreaturesOfGeneration.add(nextCreature);
				evolvingCreatures++;
			}
		}

		for (int i = 0; i < AMOUNT_OF_RANDOM_CREATURES_PER_GENERATION; i++) {
			Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
			Creature nextCreature = new Creature(point.getX(), point.getY(), this, i);
			creatures.add(nextCreature);
			newAllCreaturesOfGeneration.add(nextCreature);
			randomCreatures++;
		}

		allCreaturesOfGeneration.clear();
		for (int i = 0; i < newAllCreaturesOfGeneration.size(); i++) {
			allCreaturesOfGeneration.add(newAllCreaturesOfGeneration.get(i));
		}
		for (int i = newAllCreaturesOfGeneration.size(); i < creaturesToSpawn; i++) {
			allCreaturesOfGeneration.add(spawnRandomCreature(i));
		}

		generation++;
		System.out.println(" ");
		System.out.println("Generation: " + generation + " spawned!");
	}

	public Creature spawnRandomCreature(int id) {
		ArrayList<Point2D> availableSpawnPoints = spawnPoints;
		Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
		Creature randomCreature = new Creature(point.getX(), point.getY(), this, id);
		return randomCreature;
	}

	private ArrayList<Point2D> generateSpawnPoints() {

		ArrayList<Point2D> spawnPoints = new ArrayList<Point2D>();

		spawnPoints.ensureCapacity(
				(int) (landTiles.size() * (TILE_SIZE / (CREATURE_SIZE + 2)) * (TILE_SIZE / (CREATURE_SIZE + 2))));

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

	public void updateStep() {

		if (creatures.size() == 0) {
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

			infoPanel.setAverageFitnessOfPreviousGeneration(averageFitness);
			System.out.println("averageFitness: " + (int) averageFitness);

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

			map.refillLandTiles();

			this.spawnCreatures();
		}

		ArrayList<Creature> tempList = new ArrayList<Creature>();

		for (Creature crtr : creatures) {

			if (crtr.isControlled()) {
				crtr.doStep(mainFrame.getCameraPanel().getRcDeltaSpeed(),
						mainFrame.getCameraPanel().getRcDeltaDirection(), mainFrame.getCameraPanel().getRcFoodAmount());

			} else {
				crtr.doStep();

			}
			if (crtr.isDead()) {
				tempList.add(crtr);
			}
		}

		for (Creature crtr : tempList) {
			creatures.remove(crtr);
		}

		for (Tile[] tileArray : map.getTiles()) {
			for (Tile tile : tileArray) {
				tile.calculateNextFood();
			}
		}

		mainFrame.getCameraPanel().update();
		// System.out.println("done");

	}

	public void drawBoard(Graphics2D g2d) {
		map.drawMap(g2d);

		if (creatures != null) {
			for (Creature crtr : new ArrayList<Creature>(creatures)) {
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

	public ArrayList<Creature> getCreatures() {
		return creatures;
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
