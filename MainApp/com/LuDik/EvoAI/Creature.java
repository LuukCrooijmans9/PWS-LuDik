package com.LuDik.EvoAI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 
 * This class contains the body and the action of the creature.
 * 
 */

public class Creature {

	// Copied from ConfigSingleton.INSTANCE
	private static final double EAT_EFFICIENCY_STEEPNESS = ConfigSingleton.INSTANCE.eatEfficiencySteepness;
	private static final double WEIGHT_PER_FAT = ConfigSingleton.INSTANCE.weightPerFat;
	private static final double WEIGHT_PER_WATER = ConfigSingleton.INSTANCE.weightPerWater;
	private static final double BASE_FAT_CONSUMPTION = ConfigSingleton.INSTANCE.baseFatConsumption;
	public static final double DEFAULT_MAX_FOOD_IN_MOUTH = ConfigSingleton.INSTANCE.maxFoodInMouth;
	private static final int DEFAULT_BRAIN_WIDTH = ConfigSingleton.INSTANCE.brainWidth;
	private static final int DEFAULT_INPUT_HEIGHT = ConfigSingleton.INSTANCE.inputLayerHeight;
	private static final int DEFAULT_HIDDEN_HEIGHT = ConfigSingleton.INSTANCE.hiddenLayerHeight;
	private static final double BASE_WATER_CONSUMPTION = ConfigSingleton.INSTANCE.baseWaterConsumption;

	private final long creatureID;
	private Creature parent;

	// The brain and it's outputs are stored in these variables.
	private Brain brain;
	private double[] brainOutputs;

	private double age;
	private double fitness;

	private double totalFoodEaten;
	private double fat, weight, fatBurned;
	private double water, actualWaterAmount, waterInMouth;
	private double totalWaterDrunk;
	private double actualFoodAmount, foodInMouth;
	private double eatEfficiency;

	// Position, speed and direction of this creature and the changes.
	private double xPos, deltaXPos, deltaYPos, yPos, direction;
	private double speed, maxSpeed;
	private double deltaDirection, deltaSpeed;
	private double totalDistanceTravelled;

	private double creatureSize;
	private int xTile, yTile;
	private boolean isMature = false;
	private boolean isDead = false;

	// It's eyes

	private Color rightEyeColor, centerEyeColor, leftEyeColor;
	private double eyeDeviation, eyeLength, rightEyeX, rightEyeY, leftEyeX, leftEyeY;
	private Eye eye;

	// Where it is ALIVE
	private Board board;

	// Visuals
	private Color creatureColor;
	Ellipse2D creatureShape;
	private boolean selected;
	private boolean controlled;
	private int generation;
	private int amountOfChildren;
	private ArrayList<Creature> children = new ArrayList<Creature>();

	// Translates the x and y values to the x and y values of the tile they are in.
	static public int posToTile(double x) {
		int tile = (int) Math.round((x / ConfigSingleton.INSTANCE.tileSize) - 0.5d);
		if (tile < 0) {
			tile = 0;
		} else if (tile >= ConfigSingleton.INSTANCE.mapSizeInTiles) {
			tile = ConfigSingleton.INSTANCE.mapSizeInTiles - 1;
		}
		return tile;

	}

	// Totally random creature
	Creature(double x, double y, Board brd, int creatureNumber) {

		board = brd;

		creatureID = creatureNumber;

		setXPos(x);
		setYPos(y);
		direction = board.randomDouble() * 360;

		age = 0;
		fat = ConfigSingleton.INSTANCE.startingFat;
		water = ConfigSingleton.INSTANCE.startingWater;

		creatureSize = ConfigSingleton.INSTANCE.crtrSize;
		eyeLength = ConfigSingleton.INSTANCE.eyeLength;
		eyeDeviation = ConfigSingleton.INSTANCE.eyeDeviation;

		setCreatureColor(new Color(0f, 1f, 0f));
		creatureShape = new Ellipse2D.Double(getXPos() - (creatureSize / 2), getYPos() - (creatureSize / 2),
				creatureSize, creatureSize);
		brain = new Brain(DEFAULT_INPUT_HEIGHT, DEFAULT_HIDDEN_HEIGHT, DEFAULT_BRAIN_WIDTH, this);
		brainOutputs = new double[brain.getHeight()];
		eye = new Eye(this, this.board, this.eyeLength, eyeDeviation);
	}

	// Creature based of 1 parent
	Creature(Creature parentCreature, double x, double y, int creatureNumber, double deviation) {
		parent = parentCreature;
		board = parent.getBoard();
		generation = parent.getGeneration();

		// TODO CreatureID remake
		creatureID = creatureNumber + ConfigSingleton.INSTANCE.beginAmountCreatures * generation;

		setXPos(x);
		setYPos(y);
		direction = board.randomDouble() * 360;

		age = 0;
		fat = ConfigSingleton.INSTANCE.startingFat;
		water = ConfigSingleton.INSTANCE.startingWater;

		creatureSize = ConfigSingleton.INSTANCE.crtrSize;
		eyeLength = ConfigSingleton.INSTANCE.eyeLength;
		eyeDeviation = ConfigSingleton.INSTANCE.eyeDeviation;

		setCreatureColor(new Color(0f, 1f, 0f));
		creatureShape = new Ellipse2D.Double(getXPos() - (creatureSize / 2), getYPos() - (creatureSize / 2),
				creatureSize, creatureSize);
		brain = new Brain(parentCreature.getBrain(), this, deviation);
		brainOutputs = new double[brain.getHeight()];
		eye = new Eye(this, this.board, this.eyeLength, eyeDeviation);
	}

	// notification for creature to do its stuff.
	public void doStep() {
		this.beginStep();
		this.brainStep();
		this.actionStep();
		this.endStep();
	}

	// If we want to control it it does stuff based on the inputs.
	public void doStep(double deltaSpeed, double deltaDirection, double amount) {
		this.beginStep();
		move(deltaSpeed, deltaDirection);
		eat(amount);
		this.endStep();
	}

	// Some things that need to be done before everything else.
	public void beginStep() {
		fatBurned = 0;
		eye.look();
		xTile = Creature.posToTile(getXPos());
		yTile = Creature.posToTile(getYPos());
		if (!isMature) {
			if (age / ConfigSingleton.INSTANCE.agePerStep >= ConfigSingleton.INSTANCE.maturityAge) {
				isMature = true;
			}
		}
	}

	// The brain does it's magic here
	public void brainStep() {
		brain.generateInputs();
		brainOutputs = brain.feedForward();

	}

	// The creature takes action based on the outputs of the brain.
	private void actionStep() {
		this.move(brainOutputs[0], brainOutputs[1]);
		this.eat(brainOutputs[2]);
		creatureColor = new Color((float) brainOutputs[3] / 2 + .5f, (float) brainOutputs[4] / 2 + .5f,
				(float) brainOutputs[5] / 2 + .5f);
		if (ConfigSingleton.INSTANCE.needDrinking) {
			this.drink(brainOutputs[6]);
		}
		this.giveBirth(brainOutputs[6]);
	}

	// Gathering food from the tile it is on.
	public void eat(double desiredFoodAmount) {

		desiredFoodAmount = Math.max(desiredFoodAmount, 0);

		if (desiredFoodAmount != 0) {
			eatEfficiency = 1 / (EAT_EFFICIENCY_STEEPNESS * speed + 1);
			actualFoodAmount = desiredFoodAmount * eatEfficiency * DEFAULT_MAX_FOOD_IN_MOUTH;
			foodInMouth = board.getMap().getTiles()[xTile][yTile].eatFoodTile(actualFoodAmount);
			fat += foodInMouth;
			setTotalFoodEaten(getTotalFoodEaten() + foodInMouth);
			foodInMouth = 0;
			fatBurned += desiredFoodAmount * 0.1;
		}
	}

	// Drinking water from the tile it is on.
	public void drink(double desiredDrinkAmount) {

		desiredDrinkAmount = Math.max(desiredDrinkAmount, 0);

		if (desiredDrinkAmount != 0) {
			actualWaterAmount = desiredDrinkAmount;
			waterInMouth = board.getMap().getTiles()[xTile][yTile].drinkWaterTile(actualWaterAmount);
			water += waterInMouth;
			setTotalWaterDrunk(getTotalWaterDrunk() + waterInMouth);
			waterInMouth = 0;
		}
	}

	// Moves the creature.
	public void move(double deltaSpeed, double deltaDirection) {

		maxSpeed = ConfigSingleton.INSTANCE.maxSpeed;

		// Calculates new speed
		if (speed + deltaSpeed >= maxSpeed) {
			speed = maxSpeed;
		} else if (speed + deltaSpeed <= 0) {
			speed = 0;
		} else {
			speed = speed + deltaSpeed;
		}

		// Calculates new direction
		deltaDirection *= ConfigSingleton.INSTANCE.maxDeltaDirection;
		// if(deltaDirection > ConfigSingleton.INSTANCE.maxDeltaDirection) {
		// System.out.println(deltaDirection);
		// }
		direction -= deltaDirection;
		direction %= 360;

		deltaXPos = Math.sin(Math.toRadians(direction)) * speed;
		deltaYPos = Math.cos(Math.toRadians(direction)) * speed;

		// Checks if the move is not invalid (Does it stay within the borders of the
		// field)
		if (getXPos() + deltaXPos - (creatureSize / 2) > 0 && getXPos() + deltaXPos
				+ (creatureSize / 2) < ConfigSingleton.INSTANCE.mapSizeInTiles * ConfigSingleton.INSTANCE.tileSize) {
			setXPos(getXPos() + deltaXPos);
		} else if (getXPos() + deltaXPos - (creatureSize / 2) <= 0) {
			setXPos(0);
		} else {
			setXPos((ConfigSingleton.INSTANCE.mapSizeInTiles * ConfigSingleton.INSTANCE.tileSize) - 0.1);
		}

		if (getYPos() + deltaYPos - (creatureSize / 2) > 0 && getYPos() + deltaYPos
				+ (creatureSize / 2) < ConfigSingleton.INSTANCE.mapSizeInTiles * ConfigSingleton.INSTANCE.tileSize) {
			setYPos(getYPos() + deltaYPos);
		} else if (getYPos() + deltaYPos - (creatureSize / 2) <= 0) {
			setYPos(0);
		} else {
			setYPos((ConfigSingleton.INSTANCE.mapSizeInTiles * ConfigSingleton.INSTANCE.tileSize) - 0.1);
		}

		// Statistics
		setTotalDistanceTravelled(getTotalDistanceTravelled() + speed);

		// Calculates the fatBurned.
		fatBurned += speed * weight;
		fatBurned += Math.abs(deltaDirection) * weight;
	}

	public void giveBirth(double willingness) {
		if (isMature & willingness > 0 & fat > 2 * ConfigSingleton.INSTANCE.startingFat) {
			fat -= ConfigSingleton.INSTANCE.startingFat;
			Creature crtr = board.spawnSingleParentCreature(this, ConfigSingleton.INSTANCE.evolutionFactor);
			children.add(crtr);
			amountOfChildren++;
		}
	}

	// Finishes this step and checks if the creature survived this day
	public void endStep() {

		fat -= BASE_FAT_CONSUMPTION + ((age * age) / 1000);

		if (ConfigSingleton.INSTANCE.needDrinking) {
			water -= BASE_WATER_CONSUMPTION;
			weight = fat * WEIGHT_PER_FAT + water * WEIGHT_PER_WATER;
		} else {
			weight = fat * WEIGHT_PER_FAT;
		}
		if (fat <= 0) {
			isDead = true; // He was a loving Father to us all
		} else {
			age += ConfigSingleton.INSTANCE.agePerStep;
		}

	}

	public double[] funeral() {
		parent = null;
		double[] accomplishments = new double[4];
		accomplishments[0] = amountOfChildren;
		accomplishments[1] = age;
		accomplishments[2] = fitness;
		accomplishments[3] = totalFoodEaten;
		return accomplishments;
	}

	// Draws the creature
	void draw(Graphics2D g2d) {

		/**
		 * Draws the red circle around the selected creature:
		 */

		if (selected) {
			g2d.setPaint(Color.red);
			BasicStroke stroke = (BasicStroke) g2d.getStroke();
			g2d.setStroke(new BasicStroke(2));
			g2d.draw(new Ellipse2D.Double(getXPos() - (creatureSize), getYPos() - (creatureSize), 2 * creatureSize,
					2 * creatureSize));
			g2d.setStroke(stroke);
		}

		/**
		 * Draws the creature itself (body, orientation line, eyes):
		 */

		g2d.setPaint(Color.black);
		creatureShape.setFrame(getXPos() - (creatureSize / 2), getYPos() - (creatureSize / 2), creatureSize,
				creatureSize);

		g2d.setColor(getCreatureColor());

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

	// Calculates the fitness
	public double getFitness() {
		fitness = getTotalFoodEaten() + 100 * age;
		return fitness;
	}

	public Creature getParent() {
		return parent;
	}

	public void setParent(Creature parent) {
		this.parent = parent;
	}

	public double getWater() {
		return water;
	}

	public void setWater(double water) {
		this.water = water;
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
		return amountOfChildren;
	}

	public void setAmountOfChilderen(int amountOfChilderen) {
		this.amountOfChildren = amountOfChilderen;
	}

	public double getTotalDistanceTravelled() {
		return totalDistanceTravelled;
	}

	public void setTotalDistanceTravelled(double totalDistanceTravelled) {
		this.totalDistanceTravelled = totalDistanceTravelled;
	}

	public double getTotalWaterDrunk() {
		return totalWaterDrunk;
	}

	public void setTotalWaterDrunk(double totalWaterDrunk) {
		this.totalWaterDrunk = totalWaterDrunk;
	}

	public Color getCenterEyeColor() {
		return centerEyeColor;
	}

	public void setCenterEyeColor(Color centerEyeColor) {
		this.centerEyeColor = centerEyeColor;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

}
