package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class Creature {

	private long creatureID; // ID om creature aan te herkennen
	private long parentAID, parentBID; // ID parents

	private double age;
	private double fat, weight, fatBurned; // Voedsel vooraad
	private double desiredFood, foodInMouth;

	private double xPos, deltaXPos, deltaYPos, yPos, direction; // Positie en draaing
	private double speed, maxSpeed;

	private double creatureSize;
	private int xTile, yTile;
	private boolean isDead = false;

	private Board board;

	// Door brain bepaalt
	private double deltaDirection, deltaSpeed; // waarde tussen -1 en 1

	Color creatureColor;
	Ellipse2D creatureShape;

	public Creature(double x, double y, Board brd, int creatureNumber) {

		board = brd;

		creatureID = creatureNumber;

		xPos = x;
		yPos = y;

		direction = Math.random() * 360;

		age = 0;
		fat = 10;
		creatureSize = Configuration.DEFAULT_CREATURE_SIZE;
		// weight = fat * creatureSize;

		creatureColor = new Color(0f, 1f, 0f);
		creatureShape = new Ellipse2D.Double(xPos - (creatureSize / 2), yPos - (creatureSize / 2), creatureSize,
				creatureSize);
	}

	// private void born(long parentA, long parentB) {
	//
	// age = 0;
	// fat = 10;
	// }

	public void prepareCreature() {

		// huidige tile waar die staat
		xTile = (int) Math.round((xPos / Configuration.tileSize) - 0.5d);
		yTile = (int) Math.round((yPos / Configuration.tileSize) - 0.5d);

	}

	public void eat() {
		this.prepareCreature();

		System.out.println("eating..");

		desiredFood = 1d; // later door brain bepaalt 1 is max 0 is min

		foodInMouth = board.getMap().getTiles()[xTile][yTile].eatFoodTile(desiredFood);
		//System.out.println("Food in mouth = " + foodInMouth);
		// System.out.println("food from tile " +
		// board.getMap().getTiles()[xTile][yTile].eatFoodTile(desiredFood));

		// System.out.println("food in mouth " + foodInMouth);

		fat += foodInMouth;
		foodInMouth = 0;

		// System.out.println(fat);

		fat -= 1;

		System.out.println("Done eating");



	}

	public void move() {

		System.out.println("Moving...");

		deltaSpeed = Math.random() * 2 -1;
		deltaDirection = Math.random() * 2 -1;

		// rekent maxSpeed uit.
		maxSpeed = (0.25 * creatureSize);

		// deltaSpeed *= (maxSpeed * 0.05d); // 0.05d hoeveel procent van maxSpeed er
		// per move bij kan komen.

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
		deltaDirection *= 360;
		direction += deltaDirection;
		direction %= 360;

		deltaXPos = Math.sin(Math.toRadians(direction)) * speed;
		deltaYPos = Math.cos(Math.toRadians(direction)) * speed;

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

		System.out.println("Moved");

	}

	public void processTurn() {

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

		creatureShape.setFrame(xPos - (creatureSize / 2), yPos - (creatureSize / 2), creatureSize, creatureSize);

		g2d.setColor(creatureColor);
		g2d.fill(creatureShape);
		g2d.setColor(Color.BLACK);
		g2d.draw(creatureShape);

		double radianDirection, forwardX, forwardY;
		radianDirection = Math.toRadians(direction);
		forwardX = Math.sin(radianDirection) * creatureSize;
		forwardY = Math.cos(radianDirection) * creatureSize;

		g2d.draw(new Line2D.Double(xPos, yPos, xPos + forwardX, yPos + forwardY));

	}

	public Ellipse2D getCreatureShape() {
		return creatureShape;
	}

	public void setCreatureShape(Ellipse2D creatureShape) {
		this.creatureShape = creatureShape;
	}

	public long getCreatureID() {
		return creatureID;
	}

	public void setCreatureID(long creatureID) {
		this.creatureID = creatureID;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
