package com.LuDik.EvoAI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BrainVisualisedPanel extends JPanel {

	private double gridSize;
	private int inputLayerHeight;
	private int hiddenLayerHeight;
	private int brainWidth;
	private int heighestLayer;

	private static float maxWidthLine = 4;
	private static float dimmedAlpha = 0.1f;

	private CameraPanel cameraPanel;

	private Ellipse2D selectedNeuron;
	private Ellipse2D[][] neurons;
	private boolean[][] isNeuronDimmed;
	private Creature creature;
	private Brain brain;
	private double maxWeight;
	private double maxBias;

	/**
	 * Create the panel.
	 */
	public BrainVisualisedPanel(CameraPanel cameraPanel) {
		
		inputLayerHeight = ConfigSingleton.INSTANCE.inputLayerHeight;
		hiddenLayerHeight = ConfigSingleton.INSTANCE.hiddenLayerHeight;
		brainWidth = ConfigSingleton.INSTANCE.brainWidth + 1;
		
		BrainVisualisedPanel panel = this;
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (neurons == null)
					return;

				Point clickPoint = e.getPoint();

				for (Ellipse2D[] neuronLayer : neurons) {
					for (Ellipse2D neuron : neuronLayer) {
						if (neuron.getBounds2D().contains(clickPoint)) {

							panel.setSelectedNeuron(neuron);
							update();
							System.out.println("test: " + neuron);
							return;
						}
					}
				}

				panel.setSelectedNeuron(null); // if no neuron was clicked, this deselects the currently sellected
				update();								// neuron

			}
		});
		this.cameraPanel = cameraPanel;

		heighestLayer = Math.max(inputLayerHeight, hiddenLayerHeight);

		neurons = new Ellipse2D[brainWidth][hiddenLayerHeight];
		neurons[0] = new Ellipse2D[inputLayerHeight];
		neurons[brainWidth - 1] = new Ellipse2D[inputLayerHeight];

		isNeuronDimmed = new boolean[brainWidth][hiddenLayerHeight];
		isNeuronDimmed[0] = new boolean[inputLayerHeight];
		isNeuronDimmed[brainWidth - 1] = new boolean[inputLayerHeight];
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
		float textSize = (float) (gridSize / 2);

		for (int i = 0; i < brainWidth; i++) {

			int layerHeight; // how many neurons (circles) there are in this layer
			if (i == 0 || i == brainWidth - 1) {
				layerHeight = inputLayerHeight;
			} else {
				layerHeight = hiddenLayerHeight;
			}

			double startHeight = (0.5 * getHeight()) - (0.5 * gridSize * ((2 * layerHeight) - 1)); // the height on
																									// which this layer
																									// starts

			for (int k = 0; k < layerHeight; k++) {
				neurons[i][k] = new Ellipse2D.Double(i * (neuronDist + gridSize) + neuronDist,
						startHeight + 2 * gridSize * k, gridSize, gridSize);

				if (creature != null) {
					double output;
					float bias;
					if (i != 0) {
						drawNeuronLines(g2d, i, k, (float) maxWeight);
						output = brain.getNeurons()[i - 1][k].getSigmoidOutput();
						bias = (float) brain.getNeurons()[i - 1][k].getBias();
					} else {
						output = brain.getInputs()[k];
						bias = 0;
					}

					String outputString = String.valueOf(output);
					if (outputString.length() > 5) {
						outputString = outputString.substring(0, 4);
					}

					drawNeuronBody(g2d, textSize, neurons[i][k], outputString, bias, (float) maxBias);
				}

				if (i == 0 && k <= Brain.getInputLabels().length - 1) {
					drawCenteredText(g2d, (int) (neurons[i][k].getMinX() - 2 * gridSize),
							(int) (neurons[i][k].getCenterY()), textSize, Brain.getInputLabels()[k]);
				}

				if (i == brainWidth - 1 && k <= Brain.getOutputLabels().length - 1) {
					drawCenteredText(g2d, (int) (neurons[i][k].getMaxX() + 2 * gridSize),
							(int) (neurons[i][k].getCenterY()), textSize, Brain.getOutputLabels()[k]);
				}
			}
		}

	}

	/**
	 * Updates the panel. This method should be called everytime the state of the brain or of the window containing this 
	 * panel is changed;
	 */

	public void update() {
		gridSize = Math.min(getHeight() / ((2 * heighestLayer) + 1), getWidth() / ((2 * brainWidth) + 1));
		creature = cameraPanel.getSelectedCreature();

		if (creature != null) {
			brain = creature.getBrain();
			maxWeight = determineMaxWeight(brain);
			maxBias = determineMaxBias(brain);

		}

		repaint();
	}

	/**
	 * Returns the highest absolute value of a weight of the brain parameter
	 * @param brain
	 * @return highest absolute value of a weight
	 */
	
	private double determineMaxWeight(Brain brain) {
		double maxWeight = 0;
		double averageWeight = 0;
		int amountOfWeights = 0;
		for (Neuron[] neurons : brain.getNeurons()) {
			for (Neuron neuron : neurons) {
				for (double weight : neuron.getWeights()) {
					if (Math.abs(weight) > Math.abs(maxWeight)) {
						maxWeight = weight;
					}
					averageWeight += Math.abs(weight);
					amountOfWeights++;

				}
			}
		}

		averageWeight /= amountOfWeights;

		// System.out.println("maxWeight: " + maxWeight);
		// System.out.println("averageWeight: " + averageWeight);
		return maxWeight;
	}

	private double determineMaxBias(Brain brain) {
		double maxBias = 0;
		double averageBias = 0;
		int amountOfBiases = 0;

		for (Neuron[] neurons : brain.getNeurons()) {
			for (Neuron neuron : neurons) {
				double bias = neuron.getBias();
				if (Math.abs(bias) > Math.abs(maxBias)) {
					maxBias = bias;
				}
				averageBias += Math.abs(bias);
				amountOfBiases++;
			}
		}

		averageBias /= amountOfBiases;

		// System.out.println("maxBias: " + maxBias);
		// System.out.println("averageBias: " + averageBias);

		return maxBias;
	}

	/**
	 * This method draws the circle and the output value in the circle representing
	 * the neuron.
	 * 
	 * @param g2d
	 * @param textSize
	 * @param neuronBody
	 * @param outputString
	 */
	private void drawNeuronBody(Graphics2D g2d, float textSize, Ellipse2D neuronBody, String outputString, float bias,
			float maxBias) {

		int[] posArr = getPosOfNeuron(neuronBody);
		int neuronLayer = posArr[0], neuronHeight = posArr[1];
		
		float startAlpha;
		if (isNeuronDimmed[neuronLayer][neuronHeight]) {
			startAlpha = dimmedAlpha;
		} else {
			startAlpha = 1;
		}
		
		
		
		if (bias < 0) {
			g2d.setPaint(new Color(1, 0, 0, startAlpha * Math.abs(bias / maxBias)));
		} else {
			g2d.setPaint(new Color(0, 1, 0, startAlpha * Math.abs(bias / maxBias)));
		}

		g2d.fill(neuronBody);

		g2d.setPaint(new Color(0, 0, 0, startAlpha));
		drawCenteredText(g2d, (int) (neuronBody.getCenterX() + 0.5), (int) (neuronBody.getCenterY() + 0.5), textSize,
				outputString);
		g2d.draw(neuronBody);
		g2d.setPaint(Color.BLACK);
	}

	/**
	 * This method draws the lines between the specified neuron and all the neurons
	 * from the previous layer, and colours those lines with a width and color
	 * determined by the weight of that connection.
	 * 
	 * @param g2d,
	 *            the graphics object
	 * @param layer,
	 *            the layer the neuron is in
	 * @param heightPos,
	 *            the height of the neuron in the layer
	 */

	private void drawNeuronLines(Graphics2D g2d, int layer, int heightPos, float maxWeight) {
		BasicStroke defStroke = (BasicStroke) g2d.getStroke();

		float widthMultiplier = Math.abs(maxWidthLine / maxWeight);

		for (int j = 0; j < neurons[layer - 1].length; j++) {

			
			Ellipse2D neuron = neurons[layer - 1][j];
			
			float startAlpha;
			if (isNeuronDimmed[layer][heightPos] || isNeuronDimmed[layer - 1][j]) {
				startAlpha = dimmedAlpha;
			} else {
				startAlpha = 1;
			}
			
			float weight = (float) brain.getNeurons()[layer - 1][heightPos].getWeights()[j];

			g2d.setPaint(
					new Color((float) Math.max(0, weight / Math.abs(weight)), 0f, 0f, startAlpha * Math.abs(weight / maxWeight)));

			g2d.setStroke(new BasicStroke((float) Math.abs(weight) * widthMultiplier));

			g2d.drawLine((int) (neuron.getMaxX() + 0.5), (int) (neuron.getCenterY() + 0.5),
					(int) (neurons[layer][heightPos].getMinX() + 0.5),
					(int) (neurons[layer][heightPos].getCenterY() + 0.5));
		}

		g2d.setStroke(defStroke);
		g2d.setPaint(Color.black);
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

	/**
	 * Sets the input as the selected neuron and dims all neurons in the same layer as the selectedNeuron.
	 * @param selectedNeuron
	 */
	
	public void setSelectedNeuron(Ellipse2D selectedNeuron) {
		this.selectedNeuron = selectedNeuron;

		for (int i = 0; i < isNeuronDimmed.length; i ++) {
			for (int k = 0; k < isNeuronDimmed[i].length; k++) {
				isNeuronDimmed[i][k] = false;
			}
		}
		if (selectedNeuron != null) {
			int[] posArr = getPosOfNeuron(selectedNeuron);
			int neuronLayer = posArr[0], neuronHeight = posArr[1];

			System.out.println("test: " + neuronLayer + "  " + neuronHeight);
			
			for (int j = 0; j < isNeuronDimmed[neuronLayer].length; j++) {
				if (j != neuronHeight) {
					isNeuronDimmed[neuronLayer][j] = true;
				}
			}

		}
	}

	private int[] getPosOfNeuron(Ellipse2D selectedNeuron) {

		int[] posArr;

		for (int i = 0; i < neurons.length; i++) {
			for (int k = 0; k < neurons[i].length; k++) {
				if (selectedNeuron == neurons[i][k]) {
					posArr = new int[] { i, k };
					return posArr;
				}
			}
		}

		return null;
	}

}
