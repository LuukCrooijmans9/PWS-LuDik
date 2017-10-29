package com.LuDik.EvoAI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

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
	private double seed; // the seed for the layout of this map. The same seed
							// will result in the same map, if no other
							// variables are changed.
	private double waterPercentage = 0.25; // the desired percentage of tiles that are waterTiles (waterTiles/Tiles)
	private double smoothness;
	/**
	 * Smoothness can be seen as how smooth the transition between adjacent
	 * tiles is. A high smoothness value means that the difference in fertility
	 * between adjacent tiles is likely low, and vice-versa.
	 */
	private Tile[][] tiles;
	private Double[][] tilesValues;
	private ArrayList<LandTile> landTiles;
	private ArrayList<WaterTile> waterTiles;

	/**
	 * This constructor constructs a new map with a height and width of
	 * mapSizeInTiles amount of tiles, with a tileSize of tSize, a seed for the
	 * layout of the map, and with a smoothness of smthnss.
	 * 
	 * @param tSize
	 * @param mapSizeInTiles
	 * @param seed
	 * @param smthnss
	 */

	public Map(int tSize, int mapSizeInTiles, double seed, double smthnss) {
		if (seed <= 0) {
			seed = Math.random() * 255d;
		}

		smoothness = smthnss;
		mapSize = mapSizeInTiles;
		this.seed = seed;
		tileSize = tSize;

		Configuration.tileSize = tileSize;
		Configuration.mapSizeInTiles = mapSizeInTiles;

		tilesValues = new Double[mapSize][mapSize];
		tiles = new Tile[mapSize][mapSize];
		landTiles = new ArrayList<LandTile>();
		waterTiles = new ArrayList<WaterTile>();

		/**
		 * The code below decides whether a tile is a WaterTile or a LandTile
		 * based on its position in the 2D field. If it's a LandTile, it also
		 * assigns a fertility to the LandTile based on its position. The first
		 * for loop is for the x-direction, the second one for the y-direction.
		 */

		double perlinNumberRedistributed;
		double perlinNumber;


		for (int i = 0; i < tilesValues.length; i++) {
			for (int k = 0; k < tilesValues[0].length; k++) {

				perlinNumber = (float) 1f
						* ((float) ImprovedNoise.noise((float) i * smoothness, (float) k * smoothness, seed));
				tilesValues[i][k] = perlinNumber;
			}
		}

		double standardDeviation = calcStandardDeviation(tilesValues);
		
		System.out.println("standard deviation: " + standardDeviation);
		
		double borderLandWater = calcBorderWithDesiredSurface(standardDeviation, waterPercentage);
		
		double maxShift = 0.5;
		
		for (int i = 0; i < tilesValues.length; i++) {

			for (int k = 0; k < tilesValues[0].length; k++) {

				perlinNumber = tilesValues[i][k];
				if (perlinNumber <= -0.95 || perlinNumber >= 0.95)
					System.out.println("before " + perlinNumber);
				// perlin = (float) (1 / (2 + Math.expm1(-perlin)));
				// System.out.println("after " + maxShift);
				// System.out.println("" );
				// perlinNumberRedistributed = (perlin * (4d / 3d)) - (1d / 3d);
				perlinNumberRedistributed = perlinNumber;

				// perlinNumberRedistributed = redistribute(perlinNumber,
				// maxShift);
//				System.out.println("" + perlinNumberRedistributed);

				if (perlinNumberRedistributed >= 0) {
					tiles[i][k] = new LandTile(i * tileSize, k * tileSize, (float) perlinNumberRedistributed);
					landTiles.add((LandTile) tiles[i][k]);
				} else {
					tiles[i][k] = new WaterTile(i * tileSize, k * tileSize);
					waterTiles.add((WaterTile) tiles[i][k]);
				}
			}

		}
		System.out.println("landTiles: " + landTiles.size());
		System.out.println("waterTiles: " + waterTiles.size());
	}
	
	private double calcBorderWithDesiredSurface(double standDev, double desiredSurface) {
		return 0;
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

	private double redistribute(double number, double maxShift) {

		double p = maxShift;
		double divisor = 1 - (p * p);
		double numberRedistributed = ((p + number) / divisor) - Math.abs(((p * p) + (p * number)) / divisor);
		return numberRedistributed;
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
