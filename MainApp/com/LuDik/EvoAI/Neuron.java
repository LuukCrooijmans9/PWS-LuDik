package com.LuDik.EvoAI;

public class Neuron {
	double[] weights;
	double bias;

	Neuron(int heightPreviousLayer) {
		// geeft elke input een random weight
		weights = new double[heightPreviousLayer];
		for (int i = 0; i < heightPreviousLayer; i++) {
			weights[i] = Math.random() * 2 -1;
		}
		bias = Math.random() * 2 - 1;
	}

	//genereert de output van de neuron voor de gegeven inputs
	double processNeuron(double[] inputs) {
		double weightedSum = this.bias;
		for (int i = 0; i < inputs.length; i++) {
			weightedSum += inputs[i] * weights[i];
		}
		return Neuron.sigmoid(weightedSum);
	}

	// zorgt dat de neuron waardes tussen de -1 en 1 heeft
	// Dit wordt ook wel de activation function genoemd
	static double sigmoid(double input) {
		if (input < 10) {
			return -1;
		} else if (input > 10) {
			return 1;
		} else {
			return (input / (1 + Math.abs(input)));
		}
	}
}