package com.LuDik.EvoAI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * This class contains a 2 dimensional array of Tiles, and additional
 * information about those tiles and the arrangement thereof.
 */

public class Map {

	/**
	 * Certain properties of this map.
	 */
	private int mapSize; // this is the value for the dimensions in tiles
	private int tileSize; // the size of eacht tile in this Map
	private double mapGenSeed; // the seed for the layout of this map. The same seed
							// will result in the same map, if no other
							// variables are changed.
	private double heightSeed;
	private double fertilitySeed; // the 
	private double waterLevel;
	private double waterPercentage = 20; // the desired percentage of tiles that are waterTiles (waterTiles/Tiles * 100% )
	private double smoothness;
	/**
	 * Smoothness can be seen as how smooth the transition between adjacent
	 * tiles is. A high smoothness value means that the difference in fertility
	 * between adjacent tiles is likely low, and vice-versa.
	 */
	private Tile[][] tiles;
	private Double[][] tileHeights;
	private ArrayList<LandTile> landTiles;
	private ArrayList<WaterTile> waterTiles;

	/**
	 * This constructor constructs a new map with a height and width of
	 * mapSizeInTiles amount of tiles, with a tileSize of tileSize, a seed for the
	 * layout of the map, and with a smoothness of smthnss.
	 * 
	 * @param tileSize
	 * @param mapSizeInTiles
	 * @param mapGenSeed
	 * @param smoothness
	 */

	public Map(int tileSize, int mapSizeInTiles, long mapGenSeed, double smoothness) {

		Random seedGenerator = new Random(mapGenSeed); // is used to ensure that one seed leads to the same map everytime
		heightSeed = seedGenerator.nextDouble() * 255d;
		fertilitySeed = seedGenerator.nextDouble() * 255d;
		
		
		this.smoothness = smoothness;
		mapSize = mapSizeInTiles;
		this.mapGenSeed = mapGenSeed;
		this.tileSize = tileSize;

		Configuration.tileSize = tileSize;
		Configuration.mapSizeInTiles = mapSizeInTiles;

		tileHeights = new Double[mapSize][mapSize];
		tiles = new Tile[mapSize][mapSize];
		landTiles = new ArrayList<LandTile>();
		waterTiles = new ArrayList<WaterTile>();

		/**
		 * The code below generates the height (altitude) of all the tiles, and determines the height of the waterlevel.
		 * Every tile with a height lower than or equal to the waterlevel is a WaterTile. If the height is higher than the waterlevel, a fertility for that tile
		 * is generated and that tile becomes a LandTile. In this way, a 2D grid of LandTiles and WaterTiles is achieved.
		 */

		generateTileHeights(tileHeights, smoothness, heightSeed);
		
		DescriptiveStatistics tileHeightsStats = getArray2DStats(tileHeights);

		System.out.println("standard deviation with apache: " + tileHeightsStats.getStandardDeviation());
		System.out.println("median with apache: " + tileHeightsStats.getPercentile(waterPercentage));

		waterLevel = tileHeightsStats.getPercentile(waterPercentage); // determines how high the waterlevel should be in order to get the desired water percentage
				
		for (int i = 0; i < tileHeights.length; i++) {
			for (int k = 0; k < tileHeights[0].length; k++) {
				
				if (tileHeights[i][k] <= waterLevel) {
					tiles[i][k] = new WaterTile(i * tileSize, k * tileSize);
					waterTiles.add((WaterTile) tiles[i][k]);
					
				} else {
					double fertility = ImprovedNoise.noise( i * smoothness, k * smoothness, fertilitySeed); // the same way of generating a value as the height, only with a different seed
					fertility = (fertility + 1) / 2; // ensures that the fertility ranges from 0 to 1
					
					tiles[i][k] = new LandTile(i * tileSize, k * tileSize, (float) fertility);
					landTiles.add((LandTile) tiles[i][k]);
				}
			}

		}
		System.out.println("landTiles: " + landTiles.size());
		System.out.println("waterTiles: " + waterTiles.size());
	}


	/**
	 * Uses the improved perlin noise algorithm to generate the double height values for the 2D double array, with a given smoothness
	 * and a seed between 0 and 255.
	 * @param tileHeights
	 * @param smoothness
	 * @param seed
	 */
	
	private void generateTileHeights(Double[][] tileHeights, double smoothness, double seed) {
		for (int i = 0; i < tileHeights.length; i++) {
			for (int k = 0; k < tileHeights[0].length; k++) {
				tileHeights[i][k] = ImprovedNoise.noise(i * smoothness, k * smoothness, seed);
			}
		}
	}

	/**
	 * Puts the contents of the array2D in the DescriptiveStatistics object.
	 * @param array2D
	 * @return a DescriptiveStatistics object of the array2D
	 */

	private DescriptiveStatistics getArray2DStats(Double[][] array2D) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		for (int i = 0; i < array2D.length; i++) {
			for (int k = 0; k < array2D[0].length; k++) {
				stats.addValue(array2D[i][k]);
			}
		}
		return stats;
	}
	

	/**
	 * This method calculates the standard deviation of the numbers in the given 2D array.
	 * 
	 * @param array2D
	 * @return the standard deviation in the given 2D array
	 */
	
	private double calcStandardDeviation(Double[][] array2D) {
		double mean = 0;
		
		for (int i = 0; i < array2D.length; i++) {
			for (int k = 0; k < array2D[0].length; k++) {
				mean += array2D[i][k];				
			}
		}

		mean /= array2D.length * array2D[0].length;
		
		double devSquared= 0;
		double meanDevSquared = 0;

		for (int i = 0; i < array2D.length; i++) {
			for (int k = 0; k < array2D[0].length; k++) {
				devSquared = (array2D[i][k] - mean) * (array2D[i][k] - mean);
				meanDevSquared += devSquared;
				
			}
		}
		
		meanDevSquared /= array2D.length * array2D[0].length;
		double standardDeviation = Math.sqrt(meanDevSquared);
		return standardDeviation;
	}

	/**
	 * this method sets all landTiles to their maximum foodValue.
	 */

	public void refillLandTiles() {
		for (LandTile lndTile : landTiles) {
			lndTile.refill();
		}
	}

	/**
	 * This method draws all the tiles on the given Graphics2D object.
	 * 
	 * @param g2d
	 */

	public void drawMap(Graphics2D g2d) {
		for (Tile[] tileArray : tiles) {
			for (Tile tile : tileArray) {
				tile.draw(g2d);
			}
		}
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public ArrayList<LandTile> getLandTiles() {
		return landTiles;
	}

	public void setLandTiles(ArrayList<LandTile> landTiles) {
		this.landTiles = landTiles;
	}

	public ArrayList<WaterTile> getWaterTiles() {
		return waterTiles;
	}

	public void setWaterTiles(ArrayList<WaterTile> waterTiles) {
		this.waterTiles = waterTiles;
	}

}
