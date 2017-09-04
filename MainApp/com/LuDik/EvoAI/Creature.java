package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Graphics2D;

public class Creature {

	private long CreatureID; // ID om creature aan te herkennen
	private double age;
	private long ParentAID, ParentBID; // ID parents
	private double fat, weight; // Voedsel vooraad
	private double Xpos, Ypos, Direction; // Positie en draaing
	private double CreatureSize;
	private int Xtile, Ytile;
	private double speed, maxSpeed;
	private double DesiredFood, DesiredDrink, foodInMouth, waterInMouth;

	Color creatureColor;

	private void Born(long ParentA, long ParentB) {
		age = 0;
		fat = 10;
	}

	private void Eat() {
		DesiredFood = 1d; // later door brain bepaalt 1 is max 0 is min negatief
		Xtile = (int) Math.round((Xpos / Configuration.tileSize) + 0.5d);
		Ytile = (int) Math.round((Ypos / Configuration.tileSize) + 0.5d);
		if (Map.tiles[Xtile][Ytile].isWaterTile) {
			foodInMouth = 0; 	// mogelijk drinkWaterTile functie later
		} else {
			foodInMouth = Map.tiles[Xtile][Ytile].eatFoodTile(DesiredFood);
			fat += foodInMouth;
			foodInMouth = 0;
		}

	}

}
