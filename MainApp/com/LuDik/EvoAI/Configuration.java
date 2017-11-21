package com.LuDik.EvoAI;

public class Configuration {

	// een aantal rekenkundige functies.

	// geeft een nummer tussen de min en max waarde. als distribution 1 is wordt
	// alles gelijk verdeeld, bij > 1 wordt er vaker een kleinere waarde gegeven en
	// bij < 1 wordt vaker een grotere waarde gegeven
	public static int distributedRandomNumber(int max, int min, double distribution) {
		int number = (int) (min + (max + 1 - min) * Math.pow(Math.random(), distribution));
		if (number < min || number > max) {
			return min;
		} else {
			return number;
		}
	}

}
