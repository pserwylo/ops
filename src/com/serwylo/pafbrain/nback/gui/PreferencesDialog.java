package com.serwylo.pafbrain.nback.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.SpringLayout.Constraints;

@SuppressWarnings("serial")
public class PreferencesDialog extends JDialog
{

	private JSpinner inputN = new JSpinner();
	private JSpinner inputTargetPercentage = new JSpinner();
	private JSpinner inputTotalNumbers = new JSpinner();
	private JSpinner inputTotalTime = new JSpinner();
	private JSpinner inputTimeBetweenNumbers = new JSpinner();
	private JCheckBox inputAlwaysRandom = new JCheckBox();
	private JSpinner inputRandomSeed = new JSpinner();
	private JTextField inputSaveDirectory = new JTextField();
	private JButton btnChooseSaveDir = new JButton( "Browse" );
	private JSpinner inputNumbersBetweenFocus = new JSpinner();
	private JSpinner inputFocusTime = new JSpinner();
	private JSpinner inputFocusTicks = new JSpinner();

	public PreferencesDialog()
	{
		
		JPanel panel = new JPanel();
		panel.setLayout( new GridBagLayout() );
		
		
		// == N Value ==
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "N Value" ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add( inputN, constraints );

		
		// == Target Percentage ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Target Percentage" ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add( inputTargetPercentage, constraints );

		
		// == Total Numbers ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Total Numbers" ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add( inputTotalNumbers, constraints );

		// == Time Between Numbers ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Time Between Numbers" ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 3;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add( inputTimeBetweenNumbers, constraints );

		// == Always Random ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Always Random" ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 4;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add( inputAlwaysRandom, constraints );

		// == Random Seed ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Random Seed" ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 5;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add( inputRandomSeed, constraints );

		// == Save Directory ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 6; 
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Save Directory" ), constraints );

		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 6;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add( inputSaveDirectory, constraints );

		constraints = new GridBagConstraints();
		constraints.gridx = 2; constraints.gridy = 6;
		panel.add( btnChooseSaveDir, constraints );
		
		/*this.addInput( panel, inputSaveDirectory, "Directory To Save To", 7 );
		this.addInput( panel, inputNumbersBetweenFocus, "Numbers Beween Focus", 8 );
		this.addInput( panel, inputFocusTime, "Focus Time", 9 );
		this.addInput( panel, inputFocusTicks, "Focus Ticks", 10 );*/
				
		this.add( panel );
		panel.setSize( 800, 600 );
		this.setSize( 800, 600 );
		this.setVisible( true );
			
	}

	private void addInput( JPanel panel, JComponent inputComponent, String label, int row )
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = row;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( label ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = row;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		panel.add( inputComponent, constraints );
	}
	
	public static void main( String[] args )
	{
		new PreferencesDialog();
	}
	
}
