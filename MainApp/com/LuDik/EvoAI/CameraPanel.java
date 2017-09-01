package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class CameraPanel extends JPanel {
	
	private static final int CPWIDTH = 1000;
	private static final int CPHEIGHT = 1000;
	
	private EvoAI parent;
	
	public CameraPanel(EvoAI obj) {
		parent = obj;
		setBackground(Color.WHITE);
		
		setPreferredSize(new Dimension(CPWIDTH, CPHEIGHT));
	}
	
	@Override
	 public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		
		doDrawing(g);
		
	}
	
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	}

	public static int getCPWIDTH() {
		return CPWIDTH;
	}

	public static int getCPHEIGHT() {
		return CPHEIGHT;
	}
	
}

