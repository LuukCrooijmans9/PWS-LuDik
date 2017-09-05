package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class Creature {

	private long creatureID; // ID om creature aan te herkennen
	private double age;
	private long parentAID, parentBID; // ID parents
	private double fat, weight, fatBurned; // Voedsel vooraad
	private double xPos, deltaXPos, deltaYPos, yPos, direction, creatureSize; // Positie en draaing
	private double CreatureSize;
	private int xTile, yTile;
	private double speed, maxSpeed;
	private double desiredFood, desiredDrink, foodInMouth, waterInMouth;
	private boolean isDead = false;

	// Door brain bepaalt
	private double deltaDirection, deltaSpeed; // waarde tussen -1 en 1

	Color creatureColor;
	Ellipse2D creatureShape;

	public void Creature(double x, double y) {

		xPos = x + 2 * Configuration.DEFAULT_CREATURE_SIZE;
		yPos = y + 2 * Configuration.DEFAULT_CREATURE_SIZE;
		age = 0;
		fat = 10;
		creatureSize = Configuration.DEFAULT_CREATURE_SIZE;
		weight = fat * creatureSize;

		creatureColor = new Color(0, 1, 0);
		creatureShape = new Ellipse2D.Double(xPos, yPos, creatureSize / 2, creatureSize / 2);
	}

	private void born(long parentA, long parentB) {

		age = 0;
		fat = 10;
	}

	private void prepareCreature() {

		// huidige tile waar die staat
		xTile = (int) Math.round((xPos / Configuration.tileSize) + 0.5d);
		yTile = (int) Math.round((yPos / Configuration.tileSize) + 0.5d);

	}

	private void consume() {

		desiredFood = 1d; // later door brain bepaalt 1 is max 0 is min

		Map.tiles[xTile][yTile].beingConsumed(desiredFood);

		fatBurned += 0.1;

	}

	private void move() {

		// rekent maxSpeed uit.
		maxSpeed = (10 * creatureSize) / weight;

		deltaSpeed *= (maxSpeed * 0.05d); // 0.05d hoeveel procent van maxSpeed er per move bij kan komen.

		// rekent speed uit
		if (speed + deltaSpeed >= maxSpeed) {
			speed = maxSpeed;
		} else if (speed + deltaSpeed <= 0) {
			speed = 0;
		} else {
			speed = speed + deltaSpeed;
		}

		// Rekent nieuwe kijkrichting/beweegrichting uit.
		// het is nu mogelijk om 180 graden draai te maken en alle snelheid te houden
		// onrealistisch!

		direction += (deltaDirection * 360);
		direction %= 360;

		deltaXPos = Math.sin((Math.PI * direction) / 180) * speed;
		deltaYPos = Math.cos((Math.PI * direction) / 180) * speed;

		// Kijkt of de move binnen het veld blijft en voert uit.
		if (xPos + deltaXPos - (creatureSize / 2) > 0 && xPos + deltaXPos
				+ (creatureSize / 2) < Configuration.DEFAULT_MAP_SIZE_IN_TILES * Configuration.DEFAULT_TILE_SIZE) {
			xPos = xPos + deltaXPos;
		}

		if (yPos + deltaYPos - (creatureSize / 2) > 0 && yPos + deltaYPos
				+ (creatureSize / 2) < Configuration.DEFAULT_MAP_SIZE_IN_TILES * Configuration.DEFAULT_TILE_SIZE) {
			yPos = yPos + deltaYPos;
		}

		// hoeveel vet creature verbrandt met de beweging. Later exp functie van maken.
		fatBurned += speed * weight;

	}

	private void giveBirth() {

	}

	private void proccesTurn() {

		fat -= fatBurned * age; // *age om oudere creatures een nadeel te geven dit verbeterd als het goed is de
								// creatures sneller door een kans te geven aan nieuwe creature
		fatBurned = 0;

		if (fat <= 0) {

			isDead = true;

		} else {
			age += 0.01;
		}

	}

	void draw(Graphics2D g2d) {

		g2d.setColor(creatureColor);
		g2d.fill(creatureShape);
		g2d.setColor(Color.BLACK);
		g2d.draw(creatureShape);

		double radianDirection, forwardX, forwardY;
		radianDirection = Math.toRadians(direction);
		forwardX = Math.sin(radianDirection);
		forwardY = Math.cos(radianDirection);

		g2d.draw(new Line2D.Double( xPos,  yPos,  forwardX, forwardY));
		
	}
}
