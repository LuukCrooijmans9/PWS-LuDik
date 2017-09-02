package com.LuDik.EvoAI;

/**
 * Deze class houdt de staat van de map en de creatures bij.
 * 
 * @author Luuk
 *
 */

public class Board {
	private Map map;

	public Board(Integer tileSize, Integer mapSize, EvoAI evoAI) {
		evoAI.setBoard(this);

		map = new Map(tileSize, mapSize);

	}

	public Board(Integer tileSize, Integer mapSize, double seed, EvoAI evoAI) {
		evoAI.setBoard(this);

		map = new Map(tileSize, mapSize, seed);

	} 
	
	public Board(Integer tileSize, Integer mapSize, double seed, EvoAI evoAI, double smoothness) {
		evoAI.setBoard(this);

		map = new Map(tileSize, mapSize, seed, smoothness);

	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
