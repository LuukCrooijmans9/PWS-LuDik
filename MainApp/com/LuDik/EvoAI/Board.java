package com.LuDik.EvoAI;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
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
	private int mapLength;
	
	
	private int BEGIN_AMOUNT_CREATURES = 10;
	private ArrayList<Creature> creatures;
	private double CREATURE_SIZE = Configuration.DEFAULT_CREATURE_SIZE;
	
	private Area landArea;
	private Area spawnArea;

	public Board(Integer tileSize, Integer mapSize, EvoAI evoAI) {
		evoAI.setBoard(this);

		map = new Map(tileSize, mapSize);

	}

	public Board(Integer tileSize, Integer mapSize, double seed, EvoAI evoAI) {
		evoAI.setBoard(this);

		map = new Map(tileSize, mapSize, seed);

	} 
	
	public Board(Integer tileSize, Integer mapSize, double seed,  double smoothness, EvoAI evoAI) {
		evoAI.setBoard(this);

		map = new Map(tileSize, mapSize, seed, smoothness);
		
		landArea = new Area();
		
		for (Tile[] tileArray : map.getTiles()) {
			for (Tile tile : tileArray) {
				
				if (tile.getClass() == LandTile.class && tile.getTileRect() != null) {					
					landArea.add(new Area(tile.getTileRect()));					
				}
				
			}
		}
		
		creatures = new ArrayList<Creature>();
		spawnArea = landArea;
		for (int i = 0; i < BEGIN_AMOUNT_CREATURES; i++) {
			Rectangle2D spawnAreaBounds = spawnArea.getBounds2D();
			double spawnAreaWidth = spawnAreaBounds.getWidth();
			double spawnAreaHeight = spawnAreaBounds.getHeight();
			double spawnAreaMinX = spawnAreaBounds.getX();
			double spawnAreaMinY = spawnAreaBounds.getY();
			
			
			while (spawnAreaWidth != 0 || spawnAreaHeight != 0) {
				double rndx = Math.random() * spawnAreaWidth + spawnAreaMinX;
				double rndy = Math.random() * spawnAreaHeight + spawnAreaMinY;
				
				if (spawnArea.contains(rndx, rndy)) {
					creatures.add(new Creature(rndx, rndy, this));
					spawnArea.subtract(new Area(new Ellipse2D.Double(rndx - CREATURE_SIZE, rndy - CREATURE_SIZE, 2 * CREATURE_SIZE, 2 * CREATURE_SIZE)));
					System.out.println("Creature created at " + rndx + "," + rndy);
					break;
				} 
			}
			
		}
		
		

	}
	
	public void drawBoard(Graphics2D g2d) {
		map.drawMap(g2d);
		for (Creature crtr : creatures) {
			crtr.draw(g2d);
		}
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
