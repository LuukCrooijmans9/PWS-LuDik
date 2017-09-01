package com.LuDik.EvoAI;

/** Deze class houdt de staat van de map en de creatures bij.
 * 
 * @author Luuk
 *
 */

public class Board {
	private Map map;
	
	
	public Board(Integer tileSize, Integer mapSize, boolean isTrueRandom) {
		map = new Map(tileSize, mapSize, isTrueRandom);
		
	}
	
	
}
