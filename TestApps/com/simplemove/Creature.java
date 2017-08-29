package com.simplemove;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;


public class Creature {
	
	private double acceleration;
	private double speed;
	
	private double xPos;
	private double yPos;
	
	private double dxPos;
	private double dyPos;

	private double orientation;
	private double turningSpeed;
	
	private static double centerPull = 10;
	private static double randomFactorTurn = 0.1;
	private static double randomFactorAcc = 0.5;
	private static double maxSpeed = 4;
	
	private static int crtrSize = 15;
//	private Shape crtrShape;
	private Board board;
	
	private static boolean attractToCenter = true;
	private static boolean disableTurningSpeed = false;

	public Creature(double x, double y, Board brd) {
		initCreature(x, y);
//		System.out.println(Math.cos(orientation) * speed);
//		System.out.println(Math.sin(orientation) * speed);
//		System.out.println(orientation);
//		System.out.println("Creature instantiated!");
		
		board = brd;
		
	}

	private void initCreature(double x, double y) {
		xPos = x;
		yPos = y;
		orientation = 270;

	}

	public Shape getCreatureShape() {
		// crtrShape = new Ellipse2D.Double(xPos - (crtrSize/ 2), yPos -
		// (crtrSize / 2), crtrSize, crtrSize);

		Shape shape;

		shape = new Ellipse2D.Double(xPos - (crtrSize / 2), yPos - (crtrSize / 2), crtrSize, crtrSize);
		return shape;

	}
	
	public Line2D.Double[] getCreatureDirectionTriangle() {
		
		
		double x1 = xPos + (Math.sin((Math.PI * orientation) / 180) * (crtrSize / 2));
		double y1 = yPos + (Math.cos((Math.PI * orientation) / 180) * (crtrSize / 2));
		orientation = orientation + 90;
		double x2 = xPos + (Math.sin((Math.PI * orientation) / 180) * (crtrSize / 2));
		double y2 = yPos + (Math.cos((Math.PI * orientation) / 180) * (crtrSize / 2));
		orientation = orientation - 180;
		double x3 = xPos + (Math.sin((Math.PI * orientation) / 180) * (crtrSize / 2));
		double y3 = yPos + (Math.cos((Math.PI * orientation) / 180) * (crtrSize / 2));
		orientation = orientation + 90;
		
		

		
		Line2D.Double[] directionTriangle = new Line2D.Double[3];
		
		directionTriangle[0] = new Line2D.Double(x1,y1,x2,y2);
		directionTriangle[1] = new Line2D.Double(x2,y2,x3,y3);
		directionTriangle[2] = new Line2D.Double(x3,y3,x1,y1);
		
		
		return directionTriangle;
	}
	
	private void attractToCenter() {
		
		if (xPos < (board.getWidth()/2) && yPos < (board.getHeight()/2) ) {
			if (orientation > 90 && orientation < 225) {
				orientation = orientation - centerPull;
				
			} else if (orientation >= 225) {
				orientation = orientation + centerPull;
				
			}
			
		} else if (xPos > (board.getWidth()/2) && yPos < (board.getHeight()/2) ) {
			if (orientation < 135) {
				orientation = orientation - centerPull;
				
			} else if (orientation < 270 && orientation > 135) {
				orientation = orientation + centerPull;
				
			}
			
		} else if (xPos > (board.getWidth()/2) && yPos > (board.getHeight()/2) ) {
			if (orientation < 45 || orientation > 270) {
				orientation = orientation - centerPull;
				
			} else if (orientation < 180 && orientation > 45) {
				orientation = orientation + centerPull;
				
			}
			
		} else if (xPos < (board.getWidth()/2) && yPos > (board.getHeight()/2) ) {
			if (orientation < 315 && orientation > 180) {
				orientation = orientation - centerPull;
				
			} else if (orientation < 90 || orientation > 315) {
				orientation = orientation + centerPull;
				
			}
			
		} 
	}

	public void notifyTimePassed(long timePassed) {
		acceleration = (Math.random() - 0.5) * randomFactorAcc;
		if (speed + acceleration >= maxSpeed) {
			speed = maxSpeed;
		} else if (speed + acceleration <= 0) {
			speed = 0;
		} else {
			speed = speed + acceleration;
		}
		
		dxPos = Math.sin((Math.PI * orientation) / 180) * speed;
		dyPos = Math.cos((Math.PI * orientation) / 180) * speed;
		
		if (xPos + dxPos - (crtrSize/2) > 0 && xPos + dxPos + (crtrSize/2) < board.getWidth()) {
			xPos = xPos + dxPos;
		}
		
		if (yPos + dyPos - (crtrSize/2) > 0 && yPos + dyPos + (crtrSize/2) < board.getHeight()) {
			yPos = yPos + dyPos;
		}
		
		if (attractToCenter) {
			attractToCenter();
		}
		
		
		orientation = orientation + turningSpeed;
		turningSpeed = turningSpeed + randomFactorTurn * (Math.random() - 0.5) ;
		if (disableTurningSpeed) {
			turningSpeed = 0;
		}
		orientation = orientation % 360;
		if (orientation < 0) {
			orientation = orientation + 360;
		}
		
	}
	
	public double getOrientation() {
		return orientation;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getTurningSpeed() {
		return turningSpeed;
	}
	
	public static double getCrtrSize() {
		return crtrSize;
	}
	
	public static void setDisableTurningSpeed(boolean state) {
		disableTurningSpeed = state;	
	}
	
	public static void setAttractToCenter(boolean state) {
		attractToCenter = state;	
	}
	
	public static boolean getDisableTurningSpeed() {
		return disableTurningSpeed;	
	}
	
	public static boolean getAttractToCenter() {
		return attractToCenter;	
	}
	
	public static double getCenterPull() {
		return centerPull;
	} 
	
	public static void setCenterPull(double newValue) {
		centerPull = newValue;
	}
	
	public static double getRandomFactorTurn() {
		return randomFactorTurn;
	} 
	
	public static void setRandomFactorTurn(double newValue) {
		randomFactorTurn = newValue;
	}
	
	public static double getRandomFactorAcc() {
		return randomFactorAcc;
	} 
	
	public static void setRandomFactorAcc(double newValue) {
		randomFactorAcc = newValue;
	}

	public static double getMaxSpeed() {
		return maxSpeed;
	}

	public static void setMaxSpeed(double maxSpeed) {
		Creature.maxSpeed = maxSpeed;
	}

	public static void setCrtrSize(int crtrSize) {
		Creature.crtrSize = crtrSize;
	}	
	
	
}
