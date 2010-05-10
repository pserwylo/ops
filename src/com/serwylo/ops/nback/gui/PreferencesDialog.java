package com.serwylo.ops.nback.gui;

import java.awt.Dimension;
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
		panel.add( new JLabel( "N Value:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		panel.add( inputN, constraints );
		inputN.setPreferredSize( new Dimension( 50, 20 ) );

		
		// == Target Percentage ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Target Percentage:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		panel.add( inputTargetPercentage, constraints );
		inputTargetPercentage.setPreferredSize( new Dimension( 50, 20 ) );
		
		
		// == Total Numbers ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Total Numbers:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.LINE_START;
		panel.add( inputTotalNumbers, constraints );
		inputTotalNumbers.setPreferredSize( new Dimension( 50, 20 ) );
		
		// == Time Between Numbers ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Time Between Numbers:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.LINE_START;
		panel.add( inputTimeBetweenNumbers, constraints );
		inputTimeBetweenNumbers.setPreferredSize( new Dimension( 50, 20 ) );
		
		// == Total Time ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Total Time:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.LINE_START;
		panel.add( inputTotalTime, constraints );
		inputTotalTime.setPreferredSize( new Dimension( 50, 20 ) );
		
		// == Always Random ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Always Random:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.LINE_START;
		panel.add( inputAlwaysRandom, constraints );
		
		// == Random Seed ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Random Seed:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.LINE_START;
		panel.add( inputRandomSeed, constraints );

		// == Save Directory ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 7; 
		constraints.anchor = GridBagConstraints.EAST;
		panel.add( new JLabel( "Save Directory:  " ), constraints );

		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 7;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		inputSaveDirectory.setPreferredSize( new Dimension( 200, 20 ) );
		panel.add( inputSaveDirectory, constraints );

		constraints = new GridBagConstraints();
		constraints.gridx = 2; constraints.gridy = 7;
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

	public static void main( String[] args )
	{
		new PreferencesDialog();
	}
	
}
