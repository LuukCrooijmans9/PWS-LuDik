package com.LuDik.EvoAI;

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
	ArrayList<Creature> creatures;

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
		
		
		
		for (int i = 0; i < BEGIN_AMOUNT_CREATURES; i++) {
			creatures.add(new Creature());
		}
		
		

	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
