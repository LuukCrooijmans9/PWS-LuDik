package com.LuDik.EvoAI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.simplemove.Board;
import com.simplemove.Creature;

public class ActionPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
//	UI containers:
	private EvoAI mainFrame;
	private CameraPanel cameraPanel;
	
	
	private Board board;
	private Timer timer;
	
	private int APHeight ; // hoogte van camerapanel
	private final int APWidth  = 200;
	
	private Button startBoardBtn;
	private TextField boardTileSizeTF;
	private TextField boardMapSizeInTilesTF;
	
	public ActionPanel(EvoAI parent) {
		initActionPanel(parent);
	}
	
	private void initActionPanel(EvoAI parent) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(APWidth, APHeight));
		
		mainFrame = parent;
		
		boardTileSizeTF = new TextField("boardTileSize: " );
		add(boardTileSizeTF);
				
		boardMapSizeInTilesTF = new TextField("boardMapSizeInTiles: " );
		add(boardMapSizeInTilesTF);
		
		startBoardBtn = new Button("Start board");
		add(startBoardBtn);
		
		startBoardBtn.addActionListener(
				event -> board = new Board(
						Integer.valueOf(boardTileSizeTF.getText()), 
						Integer.valueOf(boardMapSizeInTilesTF.getText())));
						true

	}
}
