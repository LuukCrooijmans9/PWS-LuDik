package com.LuDik.EvoAI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Color;
import javax.swing.UIManager;

public class BrainVisualisedWindow extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private BrainVisualisedPanel brainVisualisedPanel;
	private CameraPanel cameraPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			BrainVisualisedWindow dialog = new BrainVisualisedWindow(new CameraPanel(new DARWIN()));
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Create the dialog.
	 */
	public BrainVisualisedWindow(CameraPanel cameraPanel) {
		this.cameraPanel = cameraPanel;
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 470, 547);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		brainVisualisedPanel = new BrainVisualisedPanel(cameraPanel);
		contentPanel.add(brainVisualisedPanel);
		
		
		
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent e) {
		        update();          
		    }
		});
	}
	
	public void update() {
		brainVisualisedPanel.update();
		if (cameraPanel.getSelectedCreature() != null ) {
			setTitle("Brain of: Creature" + cameraPanel.getSelectedCreature().getCreatureID());
		} else {
			setTitle("Brain of: no creature selected");
		}
	}

}
