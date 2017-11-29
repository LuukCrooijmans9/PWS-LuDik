package com.LuDik.EvoAI;

import java.awt.Color;

/**
 * 
 * The brain connects neurons to eachother and keeps them organised.
 *
 */
public class Brain {
	private int inputHeight, hiddenHeight, brainWidth;
	private int height; // heighest layer
	private Neuron[][] neurons;
	private Creature creature;
	private double[] inputs, hiddenOutputs, hiddenInputs, outputs;
	private float[] rgbColor;

	private static String[] inputLabels = { "RLeftEye", "GLeftEye", "BLeftEye", "RCenterEye", "GCenterEye",
			"BCenterEye", "RRightEye", "GRightEye", "BRightEye", "currentSpeed", "fat", "constant value 1" };

	private static String[] outputLabels = { "deltaSpeed", "deltaDirection", "amount to eat", "RBody", "GBody", "BBody",
			"giveBirth" };

	/**
	 * inputs indecis 0 to 3: RGB values of the lefteye. 3 to 6: RGB values of the
	 * centereye. 6 to 9: RGB values of the righteye. 10: current speed. 11: fat.
	 * 12: constant value 1. 13>: memorycels.
	 * 
	 * outputs indecis 0: deltaspeed. 1: deltadirection. 2: amount to eat. 3 to 6:
	 * RGB values for the bodycolor. 6: giveBirth. 7 to 13: Not used atm. 13>:
	 * memorycels
	 */

	/**
	 * Generates a new random brain.
	 * 
	 * @parameters The height of the brain, The width of the brain, The creature of
	 *             the brain
	 */
	Brain(int inputHeight, int hiddenHeight, int brainWidth, Creature creature) {
		this.inputHeight = inputHeight;
		this.hiddenHeight = hiddenHeight;
		this.brainWidth = brainWidth;
		this.height = Math.max(inputHeight, Math.max(hiddenHeight, 13));
		neurons = new Neuron[brainWidth][height];
		this.creature = creature;
		outputs = new double[height];
		inputs = new double[height];
		rgbColor = new float[2];
		for (int i = 0; i < this.brainWidth; i++) {
			for (int j = 0; j < height; j++) {
				if ((i == this.height - 1 && j > this.inputHeight - 1)
						|| ((i == 0 || i == this.height - 1) && j > this.hiddenHeight - 1)) {
					neurons[i][j] = new Neuron(height, false, creature.getBoard());
				} else {
					neurons[i][j] = new Neuron(height, true, creature.getBoard());
				}
			}
		}
	}

	/** Generates a brain based on a parentbrain */
	Brain(Brain parentBrain, Creature creature, double deviation) {
		this.inputHeight = parentBrain.getInputHeight();
		this.inputHeight = parentBrain.getHiddenHeight();
		this.height = parentBrain.getHeight();
		this.brainWidth = parentBrain.getBrainWidth();
		this.neurons = parentBrain.getNeurons();
		this.creature = creature;

		outputs = new double[height];
		inputs = new double[height];
		rgbColor = new float[2];
		for (int i = 0; i < this.brainWidth; i++) {
			for (int j = 0; j < height; j++) {
				neurons[i][j] = new Neuron(neurons[i][j], deviation, creature.getBoard());
			}
		}
	}

	/** Calculates and orders the inputs for the neuron */
	public void generateInputs() {

		Color rightEyeColor = creature.getRightEyeColor();
		if (rightEyeColor != null) {
			rgbColor = rightEyeColor.getRGBColorComponents(null);
			for (int i = 0; i < rgbColor.length; i++) {
				inputs[i] = rgbColor[i] * 2 - 1;
			}
		}

		Color centerEyeColor = creature.getCenterEyeColor();
		if (centerEyeColor != null) {
			rgbColor = centerEyeColor.getRGBColorComponents(null);
			for (int i = 0; i < rgbColor.length; i++) {
				inputs[i + 3] = rgbColor[i] * 2 - 1;
			}
		}

		Color leftEyeColor = creature.getLeftEyeColor();
		if (leftEyeColor != null) {
			rgbColor = leftEyeColor.getRGBColorComponents(null);
			for (int i = 0; i < rgbColor.length; i++) {
				inputs[i + 6] = rgbColor[i] * 2 - 1;
			}
		}

		inputs[9] = Neuron.sigmoid(creature.getSpeed()) * 2 - 1;
		inputs[10] = Neuron.sigmoid(creature.getFat() / ConfigSingleton.INSTANCE.startingFat) * 2 - 1;
		// inputs[11] = creature.getWater();
		inputs[11] = -1d;
		inputs[12] = 1d;

		for (int i = 13; i < height; i++) {
			inputs[i] = outputs[i];
		}

	}

	/** proccesses the neurons and calculates the brainOutputs */
	public double[] feedForward() {
		hiddenOutputs = new double[height];
		hiddenInputs = new double[height];

		// loop voor eerste layer van neuralnet
		for (int i = 0; i < height; i++) {
			hiddenOutputs[i] = neurons[0][i].processNeuron(inputs);
		}

		// loop voor hiddenlayers van neuralnet
		for (int i = 1; i < brainWidth; i++) {
			hiddenInputs = hiddenOutputs;
			for (int j = 0; j < height; j++) {
				hiddenOutputs[i] = neurons[i][j].processNeuron(hiddenInputs);
			}
		}
		outputs = hiddenOutputs;
		return outputs;
	}

	public int getBrainWidth() {
		return brainWidth;
	}

	public void setBrainWidth(int brainWidth) {
		this.brainWidth = brainWidth;
	}

	public Neuron[][] getNeurons() {
		return neurons;
	}

	public void setNeurons(Neuron[][] neurons) {
		this.neurons = neurons;
	}

	public Creature getCreature() {
		return creature;
	}

	public void setCreature(Creature creature) {
		this.creature = creature;
	}

	public double[] getInputs() {
		return inputs;
	}

	public void setInputs(double[] inputs) {
		this.inputs = inputs;
	}

	public double[] getHiddenOutputs() {
		return hiddenOutputs;
	}

	public void setHiddenOutputs(double[] hiddenOutputs) {
		this.hiddenOutputs = hiddenOutputs;
	}

	public double[] getHiddenInputs() {
		return hiddenInputs;
	}

	public void setHiddenInputs(double[] hiddenInputs) {
		this.hiddenInputs = hiddenInputs;
	}

	public double[] getOutputs() {
		return outputs;
	}

	public void setOutputs(double[] outputs) {
		this.outputs = outputs;
	}

	public float[] getRgbColor() {
		return rgbColor;
	}

	public void setRgbColor(float[] rgbColor) {
		this.rgbColor = rgbColor;
	}

	public int getInputHeight() {
		return inputHeight;
	}

	public void setInputHeight(int inputHeight) {
		this.inputHeight = inputHeight;
	}

	public int getHiddenHeight() {
		return hiddenHeight;
	}

	public void setHiddenHeight(int hiddenHeight) {
		this.hiddenHeight = hiddenHeight;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
