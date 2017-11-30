package com.LuDik.EvoAI;

import java.awt.Color;

public class Eye {
	transient Creature creature;
	transient Board board;
	double eyeLength, eyeDeviation, rightX, rightY, leftX, leftY;
	int xTile, yTile;
	Color rightEyeColor, centerEyeColor, leftEyeColor;

	Eye(Creature creature, Board board, double eyeLength, double eyeDeviation) {
		this.creature = creature;
		this.board = board;
		this.eyeLength = eyeLength;
		this.eyeDeviation = eyeDeviation;
		this.look();
	}

	void look() {
		double rightRadianDirection, leftRadianDirection;
		rightRadianDirection = Math.toRadians(creature.getDirection() - eyeDeviation);
		rightX = (Math.sin(rightRadianDirection) * eyeLength) + creature.getXPos();
		rightY = (Math.cos(rightRadianDirection) * eyeLength) + creature.getYPos();

		xTile = Creature.posToTile(rightX);
		yTile = Creature.posToTile(rightY);

		if (xTile < 0 || yTile < 0 || xTile > ConfigSingleton.INSTANCE.mapSizeInTiles - 1
				|| yTile > ConfigSingleton.INSTANCE.mapSizeInTiles - 1) {
			rightEyeColor = Color.WHITE;
		} else {
			creature.setRightEyeColor(board.getMap().getTiles()[xTile][yTile].getTileColor());
		}

		leftRadianDirection = Math.toRadians(creature.getDirection() + eyeDeviation);
		leftX = (Math.sin(leftRadianDirection) * eyeLength) + creature.getXPos();
		leftY = (Math.cos(leftRadianDirection) * eyeLength) + creature.getYPos();

		xTile = Creature.posToTile(leftX);
		yTile = Creature.posToTile(leftY);

		if (xTile < 0 || yTile < 0 || xTile > ConfigSingleton.INSTANCE.mapSizeInTiles - 1
				|| yTile > ConfigSingleton.INSTANCE.mapSizeInTiles - 1) {
			creature.setLeftEyeColor(Color.WHITE);
		} else {
			creature.setLeftEyeColor(board.getMap().getTiles()[xTile][yTile].getTileColor());
		}

		xTile = creature.getxTile();
		yTile = creature.getyTile();
		creature.setCenterEyeColor(board.getMap().getTiles()[xTile][yTile].getTileColor());
		
	}

	public double getRightX() {
		return rightX;
	}

	public void setRightX(double rightX) {
		this.rightX = rightX;
	}

	public double getRightY() {
		return rightY;
	}

	public void setRightY(double rightY) {
		this.rightY = rightY;
	}

	public double getLeftX() {
		return leftX;
	}

	public void setLeftX(double leftX) {
		this.leftX = leftX;
	}

	public double getLeftY() {
		return leftY;
	}

	public void setLeftY(double leftY) {
		this.leftY = leftY;
	}

	public Color getRightEyeColor() {
		return rightEyeColor;
	}

	public void setRightEyeColor(Color rightEyeColor) {
		this.rightEyeColor = rightEyeColor;
	}

	public Color getLeftEyeColor() {
		return leftEyeColor;
	}

	public void setLeftEyeColor(Color leftEyeColor) {
		this.leftEyeColor = leftEyeColor;
	}

}
