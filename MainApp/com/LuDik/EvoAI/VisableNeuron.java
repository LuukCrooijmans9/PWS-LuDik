package com.LuDik.EvoAI;

import java.awt.geom.Ellipse2D;

public class VisableNeuron {
	private Ellipse2D neuronBody;
	private Brain brain;
	
	public VisableNeuron(double xPos, double yPos, double size) {
//		this.visualisedNeuron = visualisedNeuron;
		
		neuronBody = new Ellipse2D.Double(xPos, yPos, size, size);
		
	}
}
