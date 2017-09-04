package com.LuDik.EvoAI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class CameraPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int CPWIDTH = 1000;
	private static final int CPHEIGHT = 1000;

	private EvoAI mainFrame;

	private double cameraX = (CPWIDTH/2);
	private double cameraY = (CPWIDTH/2);
	private double scale = 1;
	
	private AffineTransform at = new AffineTransform();
	private AffineTransform middleTransform = new AffineTransform();
	
	// TODO schrijf de hele translation en scale operatie om met behulp van xpos en ypos
	
	private static final double SCROLL_SPEED = 20;
	private static final double ZOOM_SPEED_IN = 1.1;
	private static final double ZOOM_SPEED_OUT = 1d/1.1;
	
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
		
		AffineTransform saveXform = g2d.getTransform();
		
		
//		at.translate(cameraX + (cameraX * (1d - scale)), cameraY + (cameraY * (1d - scale)));
//		at.scale(scale, scale);
		
		g2d.transform(at);
		
//		g2d.translate(cameraX + (cameraX * (1d - scale)), cameraY + (cameraY * (1d - scale)));

//		g2d.scale(scale, scale);
//		g2d.translate(-(CPWIDTH/2), -(CPWIDTH/2));

		
		
		
		
		if (mainFrame.getBoard() != null) {
			mainFrame.getBoard().getMap().drawMap(g2d);
			mainFrame.getBoard().getMap().getTiles()[0][0];
		}
		
		
		
		g2d.setTransform(saveXform);
		
		g2d.setColor(Color.green);
		g2d.drawLine(0, CPWIDTH/2, CPWIDTH, CPWIDTH/2);
		g2d.drawLine(CPWIDTH/2, 0, CPWIDTH/2, CPWIDTH);
		
	}
	
	private void moveCamera(int amplitudeX, int amplitudeY) {
		at.translate(amplitudeX * SCROLL_SPEED, amplitudeY * SCROLL_SPEED);
		cameraX += amplitudeX * SCROLL_SPEED;
		cameraY += amplitudeY * SCROLL_SPEED;
		System.out.println("cameraX" + cameraX + "cameraY" + cameraY);
	}
	
	private void zoomCamera(boolean zoomIn) {
		double currentScale;
		
		if (zoomIn) {
			currentScale = ZOOM_SPEED_IN;
		} else {
			currentScale = ZOOM_SPEED_OUT;
		}
		
		at.translate(-cameraX * currentScale, -cameraY * currentScale);
		at.scale(currentScale, currentScale);
		at.translate(cameraX / currentScale, cameraY / currentScale);

//		middleTransform.translate(100D, 100D);
//		at.concatenate(middleTransform);
		
		scale *= currentScale;
		
		Rectangle2D rect = new Rectangle2D.Double(100d, 100d, 10d, 10d);
		
		Shape shape = at.createTransformedShape(rect);
		
		System.out.println("shape" + shape.getBounds2D());
		System.out.println("scale" + scale);
		
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
		
		
		public void mouseDown(MouseWheelEvent e) {
			System.out.println("mouseScrolled");
//			if (e.getWheelRotation() > 0) {
//				scale += ZOOM_SPEED;
//			} else {
//				scale -= ZOOM_SPEED;
//			}
			repaint();
		}
	}

	class KeyInputHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			switch (keyCode) {

			case KeyEvent.VK_LEFT:
				moveCamera(1, 0);
				repaint();
				break;

			case KeyEvent.VK_RIGHT:
				moveCamera(-1, 0);
				
				repaint();
				break;

			case KeyEvent.VK_DOWN:
				moveCamera(0, -1);
				
				repaint();
				break;

			case KeyEvent.VK_UP:
				moveCamera(0, 1);
				
				repaint();
				break;
				
			case KeyEvent.VK_ADD:
				zoomCamera(true);
				
				repaint();
				break;
				
			case KeyEvent.VK_SUBTRACT:
				zoomCamera(false);
				
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
