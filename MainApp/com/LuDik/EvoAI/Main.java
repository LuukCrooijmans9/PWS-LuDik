package com.LuDik.EvoAI;


import javax.swing.SwingUtilities;

/**
 * This class contains the main method that runs the EvoAI class.
 */

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				new EvoAI().setVisible(true); 
			}
		});
	}
}
