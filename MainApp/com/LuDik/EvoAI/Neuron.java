package com.LuDik.EvoAI;

/**
 * This is the basis of the brain class. A neuron gets inputs and multiplies
 * them by their weights wich are predefined when initalising this class. The
 * sum of this is normalized between -1 and 1 with the use of a sigmoid
 * function. This sigmoidedSum + the bias of this neuron is returned as output
 * from this neuron.
 */

public class Neuron {
	double[] weights;
	double bias;

	Neuron(int heightPreviousLayer) {
		// This assigns random values to each weight and the bias.
		weights = new double[heightPreviousLayer];
		for (int i = 0; i < heightPreviousLayer; i++) {
			weights[i] = Math.random() * 2 - 1;
		}
		bias = Math.random() * 2 - 1;
	}

	Neuron(Neuron neuron, double deviation /* van 0 tot 1 waar 1 een volledig random weight maakt */) {
		// Instead of assigning a random value it takes an predefined value from the
		// Parent neuron and changes it based on a random value between -1 and 1 and
		// multiplies this with the deviation.
		this.weights = neuron.getWeights();
		for (int i = 0; i < this.weights.length; i++) {
			double random = Math.random();
			if (random < deviation) {
				weights[i] += (Math.random() * 2 - 1) * deviation;
			}
		}
		double random = Math.random();
		if (random < deviation) {
			deviation = (Math.random() * 2 - 1) * deviation;
			this.bias += deviation;
		}
	}

	// Generates the output of the neuron
	double processNeuron(double[] inputs) {
		double weightedSum = this.bias;
		for (int i = 0; i < inputs.length; i++) {

			weightedSum += inputs[i] * weights[i];

		}
		return Neuron.sigmoid(weightedSum);
	}

	// This is a simplified sigmoid function due to the processing requirements. It
	// returns values between -1 and 1.
	static double sigmoid(double input) {
		if (input < -10) {
			return -1;
		} else if (input > 10) {
			return 1;
		} else {
			return (input / (1 + Math.abs(input)));
		}
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

}