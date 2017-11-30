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
	boolean isActive;
	private double sigmoidOutput;

	/**
	 * Creates a random Neuron
	 */
	Neuron(int heightPreviousLayer, boolean isActive, Board board) {

		weights = new double[heightPreviousLayer];
		for (int i = 0; i < heightPreviousLayer; i++) {
			weights[i] = board.randomDouble() * 2 - 1;
		}
		bias = board.randomDouble() * 2 - 1;
		this.isActive = isActive;
	}

	/**
	 * Creates a Neuron based on another "parent" Neuron. how much they differ is
	 * defined by the deviation.
	 * 
	 * @parameter Parent Neuron, Deviation
	 */

	Neuron(Neuron neuron, double deviation, Board board) {
		
		this.weights = new double[neuron.getWeights().length];

		for (int i = 0; i < neuron.getWeights().length; i++) {
			//System.out.println(neuron.getWeights()[i]);
			this.weights[i] = neuron.getWeights()[i];
		}
		for (int i = 0; i < this.weights.length; i++) {
			double random = board.randomDouble();
			if (random < deviation) {
				weights[i] += (board.randomDouble() * 2 - 1) * deviation;
			}
		}
		this.bias = neuron.getBias();
		double random = board.randomDouble();
		if (random < deviation) {
			deviation = (board.randomDouble() * 2 - 1) * deviation;
			this.bias += deviation;
		}
		this.isActive = neuron.isActive;
	}

	/** Generates the output of the neuron */
	double processNeuron(double[] inputs) {
		if (!isActive) {
			return 0d;
		}
		double weightedSum = this.bias;
		for (int i = 0; i < inputs.length; i++) {
			weightedSum += inputs[i] * weights[i];
		}

		sigmoidOutput = Neuron.sigmoid(weightedSum);
		return sigmoidOutput;
	}

	/**
	 * This is a simplified sigmoid function due to the processing requirements. It
	 * 
	 * @parameter output values between -1 and 1.
	 */
	public static double sigmoid(double input) {
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public double getSigmoidOutput() {
		return sigmoidOutput;
	}

}