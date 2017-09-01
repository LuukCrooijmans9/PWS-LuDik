package com.LuDik.EvoAI;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// UI containers:
	private EvoAI mainFrame;
	private CameraPanel cameraPanel;

	private Board board;
	private Timer timer;

	private static int APHeight;
	private static final int APWidth = 400;

	private Button startBoardBtn;
	private TextField boardTileSizeTF;
	private TextField boardMapSizeInTilesTF;

	public ActionPanel(EvoAI parent) {
		initActionPanel(parent);
	}

	private void initActionPanel(EvoAI parent) {
		APHeight = parent.getCameraPanel().getCPHEIGHT();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(APWidth, APHeight));

		mainFrame = parent;

		boardTileSizeTF = new TextField("boardTileSize: ");
		add(boardTileSizeTF);

		boardMapSizeInTilesTF = new TextField("boardMapSizeInTiles: ");
		add(boardMapSizeInTilesTF);

		startBoardBtn = new Button("Start board");
		add(startBoardBtn);

		startBoardBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					board = new Board(
							Integer.valueOf(boardTileSizeTF.getText()),
							Integer.valueOf(boardMapSizeInTilesTF.getText()),
							mainFrame
							);
					
				} catch (NumberFormatException e) {
					board = new Board(
							Configuration.DEFAULTTILESIZE,
							Configuration.DEFAULTMAPSIZEINTILES,
							mainFrame
							);
					startBoardBtn.setLabel("Taking default values...");

				} 
				boardTileSizeTF.setText("boardTileSize: " + Configuration.tileSize);
				boardMapSizeInTilesTF.setText("boardMapSizeInTiles: " + Configuration.mapSizeInTiles);
				boardTileSizeTF.setEditable(false);
				boardMapSizeInTilesTF.setEditable(false);
				startBoardBtn.setEnabled(false);
				
				mainFrame.getCameraPanel().update();
			};
		});
	}
}
