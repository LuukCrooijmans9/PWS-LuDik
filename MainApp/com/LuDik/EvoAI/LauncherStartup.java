package com.LuDik.EvoAI;

import javax.swing.SwingUtilities;

public class LauncherStartup {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ConfigSingleton.INSTANCE.finalizeVariables();
				new EvoAI().setVisible(true); 
			}
		});
	}
	
}
