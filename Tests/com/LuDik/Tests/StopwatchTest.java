package com.LuDik.Tests;

import java.lang.reflect.Array;
import java.util.Random;

public class StopwatchTest {
	private static long startTime;
	private static long endTime;
	private static long elapsedTime;

	public static void CurrentTest() {
		startTime = System.currentTimeMillis();

		CurrentTestingMethod();

		endTime = System.currentTimeMillis();

		elapsedTime = endTime - startTime;
		System.out.println("This method took " + elapsedTime + " milliseconds to run.");
	}

	private static void CurrentTestingMethod() {
		Random randy = new Random((long) (0.1 * (Long.MAX_VALUE - 1)));
		double number = (randy.nextDouble());
		System.out.println(number);
		int[] numbers = new int[10];
		int iterations = Integer.MAX_VALUE;
		for (int i = 0; i < iterations; i++) {
			// System.out.println(number);

			for (int j = 1; j < numbers.length + 1; j++) {
				if (number * 10 < j) {
					numbers[j - 1]++;
					// System.out.println("this number is between " + ((double) (j - 1) / 10) + "
					// and " + ((double) j / 10)
					// + ": " + number);
					break;
				}
			}
			if (i % 10000 == 0) {
				randy = new Random((long) (number * (Long.MAX_VALUE - 1)));
			}
			number = (randy.nextDouble());
		}
		System.out.println("last random Number :" + number);
		for (int i = 0; i < numbers.length; i++) {
			System.out
					.println("percentage of numbers between " + ((double) (i) / 10) + " and " + ((double) (i + 1) / 10)
							+ ": " + (float) ((double) numbers[i] / (double) iterations) * 100f + "%");
		}

	}

	private static void old() {
		// Testing Method
		long iterations = (long) Math.pow(2d, 32d);
		double k = 0;
		for (long i = 0; i < iterations; i++) {
			k = Math.random();
		}
		System.out.println(k);
		System.out.println(iterations);
	}

	private static void UsingNextDoubleAsSeedForNextRNG() {
		Random randy = new Random((long) (Math.random() * (Long.MAX_VALUE - 1)));
		double number = (randy.nextDouble());
		System.out.println(number);
		int[] numbers = new int[11];
		int iterations = Integer.MAX_VALUE;
		for (int i = 0; i < iterations; i++) {
			// System.out.println(number);
			for (int j = 1; j < numbers.length; j++) {
				if (number * 10 < j) {
					numbers[j]++;
					// System.out.println("this number is between " + ((double) (j - 1) / 10) + "
					// and " + ((double) j / 10)
					// + ": " + number);
					break;
				}
			}
			randy = new Random((long) (number * (Long.MAX_VALUE - 1))); // If this line is commented there will be only
																		// ONE rng. If this is not commented it will use
																		// the last generated number from the current
																		// rng to create a new rng
			number = (randy.nextDouble());
		}
		for (int i = 1; i < numbers.length; i++) {
			System.out.println("percentage of numbers between " + ((double) (i - 1) / 10) + " and " + ((double) i / 10)
					+ ": " + (float) ((double) numbers[i] / (double) iterations) * 100f + "%");
		}
	}
}
