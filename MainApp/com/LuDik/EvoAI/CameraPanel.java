package com.LuDik.EvoAI;

import java.awt.BasicStroke;
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
import java.util.ArrayList;

import javax.swing.JPanel;

public class CameraPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int CPWIDTH = 1000;
	private static final int CPHEIGHT = 1000;

	private EvoAI mainFrame;

	private double cameraX = (CPWIDTH/2);
	private double cameraY = (CPWIDTH/2);
	private double scale = 1;
	
	private boolean followSelectedCreature;
	private boolean controlCrtr;
	
	private AffineTransform saveXform;
	private AffineTransform scaleT;
	private AffineTransform translateT;

	private Creature selectedCreature;
		
	private static final double SCROLL_SPEED = 20;
	private static final double ZOOM_SPEED_IN = 1.1;
	private static final double ZOOM_SPEED_OUT = 1d/1.1;
	
	private int x,y;

	
	public CameraPanel(EvoAI parent) {
		mainFrame = parent;
		setBackground(Color.white);

		setPreferredSize(new Dimension(CPWIDTH, CPHEIGHT));
		setFocusable(true);
		addKeyListener(new KeyInputHandler());
		addMouseListener(new MouseInputHandler());
		addMouseMotionListener(new MouseInputHandler());
		addMouseWheelListener(new MouseInputHandler());
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(isFollowSelectedCreature()) {
			setCameraOnCreature(selectedCreature);
		}
		
		doDrawing(g);

	}


	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		AffineTransform saveXform = g2d.getTransform();
		
		g2d.setStroke(new BasicStroke(0.0001f));
		
		scaleT = new AffineTransform();
		translateT = new AffineTransform();
		
		scaleT.scale(scale, scale);
		translateT.translate(
				0.5 * CPWIDTH / scale - (cameraX),
				0.5 * CPWIDTH / scale - (cameraY));
				
		scaleT.concatenate(translateT);		
		g2d.setTransform(scaleT);	
		
		if (mainFrame.getBoard() != null) {
			mainFrame.getBoard().drawBoard(g2d);
			
		}
		
		
		
		g2d.setTransform(saveXform);
		
//		debug lijnen, uncomment als je twee lijnen die door het midden van het scherm gaan wil hebben.
		g2d.setColor(Color.green);
		g2d.drawLine(0, CPWIDTH/2, CPWIDTH, CPWIDTH/2);
		g2d.drawLine(CPWIDTH/2, 0, CPWIDTH/2, CPWIDTH);
		 
		
		
		g2d.dispose();
		
	}
	
	private void moveCamera(double amplitudeX, double amplitudeY) {
		cameraX += amplitudeX;
		cameraY += amplitudeY;
	}
	
	private void zoomCamera(boolean zoomIn) {
		double currentScale;
		
		if (zoomIn) {
			currentScale = ZOOM_SPEED_IN;
		} else {
			currentScale = ZOOM_SPEED_OUT;
		}
		
		scale *= currentScale;		
	}
	
	private void setCameraOnCreature(Creature crtr) {
		cameraX = crtr.getXPos();
		cameraY = crtr.getYPos();
	}
	
	class MouseInputHandler extends MouseAdapter {
		


		@Override
		public void mousePressed(MouseEvent e) {
			x = e.getX();
			y = e.getY();
			ArrayList<Creature> creatures = mainFrame.getBoard().getCreatures();
			
			mainFrame.getInfoPanel().updateMousePos(x, y);
			for (Creature crtr : creatures) {
				if (scaleT.createTransformedShape(crtr.getCreatureShape()).contains((double) x, (double) y)) {
					
					if (selectedCreature != null) {
						selectedCreature.setSelected(false);
					} else {
						mainFrame.getActionPanel().setControlCrtrBtnEnabled(true);
						mainFrame.getActionPanel().setFollowCrtrBtnEnabled(true);
					}
					selectedCreature = crtr;
					
					mainFrame.getInfoPanel().setSelectedCreature(selectedCreature);
//					selectedCreature.setCreatureColor(Color.blue);
					selectedCreature.setSelected(true);
					break;
				}
			}
//	           System.out.println(x+ " , "+ y);

//			System.out.println("mousepressed");
		}
		
		 @Override
		    public void mouseDragged(MouseEvent e) {
//		        System.out.println("mouseDragged");
		           double dx = e.getX() - x;
		           double dy = e.getY() - y;
		           
		           moveCamera(-dx / scale,-dy / scale);
		           x = e.getX();
		           y = e.getY();
		           
		           repaint();
		    }
		
		@Override
		public void mouseEntered(MouseEvent e) {
			requestFocusInWindow();
//			System.out.println("mouseEntered");
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
//			System.out.println("mouseScrolled");	
			if(e.getWheelRotation() < 0) {
				zoomCamera(true);
			} else {
				zoomCamera(false);
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
				
				
				repaint();
				break;

			case KeyEvent.VK_RIGHT:
				if (controlCrtr) 
				
				repaint();
				break;

			case KeyEvent.VK_DOWN:
				
				
				repaint();
				break;

			case KeyEvent.VK_UP:
				
				
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

	public boolean isFollowSelectedCreature() {
		return followSelectedCreature;
	}

	public void setFollowSelectedCreature(boolean followSelectedCreature) {
		this.followSelectedCreature = followSelectedCreature;
	}

	public void setControlCrtr(boolean controlCrtr) {
		this.controlCrtr = controlCrtr;
		selectedCreature.setControlled(controlCrtr);
	}
	
	public boolean isControlCrtr() {
		return controlCrtr;
	}

}
