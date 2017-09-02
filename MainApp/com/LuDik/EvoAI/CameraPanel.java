package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class CameraPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int CPWIDTH = 1000;
	private static final int CPHEIGHT = 1000;

	private EvoAI mainFrame;

	private double translationX;
	private double translationY;
	private double scale = 1;
	
	// TODO schrijf de hele translation en scale operatie om met behulp van xpos en ypos
	
	private static final double SCROLL_SPEED = 20;
	private static final double ZOOM_SPEED = 1.1;
	
	public CameraPanel(EvoAI parent) {
		mainFrame = parent;
		setBackground(Color.white);

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

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(translationX + ((CPWIDTH/2) * (1 - scale)), translationY + ((CPWIDTH/2) * (1 - scale)));

		g2d.scale(scale, scale);

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
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			System.out.println("mouseScrolled");
			if (e.getWheelRotation() > 0) {
				scale += ZOOM_SPEED;
			} else {
				scale -= ZOOM_SPEED;
			}
			repaint();
		}
	}

	class KeyInputHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			switch (keyCode) {

			case KeyEvent.VK_LEFT:
				if (translationX <= scale * CPWIDTH/2) {
					translationX += SCROLL_SPEED;
				}
				repaint();
				break;

			case KeyEvent.VK_RIGHT:
				if (translationX >= -scale * CPWIDTH/2) {
					translationX -= SCROLL_SPEED;
				}
				repaint();
				break;

			case KeyEvent.VK_DOWN:
				if (translationY >= -scale * CPWIDTH/2) {
					translationY -= SCROLL_SPEED;
				}
				repaint();
				break;

			case KeyEvent.VK_UP:
				if (translationY <= scale * CPWIDTH/2) {
					translationY += SCROLL_SPEED;
				}
				repaint();
				break;
				
			case KeyEvent.VK_ADD:
				if (translationY >= -scale * CPWIDTH/2 || translationX >= -scale * CPWIDTH/2) {
					scale *= ZOOM_SPEED;
				}
				repaint();
				break;
				
			case KeyEvent.VK_SUBTRACT:
				if (translationY <= scale * CPWIDTH/2 || translationX >= -scale * CPWIDTH/2) {
					scale /= ZOOM_SPEED;
				}
				repaint();
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
