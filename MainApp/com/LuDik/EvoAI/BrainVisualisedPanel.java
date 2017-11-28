package com.LuDik.EvoAI;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class BrainVisualisedPanel extends JPanel {

	private double gridSize;
	private static int inputLayerHeight = ConfigSingleton.INSTANCE.inputLayerHeight;
	private static int hiddenLayerHeight = ConfigSingleton.INSTANCE.hiddenLayerHeight;
	private static int brainWidth = ConfigSingleton.INSTANCE.brainWidth;
	private static int heighestLayer;

	private Ellipse2D[][] neurons;

	/**
	 * Create the panel.
	 */
	public BrainVisualisedPanel(CameraPanel cameraPanel) {
		heighestLayer = Math.max(inputLayerHeight, hiddenLayerHeight);
		
		neurons = new Ellipse2D[brainWidth][hiddenLayerHeight];
		neurons[0] = new Ellipse2D[inputLayerHeight];
		neurons[brainWidth - 1] = new Ellipse2D[inputLayerHeight];
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(0.0001f));
		
		double neuronDist = (getWidth() - (brainWidth * gridSize)) / (brainWidth + 1); // the distance between the neurons on the X-axis

		for (int i = 0; i < brainWidth; i++) {
			
			int layerHeight;
			if (i == 0 || i == brainWidth - 1) {
				layerHeight = inputLayerHeight;
			} else {
				layerHeight = hiddenLayerHeight;
			}

			double startHeight = (0.5 * getHeight()) - (0.5 * gridSize * ((2 * layerHeight) - 1));

			for (int k = 0; k < layerHeight; k++) {
				neurons[i][k] = new Ellipse2D.Double(
						i * (neuronDist + gridSize) + neuronDist, 
						startHeight + 2 * gridSize * k, 
						gridSize, gridSize); 
				g2d.draw(neurons[i][k]);
				
				if (i != 0) {
					for (Ellipse2D neuron : neurons[i - 1]) {
						g2d.drawLine(
								(int) (neuron.getMaxX() + 0.5), (int) (neuron.getCenterY() + 0.5), 
								(int) (neurons[i][k].getMinX() + 0.5), (int) (neurons[i][k].getCenterY() + 0.5));
					}
				}
			}
		}

		

	}

	public void update() {
		gridSize = Math.min(getHeight() / ((2 * heighestLayer) + 1), getWidth() / ((2 * brainWidth) + 1));

		System.out.println("test: " + gridSize);

		repaint();
	}

}
