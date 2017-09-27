package com.LuDik.EvoAI;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Deze class houdt de staat van de map en de creatures bij.
 * 
 * @author Luuk
 *
 */

public class Board {

	private Map map;
	private ArrayList<LandTile> landTiles;
	private int mapLength;

	EvoAI mainFrame;

	private ArrayList<Creature> creatures;
	private ArrayList<Creature> tempList;
	private ArrayList<Creature> allCreaturesOfGeneration;

	private int BEGIN_AMOUNT_CREATURES = Configuration.BEGIN_AMOUNT_CREATURES;
	private double CREATURE_SIZE = Configuration.DEFAULT_CREATURE_SIZE;
	private Integer tileSize;

	private Area landArea;
	private Area spawnArea;
	private TimeKeeper timeKeeper;
	private InfoPanel infoPanel;

	public Board(Integer tileSize, Integer mapSize, EvoAI evoAI) {
		evoAI.setBoard(this);

		map = new Map(tileSize, mapSize);

	}

	public Board(Integer tileSize, Integer mapSize, double seed, EvoAI evoAI) {
		evoAI.setBoard(this);

		this.tileSize = tileSize;

		map = new Map(tileSize, mapSize, seed);

	}

	public Board(Integer tileSize, Integer mapSize, double seed, double smoothness, EvoAI eAI) {
		eAI.setBoard(this);

		this.tileSize = tileSize;
		mainFrame = eAI;
		infoPanel = mainFrame.getInfoPanel();
		map = new Map(tileSize, mapSize, seed, smoothness);

		landTiles = map.getLandTiles();

		landArea = new Area();

		// System.out.println(map.getLandTiles());

		for (LandTile landTile : landTiles) {

			if (landTile.getTileRect() != null) {
				landArea.add(new Area(landTile.getTileRect()));

			}
		}

		timeKeeper = new TimeKeeper(this);
		timeKeeper.setInfoPanel(infoPanel);
		infoPanel.setTimeKeeper(timeKeeper);

	}

	public void spawnRandomCreatures() {

		creatures = new ArrayList<Creature>();
		tempList = new ArrayList<Creature>();
		// spawnArea = landArea;

		ArrayList<Point2D> spawnPoints = this.generateSpawnPoints();
		ArrayList<Point2D> availableSpawnPoints = this.generateSpawnPoints();
		int creaturesToSpawn = Math.min(BEGIN_AMOUNT_CREATURES, spawnPoints.size());

		for (int i = 0; i < creaturesToSpawn; i++) {
			Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
			creatures.add(new Creature(point.getX(), point.getY(), this, i));
			availableSpawnPoints.remove(point);
		}
		allCreaturesOfGeneration = creatures;
	}
	
	public void spawnCreatures() {
		
		//TODO dit afmaken
		ArrayList<Point2D> spawnPoints = this.generateSpawnPoints();
		ArrayList<Point2D> availableSpawnPoints = this.generateSpawnPoints();
		int creaturesToSpawn = Math.min(BEGIN_AMOUNT_CREATURES, spawnPoints.size());

		for (int i = 0; i < creaturesToSpawn; i++) {
			Point2D point = availableSpawnPoints.get((int) ((availableSpawnPoints.size() - 1) * Math.random() + 0.5));
			
		}
		allCreaturesOfGeneration = creatures;
	}

	private ArrayList<Point2D> generateSpawnPoints() {

		ArrayList<Point2D> spawnPoints = new ArrayList<Point2D>();

		spawnPoints.ensureCapacity(
				(int) (landTiles.size() * (tileSize / (CREATURE_SIZE + 2)) * (tileSize / (CREATURE_SIZE + 2))));
		for (LandTile landTile : landTiles) {
			for (int i = 0; i < tileSize / (CREATURE_SIZE + 2) - 1; i++) {
				for (int k = 0; k < tileSize / (CREATURE_SIZE + 2) - 1; k++) {
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
			this.spawnRandomCreatures();
		}

		for (Creature crtr : creatures) {

			if (crtr.isControlled()) {
//				crtr.move(mainFrame.getCameraPanel().getRcDeltaSpeed(),
//						mainFrame.getCameraPanel().getRcDeltaDirection());
//				crtr.eat(mainFrame.getCameraPanel().getRcFoodAmount());
				crtr.doStep(
						mainFrame.getCameraPanel().getRcDeltaSpeed(),
						mainFrame.getCameraPanel().getRcDeltaDirection(), 
						mainFrame.getCameraPanel().getRcFoodAmount());

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

		tempList.clear();

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

}
