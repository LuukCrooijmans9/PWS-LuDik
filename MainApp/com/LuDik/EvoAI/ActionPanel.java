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
	private TextField smoothnessTF;

	public ActionPanel(EvoAI parent) {
		initActionPanel(parent);
	}

	private void initActionPanel(EvoAI parent) {
		APHeight = parent.getCameraPanel().getCPHEIGHT();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(APWidth, APHeight));

		mainFrame = parent;

		boardTileSizeTF = new TextField("" + Configuration.DEFAULT_TILE_SIZE);
		add(boardTileSizeTF);

		boardMapSizeInTilesTF = new TextField("" + Configuration.DEFAULT_MAP_SIZE_IN_TILES);
		add(boardMapSizeInTilesTF);
		
		smoothnessTF = new TextField("" + Configuration.DEFAULT_SMOOTHNESS);
		add(smoothnessTF);

		startBoardBtn = new Button("Start board");
		add(startBoardBtn);

		startBoardBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					board = new Board(
							Integer.valueOf(boardTileSizeTF.getText()),
							Integer.valueOf(boardMapSizeInTilesTF.getText()),
							Configuration.DEFAULT_SEED,
							Double.valueOf(smoothnessTF.getText()),
							mainFrame
							);
					
				} catch (NumberFormatException e) {
					board = new Board(
							Configuration.DEFAULT_TILE_SIZE,
							Configuration.DEFAULT_MAP_SIZE_IN_TILES,
							Configuration.DEFAULT_SEED,
							mainFrame
							);
					startBoardBtn.setLabel("Taking default values...");

				} 
				boardTileSizeTF.setText("" + Configuration.tileSize);
				boardMapSizeInTilesTF.setText("" + Configuration.mapSizeInTiles);
//				boardTileSizeTF.setEditable(false);
//				boardMapSizeInTilesTF.setEditable(false);
//				startBoardBtn.setEnabled(false);
				
				mainFrame.getCameraPanel().update();
			};
		});
	}
}
