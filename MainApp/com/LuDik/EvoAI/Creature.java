package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Graphics2D;

public class Creature {

	private long creatureID; // ID om creature aan te herkennen
	private double age;
	private long parentAID, parentBID; // ID parents
	private double fat, weight; // Voedsel vooraad
	private double xPos, yPos, Direction; // Positie en draaing
	private double CreatureSize;
	private int xTile, yTile;
	private double speed, maxSpeed;
	private double desiredFood, desiredDrink, foodInMouth, waterInMouth;

	Color creatureColor;

	public Creature {
		
	}
	
	private void born(long parentA, long parentB) {
		age = 0;
		fat = 10;
	}

	private void consume() {
		desiredFood = 1d; // later door brain bepaalt 1 is max 0 is min negatief
		xTile = (int) Math.round((xPos / Configuration.tileSize) + 0.5d);
		yTile = (int) Math.round((yPos / Configuration.tileSize) + 0.5d);
		

	}

}
