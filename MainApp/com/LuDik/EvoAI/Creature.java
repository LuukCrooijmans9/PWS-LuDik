package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class Creature {

	private long creatureID; // ID om creature aan te herkennen
	private long parentAID, parentBID; // ID parents
	private Brain brain;
	private double[] brainOutputs;

	private double age;
	private double fat, weight, fatBurned; // Voedsel vooraad
	private double desiredFood, foodInMouth;

	private double xPos, deltaXPos, deltaYPos, yPos, direction; // Positie en draaing
	private double speed, maxSpeed;

	private double creatureSize;
	private int xTile, yTile;
	private boolean isDead = false;

	private Color leftEyeColor, rightEyeColor;
	private double eyeDeviation, eyeLength, rightEyeX, rightEyeY, leftEyeX, leftEyeY;

	private Board board;

	// Door brain bepaalt
	private double deltaDirection, deltaSpeed; // waarde tussen -1 en 1

	private Color creatureColor;
	Ellipse2D creatureShape;

	private boolean selected;
	private boolean controlled;

	public Creature(double x, double y, Board brd, int creatureNumber) {

		board = brd;

		creatureID = creatureNumber;

		setXPos(x);
		setYPos(y);

		direction = Math.random() * 360;

		eyeDeviation = 45;

		age = 0;
		fat = 10;
		creatureSize = Configuration.DEFAULT_CREATURE_SIZE;
		eyeLength = Configuration.DEFAULT_EYE_LENGTH;
		// weight = fat * creatureSize;

		setCreatureColor(new Color(0f, 1f, 0f));
		creatureShape = new Ellipse2D.Double(getXPos() - (creatureSize / 2), getYPos() - (creatureSize / 2),
				creatureSize, creatureSize);
		brain = new Brain(12, 3, this);
		brainOutputs = new double[12];

	}

	public void doStep() {
		this.beginStep();
		this.brainStep();
		this.endStep();
	}

	public void doStep(double deltaSpeed, double deltaDirection, double amount) {
		move(deltaSpeed, deltaDirection);
		eat(amount);
	}

	public void beginStep() {
		this.look();
	}

	public void brainStep() {
		brain.generateInputs();
		brainOutputs = brain.feedForward();

		this.move(brainOutputs[0], brainOutputs[1]);
		this.eat(brainOutputs[2]);
		creatureColor = new Color((float) brainOutputs[3] / 2 + .5f, (float) brainOutputs[4] / 2 + .5f,
				(float) brainOutputs[5] / 2 + .5f);
	}

	public int posToTile(double x) {
		return (int) Math.round((x / Configuration.tileSize) - 0.5d);
	}

	public void eat(double amount) {
		xTile = this.posToTile(getXPos());
		yTile = this.posToTile(getYPos());

		// System.out.println("eating..");

		desiredFood = amount;

		foodInMouth = board.getMap().getTiles()[xTile][yTile].eatFoodTile(desiredFood);
		// System.out.println("Food in mouth = " + foodInMouth);
		// System.out.println("food from tile " +
		// board.getMap().getTiles()[xTile][yTile].eatFoodTile(desiredFood));

		// System.out.println("food in mouth " + foodInMouth);

		fat += foodInMouth;
		foodInMouth = 0;

		// System.out.println(fat);

		fat -= 1;

		// System.out.println("Done eating");

	}

	public void move(double deltaSpeed, double deltaDirection) {

		// System.out.println("Moving...");

		// this.deltaSpeed = Math.random() * 2 - 1;
		// this.deltaDirection = Math.random() * 2 - 1;

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
		deltaDirection *= 10;
		direction -= deltaDirection;
		direction %= 360;

		deltaXPos = Math.sin(Math.toRadians(direction)) * speed;
		deltaYPos = Math.cos(Math.toRadians(direction)) * speed;

		// Kijkt of de move binnen het veld blijft en voert uit.
		if (getXPos() + deltaXPos - (creatureSize / 2) > 0 && getXPos() + deltaXPos
				+ (creatureSize / 2) < Configuration.DEFAULT_MAP_SIZE_IN_TILES * Configuration.DEFAULT_TILE_SIZE) {
			setXPos(getXPos() + deltaXPos);
		}

		if (getYPos() + deltaYPos - (creatureSize / 2) > 0 && getYPos() + deltaYPos
				+ (creatureSize / 2) < Configuration.DEFAULT_MAP_SIZE_IN_TILES * Configuration.DEFAULT_TILE_SIZE) {
			setYPos(getYPos() + deltaYPos);
		}

		// hoeveel vet creature verbrandt met de beweging. Later exp functie van maken.
		fatBurned += speed * weight;

		// System.out.println("Moved");

	}

	public void look() {

		double rightRadianDirection, leftRadianDirection;
		rightRadianDirection = Math.toRadians(direction - eyeDeviation);
		rightEyeX = (Math.sin(rightRadianDirection) * eyeLength) + getXPos();
		rightEyeY = (Math.cos(rightRadianDirection) * eyeLength) + getYPos();

		leftRadianDirection = Math.toRadians(direction + eyeDeviation);
		leftEyeX = (Math.sin(leftRadianDirection) * eyeLength) + getXPos();
		leftEyeY = (Math.cos(leftRadianDirection) * eyeLength) + getYPos();

		int xRightEyeTile = posToTile(rightEyeX);
		int yRightEyeTile = posToTile(rightEyeY);
		int xLeftEyeTile = posToTile(leftEyeX);
		int yLeftEyeTile = posToTile(leftEyeY);

		if (xRightEyeTile < 0 || yRightEyeTile < 0 || xRightEyeTile > Configuration.DEFAULT_MAP_SIZE_IN_TILES - 1
				|| yRightEyeTile > Configuration.DEFAULT_MAP_SIZE_IN_TILES - 1) {

			setRightEyeColor(Color.WHITE);

		} else if (xLeftEyeTile < 0 || yLeftEyeTile < 0 || xLeftEyeTile > Configuration.DEFAULT_MAP_SIZE_IN_TILES - 1
				|| yLeftEyeTile > Configuration.DEFAULT_MAP_SIZE_IN_TILES - 1) {

			setLeftEyeColor(Color.WHITE);

		} else {
			setRightEyeColor(board.getMap().getTiles()[xRightEyeTile][yRightEyeTile].getTileColor());
			setLeftEyeColor(board.getMap().getTiles()[xLeftEyeTile][yLeftEyeTile].getTileColor());
		}

	}

	public void endStep() {

		fat -= fatBurned * age; // *age om oudere creatures een nadeel te geven dit verbeterd als het goed is de
								// creatures sneller door een kans te geven aan nieuwe creature
		fatBurned = 0;

		if (fat <= 0) {

			// isDead = true;

		} else {
			age += 0.01;
		}

	}

	void draw(Graphics2D g2d) {

		creatureShape.setFrame(getXPos() - (creatureSize / 2), getYPos() - (creatureSize / 2), creatureSize,
				creatureSize);

		if (!selected)
			g2d.setColor(getCreatureColor());
		else
			g2d.setColor(Color.blue);

		g2d.fill(creatureShape);
		g2d.setColor(Color.BLACK);
		g2d.draw(creatureShape);

		double radianDirection, forwardX, forwardY;
		radianDirection = Math.toRadians(direction);
		forwardX = Math.sin(radianDirection) * creatureSize;
		forwardY = Math.cos(radianDirection) * creatureSize;

		g2d.draw(new Line2D.Double(getXPos(), getYPos(), getXPos() + forwardX, getYPos() + forwardY));

		g2d.setColor(Color.RED);
		g2d.draw(new Line2D.Double(getXPos(), getYPos(), rightEyeX, rightEyeY));
		g2d.setColor(Color.BLUE);
		g2d.draw(new Line2D.Double(getXPos(), getYPos(), leftEyeX, leftEyeY));
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

	public double getXPos() {
		return xPos;
	}

	public void setXPos(double xPos) {
		this.xPos = xPos;
	}

	public double getYPos() {
		return yPos;
	}

	public void setYPos(double yPos) {
		this.yPos = yPos;
	}

	public Color getCreatureColor() {
		return creatureColor;
	}

	public void setCreatureColor(Color creatureColor) {
		this.creatureColor = creatureColor;
	}

	public boolean selected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isControlled() {
		return controlled;
	}

	public void setControlled(boolean controlled) {
		this.controlled = controlled;
	}

	public Color getLeftEyeColor() {
		return leftEyeColor;
	}

	public void setLeftEyeColor(Color leftEyeColor) {
		this.leftEyeColor = leftEyeColor;
	}

	public Color getRightEyeColor() {
		return rightEyeColor;
	}

	public void setRightEyeColor(Color rightEyeColor) {
		this.rightEyeColor = rightEyeColor;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public double getFat() {
		return fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
