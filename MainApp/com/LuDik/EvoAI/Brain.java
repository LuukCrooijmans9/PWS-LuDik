package com.LuDik.EvoAI;

import java.awt.Color;

/**
 * 
 * The brain connects neurons to eachother and keeps them organised.
 *
 */
public class Brain {
	private int brainHeight, brainWidth;
	private Neuron[][] neurons;
	private Creature creature;
	private double[] inputs, hiddenOutputs, hiddenInputs, outputs;
	private float[] rgbColor;

	/**
	 * inputs indecis 0 to 3: RGB values of the lefteye. 3 to 6: RGB values of the
	 * centereye. 6 to 9: RGB values of the
	 * righteye. 10: current speed.  11: fat. 12:
	 * constant value 1. 13>: memorycels.
	 * 
	 * outputs indecis 0: deltaspeed. 1: deltadirection. 2: amount to eat. 3 to 6:
	 * RGB values for the bodycolor. 6 to 10: Not used atm. 13>: memorycels
	 */

	// Generates a new random brain.
	Brain(int brainHeight, int brainWidth, Creature creature) {
		this.brainHeight = brainHeight;
		this.brainWidth = brainWidth;
		neurons = new Neuron[brainWidth][brainHeight];
		this.creature = creature;
		outputs = new double[brainHeight];
		inputs = new double[brainHeight];
		rgbColor = new float[2];
		for (int i = 0; i < this.brainWidth; i++) {
			for (int j = 0; j < this.brainHeight; j++) {
				neurons[i][j] = new Neuron(brainHeight);
			}
		}
	}

	// Generates a brain based on a parentbrain
	Brain(Brain parentBrain, Creature creature, double deviation) {
		this.brainHeight = parentBrain.getBrainHeight();
		this.brainWidth = parentBrain.getBrainWidth();
		this.neurons = parentBrain.getNeurons();
		this.creature = creature;
		outputs = new double[this.brainHeight];
		inputs = new double[this.brainHeight];
		rgbColor = new float[2];
		for (int i = 0; i < this.brainWidth; i++) {
			for (int j = 0; j < this.brainHeight; j++) {
				this.neurons[i][j] = new Neuron(this.neurons[i][j], deviation);
			}
		}
	}

	// Calculates and orders the inputs for the neuron
	public void generateInputs() {

		Color rightEyeColor = creature.getRightEyeColor();
		if (rightEyeColor != null) {
			rgbColor = rightEyeColor.getRGBColorComponents(null);
			for (int i = 0; i < rgbColor.length; i++) {
				inputs[i] = rgbColor[i];
			}
		}

		Color centerEyeColor = creature.getCenterEyeColor();
		if (centerEyeColor != null) {
			rgbColor = centerEyeColor.getRGBColorComponents(null);
			for (int i = 0; i < rgbColor.length; i++) {
				inputs[i + 3] = rgbColor[i];
			}
		}

		Color leftEyeColor = creature.getLeftEyeColor();
		if (leftEyeColor != null) {
			rgbColor = leftEyeColor.getRGBColorComponents(null);
			for (int i = 0; i < rgbColor.length; i++) {
				inputs[i + 6] = rgbColor[i];
			}
		}

		inputs[9] = creature.getSpeed();
		inputs[10] = creature.getFat();
		inputs[11] = creature.getWater();

		inputs[12] = 1d;

		for (int i = 13; i < brainHeight; i++) {
			inputs[i] = outputs[i];
		}

	}

	// proccesses the neurons and calculates the brainOutputs
	public double[] feedForward() {
		hiddenOutputs = new double[brainHeight];
		hiddenInputs = new double[brainHeight];

		// loop voor eerste layer van neuralnet
		for (int i = 0; i < brainHeight; i++) {
			hiddenOutputs[i] = neurons[0][i].processNeuron(inputs);
		}

		// loop voor hiddenlayers van neuralnet
		for (int i = 1; i < brainWidth; i++) {
			hiddenInputs = hiddenOutputs;
			for (int j = 0; j < brainHeight; j++) {
				hiddenOutputs[i] = neurons[i][j].processNeuron(hiddenInputs);
			}
		}
		outputs = hiddenOutputs;
		return outputs;
	}

	public int getBrainHeight() {
		return brainHeight;
	}

	public void setBrainHeight(int brainHeight) {
		this.brainHeight = brainHeight;
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

}
