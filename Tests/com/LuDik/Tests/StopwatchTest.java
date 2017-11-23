package com.LuDik.Tests;

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
		// Testing Method

	}

	private static void old() {
	}
}
