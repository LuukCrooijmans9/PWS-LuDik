package com.LuDik.EvoAI;

import java.awt.Color;

public class Brain {
	private int height, width;
	private Neuron[][] neurons;
	private Creature creature;
	private double[] inputs, hiddenOutputs, outputs;
	private float[] rgbColor;

	Brain(int height, int width, Creature creature) {
		this.height = height;
		this.width = width;
		this.creature = creature;
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				neurons[i][j] = new Neuron(i);
			}
		}
	}

	public void generateInputs() {

		Color leftEyeColor = creature.getLeftEyeColor();
		leftEyeColor.getRGBColorComponents(rgbColor);
		for (int i = 0; i < rgbColor.length; i++) {
			inputs[i] = rgbColor[i];
		}

		Color rightEyeColor = creature.getRightEyeColor();
		rightEyeColor.getRGBColorComponents(rgbColor);
		for (int i = 0; i < rgbColor.length; i++) {
			inputs[i + 3] = rgbColor[i];
		}

		inputs[6] = creature.getSpeed();
		inputs[7] = creature.getDirection();
		inputs[8] = creature.getAge();
		inputs[9] = creature.getFat();

		// Als alle andere waardes 0 zijn kan het nogsteeds acties uitvoeren door de
		// constante 1
		inputs[10] = 1d;

		for (int i = 1; i < height; i++) {
			inputs[i] = outputs[i];
		}
	}

	public void feedForward() {
		hiddenOutputs = new double[height];
		
		//loop voor eerste layer van neuralnet
		for (int i = 0; i < height; i++) {
			hiddenOutputs[i] = neurons[0][i].processNeuron(inputs);
		}
		
		//loop voor hiddenlayers van neuralnet
		for (int i = 1; i < width -1; i++) {
			for (int j = 0; j < height ; j++) {
				neurons[i][j].processNeuron(hiddenOutputs);
			}
		}

	}

}
