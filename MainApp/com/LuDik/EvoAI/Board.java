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

	// spawnCreatures() pakt is op het begin hetzelfde als landArea, zoekt dan
	// op zichzelf een willekeurige positie,
	// maakt daar een creature en haalt dan een hap 2 keer de grootte van een
	// creature op diezelfde plek weg, zodat creatures niet in elkaar gemaakt
	// kunnen worden.
	// Deze functie is verre van geoptimaliseerd, dus BEGIN_AMOUNT_CREATURES
	// moet laag zijn wil je de functie in een redelijke tijd klaar hebben
//	public void spawnCreatures() {
//
//		creatures = new ArrayList<Creature>();
//		tempList = new ArrayList<Creature>();
//		spawnArea = landArea;
//
//		for (int i = 0; i < BEGIN_AMOUNT_CREATURES; i++) {
//			Rectangle2D spawnAreaBounds = spawnArea.getBounds2D();
//			float spawnAreaWidth = (float) spawnAreaBounds.getWidth();
//			float spawnAreaHeight = (float) spawnAreaBounds.getHeight();
//			float spawnAreaMinX = (float) spawnAreaBounds.getX();
//			float spawnAreaMinY = (float) spawnAreaBounds.getY();
//
//			while (spawnAreaWidth != 0 || spawnAreaHeight != 0) {
//				float rndx = (float) Math.random() * spawnAreaWidth + spawnAreaMinX;
//				float rndy = (float) Math.random() * spawnAreaHeight + spawnAreaMinY;
//
//				if (spawnArea.contains(rndx, rndy)) {
//					creatures.add(new Creature(rndx, rndy, this, i));
//					spawnArea.subtract(new Area(new Ellipse2D.Float(rndx - (float) CREATURE_SIZE,
//							rndy - (float) CREATURE_SIZE, 2 * (float) CREATURE_SIZE, 2 * (float) CREATURE_SIZE)));
//					break;
//				}
//			}
//
//		}
//	}

	public void spawnCreatures() {
		
		creatures = new ArrayList<Creature>();
		tempList = new ArrayList<Creature>();
//		spawnArea = landArea;
		
		ArrayList<Point2D> spawnPoints = this.generateSpawnPoints();
		ArrayList<Point2D> availableSpawnPoints = this.generateSpawnPoints();
		
		
		
		for (int i = 0; i < BEGIN_AMOUNT_CREATURES; i++) {
			Point2D point = availableSpawnPoints.get((int) (availableSpawnPoints.size() * Math.random() + 0.5));
			creatures.add(new Creature(point.getX(), point.getY(), this, i));
			availableSpawnPoints.remove(point);
		}
	}
	
	private ArrayList<Point2D> generateSpawnPoints() {
		
		ArrayList<Point2D> spawnPoints = new ArrayList<Point2D>();
		
		spawnPoints.ensureCapacity((int) (landTiles.size() * (tileSize/(CREATURE_SIZE + 2)) * (tileSize/(CREATURE_SIZE + 2))));
		for (LandTile landTile : landTiles) {
			for (int i = 0; i < tileSize/(CREATURE_SIZE + 2) -1; i++) {				
				for (int k = 0; k < tileSize/(CREATURE_SIZE + 2) -1; k++) {
					spawnPoints.add(new Point2D.Double(
							landTile.getTileRect().getX() + (i + 0.5d) * ((CREATURE_SIZE + 2d)), 
							landTile.getTileRect().getY() + (k + 0.5d) * ((CREATURE_SIZE + 2d))));
				}
			}
		}
		
		return spawnPoints;
	}

	public void updateStep() {

		for (Creature crtr : creatures) {

			if (crtr.isControlled()) {
				crtr.move(mainFrame.getCameraPanel().getRcDeltaSpeed(), mainFrame.getCameraPanel().getRcDeltaDirection());
				crtr.eat(mainFrame.getCameraPanel().getRcFoodAmount());
			} else {
				crtr.move(Math.random() * 2 - 1, Math.random() * 2 - 1);
				crtr.eat(Math.random());
			}
				crtr.look();
				crtr.endStep();
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
				crtr.draw(g2d);
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
