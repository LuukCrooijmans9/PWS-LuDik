package com.LuDik.EvoAI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class BrainVisualisedPanel extends JPanel {

	private double gridSize;
	private static int inputLayerHeight = ConfigSingleton.INSTANCE.inputLayerHeight;
	private static int hiddenLayerHeight = ConfigSingleton.INSTANCE.hiddenLayerHeight;
	private static int brainWidth = ConfigSingleton.INSTANCE.brainWidth + 1;
	private static int heighestLayer;

	private CameraPanel cameraPanel;

	private Ellipse2D[][] neurons;
	private Creature creature;
	private Brain brain;

	/**
	 * Create the panel.
	 */
	public BrainVisualisedPanel(CameraPanel cameraPanel) {
		this.cameraPanel = cameraPanel;

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

		double neuronDist = (getWidth() - (brainWidth * gridSize)) / (brainWidth + 1); // the distance between the
																						// neurons on the X-axis

		for (int i = 0; i < brainWidth; i++) {

			int layerHeight;
			if (i == 0 || i == brainWidth - 1) {
				layerHeight = inputLayerHeight;
			} else {
				layerHeight = hiddenLayerHeight;
			}

			double startHeight = (0.5 * getHeight()) - (0.5 * gridSize * ((2 * layerHeight) - 1));

			for (int k = 0; k < layerHeight; k++) {
				neurons[i][k] = new Ellipse2D.Double(i * (neuronDist + gridSize) + neuronDist,
						startHeight + 2 * gridSize * k, gridSize, gridSize);

				if (creature != null) {
					double output;
					if (i != 0) {
						BasicStroke defStroke = (BasicStroke) g2d.getStroke();
						for (int j = 0; j < neurons[i - 1].length; j++) {
							Ellipse2D neuron = neurons[i - 1][j];
							double weight = brain.getNeurons()[i - 1]
									[k].getWeights()
									[j];
							g2d.setPaint(new Color((float) Math.max(0, weight / Math.abs(weight)), 0f, 0f,
									(float) Math.abs(weight / (1 + Math.abs(weight)))));
							g2d.setStroke(new BasicStroke((float) Math.abs(weight)));
							g2d.drawLine(
									(int) (neuron.getMaxX() + 0.5), (int) (neuron.getCenterY() + 0.5),
									(int) (neurons[i][k].getMinX() + 0.5), (int) (neurons[i][k].getCenterY() + 0.5));
						}
						
						g2d.setStroke(defStroke);
						g2d.setPaint(Color.black);
						output = brain.getNeurons()[i - 1][k].getSigmoidOutput();
					} else {
						output = brain.getInputs()[k];
					}

					String outputString = String.valueOf(output);
					if (outputString.length() > 5) {
						outputString = outputString.substring(0, 4);
					}
					drawCenteredText(g2d, (int) (neurons[i][k].getCenterX() + 0.5),
							(int) (neurons[i][k].getCenterY() + 0.5), 10f, outputString);
				}
				g2d.draw(neurons[i][k]);
			}
		}

	}

	public void update() {
		gridSize = Math.min(getHeight() / ((2 * heighestLayer) + 1), getWidth() / ((2 * brainWidth) + 1));
		System.out.println("camerapanel: " + cameraPanel);
		creature = cameraPanel.getSelectedCreature();

		if (creature != null) {
			brain = creature.getBrain();
		}

		System.out.println("test: " + gridSize);

		repaint();
	}

	public static void drawCenteredText(Graphics2D g2d, int x, int y, float size, String text) {
		// Create a new font with the desired size
		Font newFont = g2d.getFont().deriveFont(size);
		g2d.setFont(newFont);
		// Find the size of string s in font f in the current Graphics context g.
		FontMetrics fm = g2d.getFontMetrics();
		java.awt.geom.Rectangle2D rect = fm.getStringBounds(text, g2d);

		int textHeight = (int) (rect.getHeight());
		int textWidth = (int) (rect.getWidth());

		// Find the top left and right corner
		int cornerX = x - (textWidth / 2);
		int cornerY = y - (textHeight / 2) + fm.getAscent();

		g2d.drawString(text, cornerX, cornerY); // Draw the string.
	}

}
