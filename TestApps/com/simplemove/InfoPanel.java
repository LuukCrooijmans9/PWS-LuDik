package com.simplemove;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.*;

public class InfoPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int IPHeight = Board.getBHeight();
	private static int IPWidth = 200;
	private Board board;
	
	private Button startBtn;
	private Button disableTurningSpeedBtn;
	private Button attractToCenterBtn;
	
	private TextField centerPull;
	private TextField randomFactorTurn;
	private TextField randomFactorAcc;
	private TextField maxSpeed;
	
	private TextField totalCreatures;
	private TextField crtrSize;
	private TextField delay;
	
	
	
//	private Label orientationLabel;
//	private Label speedLabel;
//	private Label turningSpeedLabel;
	private Label stepLabel;
	
	
	Creature crtr;

	
	public InfoPanel(Board startableBoard) {
		initInfoPanel(startableBoard);
	}
	
	private void initInfoPanel(Board startableBoard) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(IPWidth, IPHeight));
		
		board = startableBoard;
		
		startBtn = new Button("Start");
		add(startBtn);
		
		disableTurningSpeedBtn = new Button("graphicalMode: " + board.isGraphicalMode());
		add(disableTurningSpeedBtn);
		
		attractToCenterBtn = new Button("attractToCenter: true");
		add(attractToCenterBtn);
		
		
		centerPull = new TextField("centerPull: " + Creature.getCenterPull());
		add(centerPull);
		
		randomFactorTurn = new TextField("randomFactorTurn: " + Creature.getRandomFactorTurn());
		add(randomFactorTurn);
		
		randomFactorAcc = new TextField("randomFactorAcc: " + Creature.getRandomFactorAcc());
		add(randomFactorAcc);
		
		maxSpeed = new TextField("maxSpeed: " + Creature.getMaxSpeed());
		add(maxSpeed);
		
		totalCreatures = new TextField("totalCreatures: " + startableBoard.getTotalCreatures());
		add(totalCreatures);
		
		crtrSize = new TextField("crtrSize: " + Creature.getCrtrSize());
		add(crtrSize);
		
		delay = new TextField("delay: " + board.getDelay());
		add(delay);
		
		centerPull.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Creature.setCenterPull(Double.parseDouble(centerPull.getText()));
					centerPull.setText("centerPull: " + Creature.getCenterPull());
				} catch (Exception e) {
					centerPull.setText("centerPull: " + Creature.getCenterPull());
				}
			}
		});
		
		randomFactorTurn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Creature.setRandomFactorTurn(Double.parseDouble(randomFactorTurn.getText()));
					randomFactorTurn.setText("randomFactorTurn: " + Creature.getRandomFactorTurn());
				} catch (Exception e) {
					randomFactorTurn.setText("randomFactorTurn: " + Creature.getRandomFactorTurn());
				}
			}
		});
		
		randomFactorAcc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Creature.setRandomFactorAcc(Double.parseDouble(randomFactorAcc.getText()));
					randomFactorAcc.setText("randomFactorAcc: " + Creature.getRandomFactorAcc());
				} catch (Exception e) {
					randomFactorAcc.setText("randomFactorAcc: " + Creature.getRandomFactorAcc());
				}
			}
		});
		
		maxSpeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Creature.setMaxSpeed(Double.parseDouble(maxSpeed.getText()));
					maxSpeed.setText("maxSpeed: " + Creature.getMaxSpeed());
				} catch (Exception e) {
					maxSpeed.setText("maxSpeed: " + Creature.getMaxSpeed());
				}
			}
		});
		
		totalCreatures.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					startableBoard.setTotalCreatures((int) Double.parseDouble(totalCreatures.getText()));
					totalCreatures.setText("totalCreatures: " + startableBoard.getTotalCreatures());
					startableBoard.initBoard();
				} catch (Exception e) {
					totalCreatures.setText("totalCreatures: " + startableBoard.getTotalCreatures());
				}
			}
		});

		crtrSize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Creature.setCrtrSize((int) Double.parseDouble(crtrSize.getText()));
					crtrSize.setText("crtrSize: " + Creature.getCrtrSize());
					startableBoard.initBoard();

				} catch (Exception e) {
					crtrSize.setText("crtrSize: " + Creature.getCrtrSize());
				}
			}
		});
		
		delay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					board.setDelay((int) Double.parseDouble(delay.getText()));
					delay.setText("delay: " + board.getDelay());
					startableBoard.initBoard();
					
				} catch (Exception e) {
					delay.setText("delay: " + board.getDelay());
				}
			}
		});
		
		disableTurningSpeedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				board.setGraphicalMode(!board.isGraphicalMode());
				disableTurningSpeedBtn.setLabel("graphicalMode: " + board.isGraphicalMode());
			}
		});
		
		attractToCenterBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Creature.setAttractToCenter(!Creature.getAttractToCenter());
				attractToCenterBtn.setLabel("attractToCenter: " + Creature.getAttractToCenter());
			}
		});
		
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				board.start();
			}
		});
		
//		crtr = board.getCreature();
		
		stepLabel = new Label("step = " + startableBoard.getStep() + "AverageTime :" + board.getTotTimeAverage());
		add(stepLabel);
		
//		speedLabel = new Label("crtr.getSpeed() = " + crtr.getSpeed());
//		add(speedLabel);
//		
//		turningSpeedLabel = new Label("crtr.getTurningSpeed() = " + crtr.getTurningSpeed());
//		add(turningSpeedLabel);
	}
	
	public void notifyTimePassed() {
//		orientationLabel.setText("crtr.getOrientation() = " + (crtr.getOrientation()) % 360);
//		speedLabel.setText("crtr.getSpeed() = " + crtr.getSpeed());
		stepLabel.setText("step = " + board.getStep() + "AverageTime :" + board.getTotTimeAverage());
		
	}
	
	static int getIPHeight() {
		return IPHeight;
	}

	static int getIPWidth() {
		return IPWidth;
	}
}
