package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class Creature {

	private static final double EAT_EFFICIENCY_STEEPNESS = 2;
	private final long creatureID; // ID om creature aan te herkennen
	private Creature parent; // de parent van deze creature
	private Brain brain;
	private double[] brainOutputs;

	private double age;
	private double totalFoodEaten;
	private double fitness;
	private double fat, weight, fatBurned; // Voedsel vooraad
	private double actualFoodAmount, foodInMouth;
	private double eatEfficiency;

	private double xPos, deltaXPos, deltaYPos, yPos, direction; // Positie en draaing
	private double speed, maxSpeed;

	private double creatureSize;
	private int xTile, yTile;
	private boolean isDead = false;

	private Color leftEyeColor, rightEyeColor;
	private double eyeDeviation, eyeLength, rightEyeX, rightEyeY, leftEyeX, leftEyeY;
	private Eye eye;

	private Board board;

	// Door brain bepaalt
	private double deltaDirection, deltaSpeed; // waarde tussen -1 en 1

	private Color creatureColor;
	Ellipse2D creatureShape;

	private boolean selected;
	private boolean controlled;
	private int amountOfChilderen;

	Creature(double x, double y, Board brd, int creatureNumber) {

		board = brd;

		creatureID = creatureNumber;

		setXPos(x);
		setYPos(y);

		direction = Math.random() * 360;

		eyeDeviation = 45;

		age = 0;
		fat = 1000;
		creatureSize = Configuration.DEFAULT_CREATURE_SIZE;
		eyeLength = Configuration.DEFAULT_EYE_LENGTH;

		setCreatureColor(new Color(0f, 1f, 0f));
		creatureShape = new Ellipse2D.Double(getXPos() - (creatureSize / 2), getYPos() - (creatureSize / 2),
				creatureSize, creatureSize);
		brain = new Brain(20, 5, this);
		brainOutputs = new double[20];
		eye = new Eye(this, this.board, this.eyeLength, eyeDeviation);
	}

	Creature(Creature parentCreature, double x, double y, int generation, int creatureNumber, double deviation) {
		parent = parentCreature;
		board = parent.getBoard();

		creatureID = creatureNumber + Configuration.BEGIN_AMOUNT_CREATURES * generation;

		setXPos(x);
		setYPos(y);

		direction = Math.random() * 360;

		eyeDeviation = 45;

		age = 0;
		fat = 1000;
		creatureSize = Configuration.DEFAULT_CREATURE_SIZE;
		eyeLength = Configuration.DEFAULT_EYE_LENGTH;

		setCreatureColor(new Color(0f, 1f, 0f));
		creatureShape = new Ellipse2D.Double(getXPos() - (creatureSize / 2), getYPos() - (creatureSize / 2),
				creatureSize, creatureSize);
		brain = new Brain(parentCreature.getBrain(), this, deviation);
		brainOutputs = new double[20];
		eye = new Eye(this, this.board, this.eyeLength, eyeDeviation);
	}

	public void doStep() {
		this.beginStep();
		this.brainStep();
		this.actionStep();
		this.endStep();
	}

	public void doStep(double deltaSpeed, double deltaDirection, double amount) {
		eye.look();
		move(deltaSpeed, deltaDirection);
		eat(amount);
		this.endStep();
	}

	public void beginStep() {
		fatBurned = 0;
		eye.look();
	}

	public void brainStep() {
		brain.generateInputs();
		brainOutputs = brain.feedForward();

	}

	private void actionStep() {
		this.move(brainOutputs[0], brainOutputs[1]);
		this.eat(brainOutputs[2]);
		creatureColor = new Color((float) brainOutputs[3] / 2 + .5f, (float) brainOutputs[4] / 2 + .5f,
				(float) brainOutputs[5] / 2 + .5f);
	}

	static public int posToTile(double x) {
		return (int) Math.round((x / Configuration.tileSize) - 0.5d);
	}

	public void eat(double desiredFoodAmount) {
		xTile = Creature.posToTile(getXPos());
		yTile = Creature.posToTile(getYPos());
		desiredFoodAmount = Math.max(desiredFoodAmount, 0);
		
		if (desiredFoodAmount != 0) {
			eatEfficiency = 1/(EAT_EFFICIENCY_STEEPNESS * speed + 1);
		}
		
		actualFoodAmount = desiredFoodAmount * eatEfficiency;
		
		foodInMouth = board.getMap().getTiles()[xTile][yTile].eatFoodTile(actualFoodAmount);
		fat += foodInMouth;
		setTotalFoodEaten(getTotalFoodEaten() + foodInMouth);
		foodInMouth = 0;
		fatBurned += desiredFoodAmount;
	}

	public void move(double deltaSpeed, double deltaDirection) {

		// rekent maxSpeed uit.
		maxSpeed = (0.25 * creatureSize);

		// rekent speed uit
		if (speed + deltaSpeed >= maxSpeed) {
			speed = maxSpeed;
		} else if (speed + deltaSpeed <= 0) {
			speed = 0;
		} else {
			speed = speed + deltaSpeed;
		}

		// Rekent nieuwe kijkrichting/beweegrichting uit.
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
	}

	public void endStep() {

		fat -= 1 + fatBurned * age * age; // *age om oudere creatures een nadeel te geven dit verbeterd als het goed is
											// de
		// creatures sneller door een kans te geven aan nieuwe creature

		if (fat <= 0) {

			isDead = true;

		} else {
			age += 0.01d;
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
		g2d.draw(new Line2D.Double(getXPos(), getYPos(), eye.getRightX(), eye.getRightY()));
		g2d.setColor(Color.BLUE);
		g2d.draw(new Line2D.Double(getXPos(), getYPos(), eye.getLeftX(), eye.getLeftY()));
	}

	public double getFitness() {
		fitness = age * getTotalFoodEaten();
		return fitness;
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

	public double getTotalFoodEaten() {
		return totalFoodEaten;
	}

	public void setTotalFoodEaten(double totalFoodEaten) {
		this.totalFoodEaten = totalFoodEaten;
	}

	public double[] getBrainOutputs() {
		return brainOutputs;
	}

	public double getBrainOutputs(int i) {
		return brainOutputs[i];
	}

	public void setBrainOutputs(double[] brainOutputs) {
		this.brainOutputs = brainOutputs;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Brain getBrain() {
		return brain;
	}

	public void setBrain(Brain brain) {
		this.brain = brain;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getFatBurned() {
		return fatBurned;
	}

	public void setFatBurned(double fatBurned) {
		this.fatBurned = fatBurned;
	}

	public double getDesiredFood() {
		return actualFoodAmount;
	}

	public void setDesiredFood(double desiredFood) {
		this.actualFoodAmount = desiredFood;
	}

	public double getFoodInMouth() {
		return foodInMouth;
	}

	public void setFoodInMouth(double foodInMouth) {
		this.foodInMouth = foodInMouth;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getDeltaXPos() {
		return deltaXPos;
	}

	public void setDeltaXPos(double deltaXPos) {
		this.deltaXPos = deltaXPos;
	}

	public double getDeltaYPos() {
		return deltaYPos;
	}

	public void setDeltaYPos(double deltaYPos) {
		this.deltaYPos = deltaYPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getCreatureSize() {
		return creatureSize;
	}

	public void setCreatureSize(double creatureSize) {
		this.creatureSize = creatureSize;
	}

	public int getxTile() {
		return xTile;
	}

	public void setxTile(int xTile) {
		this.xTile = xTile;
	}

	public int getyTile() {
		return yTile;
	}

	public void setyTile(int yTile) {
		this.yTile = yTile;
	}

	public double getEyeDeviation() {
		return eyeDeviation;
	}

	public void setEyeDeviation(double eyeDeviation) {
		this.eyeDeviation = eyeDeviation;
	}

	public double getEyeLength() {
		return eyeLength;
	}

	public void setEyeLength(double eyeLength) {
		this.eyeLength = eyeLength;
	}

	public double getRightEyeX() {
		return rightEyeX;
	}

	public void setRightEyeX(double rightEyeX) {
		this.rightEyeX = rightEyeX;
	}

	public double getRightEyeY() {
		return rightEyeY;
	}

	public void setRightEyeY(double rightEyeY) {
		this.rightEyeY = rightEyeY;
	}

	public double getLeftEyeX() {
		return leftEyeX;
	}

	public void setLeftEyeX(double leftEyeX) {
		this.leftEyeX = leftEyeX;
	}

	public double getLeftEyeY() {
		return leftEyeY;
	}

	public void setLeftEyeY(double leftEyeY) {
		this.leftEyeY = leftEyeY;
	}

	public Eye getEye() {
		return eye;
	}

	public void setEye(Eye eye) {
		this.eye = eye;
	}

	public double getDeltaDirection() {
		return deltaDirection;
	}

	public void setDeltaDirection(double deltaDirection) {
		this.deltaDirection = deltaDirection;
	}

	public double getDeltaSpeed() {
		return deltaSpeed;
	}

	public void setDeltaSpeed(double deltaSpeed) {
		this.deltaSpeed = deltaSpeed;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public int getAmountOfChilderen() {
		return amountOfChilderen;
	}

	public void setAmountOfChilderen(int amountOfChilderen) {
		this.amountOfChilderen = amountOfChilderen;
	}
}
