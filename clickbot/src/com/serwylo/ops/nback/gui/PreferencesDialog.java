package com.serwylo.ops.nback.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.SpringLayout.Constraints;

import com.serwylo.ops.nback.core.NBackProperties;

@SuppressWarnings("serial")
public class PreferencesDialog extends JDialog
{
	
	private JTabbedPane tabNavigator = new JTabbedPane();

	// GLOBAL PROPERTIES:
	// These are applicable to all running instances of the task.
	private JPanel tabGlobal = new JPanel();

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
	
	
	private JPanel tabInteractiveTimed = new JPanel();
	
	
	private JPanel tabGui = new JPanel();

	private JSpinner inputNumberFontSize = new JSpinner();
	private JSpinner inputFocusFontSize = new JSpinner();
	private JButton btnChooseTextColour = new JButton( "Select" );
	private JButton btnChooseBackgroundColour = new JButton( "Select" );
	private JLabel labelShowTextColour = new JLabel();
	private JLabel labelShowBackgroundColour = new JLabel();

	public PreferencesDialog()
	{
		
		this.tabGlobal.setLayout( new GridBagLayout() );
		
		// == N Value ==
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGlobal.add( new JLabel( "N Value:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGlobal.add( inputN, constraints );
		inputN.setPreferredSize( new Dimension( 50, 20 ) );

		
		// == Target Percentage ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGlobal.add( new JLabel( "Target Percentage:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGlobal.add( inputTargetPercentage, constraints );
		inputTargetPercentage.setPreferredSize( new Dimension( 50, 20 ) );
		
		
		// == Total Numbers ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGlobal.add( new JLabel( "Total Numbers:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGlobal.add( inputTotalNumbers, constraints );
		inputTotalNumbers.setPreferredSize( new Dimension( 50, 20 ) );
		
		// == Time Between Numbers ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGlobal.add( new JLabel( "Time Between Numbers:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGlobal.add( inputTimeBetweenNumbers, constraints );
		inputTimeBetweenNumbers.setPreferredSize( new Dimension( 50, 20 ) );
		
		// == Total Time ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGlobal.add( new JLabel( "Total Time:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGlobal.add( inputTotalTime, constraints );
		inputTotalTime.setPreferredSize( new Dimension( 50, 20 ) );
		
		// == Always Random ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGlobal.add( new JLabel( "Always Random:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGlobal.add( inputAlwaysRandom, constraints );
		
		// == Random Seed ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGlobal.add( new JLabel( "Random Seed:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGlobal.add( inputRandomSeed, constraints );

		// == Save Directory ==
		constraints = new GridBagConstraints();
		constraints.gridx = 0; constraints.gridy = 7; 
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGlobal.add( new JLabel( "Save Directory:  " ), constraints );

		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 7;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		inputSaveDirectory.setPreferredSize( new Dimension( 200, 20 ) );
		this.tabGlobal.add( inputSaveDirectory, constraints );

		constraints = new GridBagConstraints();
		constraints.gridx = 2; constraints.gridy = 7;
		this.tabGlobal.add( btnChooseSaveDir, constraints );
		
		/*this.addInput( panel, inputSaveDirectory, "Directory To Save To", 7 );
		this.addInput( panel, inputNumbersBetweenFocus, "Numbers Beween Focus", 8 );
		this.addInput( panel, inputFocusTime, "Focus Time", 9 );
		this.addInput( panel, inputFocusTicks, "Focus Ticks", 10 );*/
		
		this.tabNavigator.addTab( "General", this.tabGlobal );
		
		
		this.tabGui.setLayout( new GridBagLayout() );

		// == Number Font Size ==
		constraints.gridx = 0; constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGui.add( new JLabel( "Number font size:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGui.add( inputNumberFontSize, constraints );
		inputNumberFontSize.setPreferredSize( new Dimension( 50, 20 ) );

		
		// == Focus Font Size ==
		constraints.gridx = 0; constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGui.add( new JLabel( "Focus font size:  " ), constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGui.add( inputFocusFontSize, constraints );
		inputFocusFontSize.setPreferredSize( new Dimension( 50, 20 ) );

		
		// == Background colour ==
		constraints.gridx = 0; constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGui.add( new JLabel( "Background colour:  " ), constraints );

		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 2;
		labelShowBackgroundColour.setPreferredSize( new Dimension( 60, 30 ) );
		labelShowBackgroundColour.setOpaque( true );
		labelShowBackgroundColour.setBackground( NBackProperties.DEFAULT_BACKGROUND_COLOUR );
		this.tabGui.add( labelShowBackgroundColour, constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 2; constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGui.add( btnChooseBackgroundColour, constraints );
		btnChooseBackgroundColour.addActionListener
		( 
			new ActionListener() 
			{
				@Override
				public void actionPerformed( ActionEvent e ) 
				{
					Color colour = JColorChooser.showDialog( null, "Background Colour", labelShowBackgroundColour.getBackground() );
					if ( colour != null )
					{
						labelShowBackgroundColour.setBackground( colour );
					}
				}
			}
		);
		
		
		// == Text colour ==
		constraints.gridx = 0; constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		this.tabGui.add( new JLabel( "Text colour:  " ), constraints );

		constraints = new GridBagConstraints();
		constraints.gridx = 1; constraints.gridy = 3;
		labelShowTextColour.setPreferredSize( new Dimension( 60, 30 ) );
		labelShowTextColour.setOpaque( true );
		labelShowTextColour.setBackground( NBackProperties.DEFAULT_TEXT_COLOUR );
		this.tabGui.add( labelShowTextColour, constraints );
		
		constraints = new GridBagConstraints();
		constraints.gridx = 2; constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.LINE_START;
		this.tabGui.add( btnChooseTextColour, constraints );
		btnChooseTextColour.addActionListener
		( 
			new ActionListener() 
			{
				@Override
				public void actionPerformed( ActionEvent e ) 
				{
					Color colour = JColorChooser.showDialog( null, "Text Colour", labelShowTextColour.getBackground() );
					if ( colour != null )
					{
						labelShowTextColour.setBackground( colour );
					}
				}
			}
		);
		
		this.tabNavigator.addTab( "Interface", this.tabGui );
		
		
		this.add( this.tabNavigator );
		this.setSize( 800, 600 );
		this.setVisible( true );
			
	}

	public static void main( String[] args )
	{
		new PreferencesDialog();
	}
	
}
