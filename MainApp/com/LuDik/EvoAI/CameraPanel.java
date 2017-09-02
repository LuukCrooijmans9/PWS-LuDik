package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class CameraPanel extends JPanel {

	private static final int CPWIDTH = 1000;
	private static final int CPHEIGHT = 1000;

	private EvoAI mainFrame;

	public CameraPanel(EvoAI parent) {
		mainFrame = parent;
		setBackground(Color.WHITE);

		setPreferredSize(new Dimension(CPWIDTH, CPHEIGHT));
		setFocusable(true);
		addKeyListener(new KeyInputHandler());
		addMouseListener(new MouseInputHandler());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		doDrawing(g);

	}

	boolean test = false;

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (mainFrame.getBoard() != null) {
			mainFrame.getBoard().getMap().drawMap(g2d);
		}

	}

	class MouseInputHandler extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			requestFocusInWindow();
			System.out.println("mousepressed");
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			requestFocusInWindow();
			System.out.println("mouseEntered");
		}
	}

	class KeyInputHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			switch (keyCode) {

			case KeyEvent.VK_LEFT:
				break;

			case KeyEvent.VK_RIGHT:
				break;

			case KeyEvent.VK_DOWN:
				break;

			case KeyEvent.VK_UP:
				break;

			case KeyEvent.VK_SPACE:
				break;
			}
		}
	}

	public void update() {
		repaint();
	}

	public static int getCPWIDTH() {
		return CPWIDTH;
	}

	public static int getCPHEIGHT() {
		return CPHEIGHT;
	}

}
