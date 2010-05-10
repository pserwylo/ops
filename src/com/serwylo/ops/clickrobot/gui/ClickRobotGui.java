package com.serwylo.ops.clickrobot.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Simple interface to the AWT Robot class, which simulates mouse clicks 
 * continually with a specified interval.
 * @author Peter Serwylo
 *
 */
public class ClickRobotGui extends JFrame implements ActionListener
{

	private static final long serialVersionUID = -8580140189070468490L;

	private JLabel labelError;
	private JSpinner spinnerInterval, spinnerCountdown;
	private JToggleButton btnToggleStart;
	
	private Timer timer;
	
	public ClickRobotGui()
	{
		this.timer = new Timer( 1000, this );

		this.setTitle( "Fred the Mouse" );
		this.setLayout( new GridBagLayout() );
		this.setMinimumSize( new Dimension( 250, 130 ) );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );

		this.spinnerInterval = new JSpinner( new SpinnerNumberModel( 1000, 100, Integer.MAX_VALUE, 100 ) );
		this.spinnerInterval.setValue( 1000 );
		this.spinnerInterval.setPreferredSize( new Dimension( 80, 20 ) );
		this.spinnerInterval.addChangeListener
		( 
			new ChangeListener() 
			{
				@Override
				public void stateChanged( ChangeEvent e ) 
				{
					timer.setDelay( (Integer)spinnerInterval.getValue() );
				}
			}
		);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets( 4, 4, 4, 4 );
		constraints.gridx = 0;
		constraints.gridy = 0;
		this.add( new JLabel( "Interval (ms): " ), constraints );
		
		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		this.add( this.spinnerInterval, constraints );

		
		this.spinnerCountdown = new JSpinner( new SpinnerNumberModel( 30000, 1000, Integer.MAX_VALUE, 1000 ) );
		this.spinnerCountdown.setValue( 30000 );
		this.spinnerCountdown.setPreferredSize( new Dimension( 80, 20 ) );
		
		constraints = new GridBagConstraints();
		constraints.insets = new Insets( 4, 4, 4, 4 );
		constraints.gridx = 0;
		constraints.gridy = 1;
		this.add( new JLabel( "Stop in (ms): " ), constraints );
		
		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		this.add( this.spinnerCountdown, constraints );
		
		JPanel panel = new JPanel();
		panel.setLayout( new GridBagLayout() );
		this.btnToggleStart = new JToggleButton( "Start" );
		this.btnToggleStart.addActionListener( this );
		panel.add( this.btnToggleStart, new GridBagConstraints() );
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0; constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.insets = new Insets( 4, 4, 4, 4 );
		this.add( panel, constraints );
		
		this.setVisible( true );
		
	}

	/**
	 * Listens for both start/stop events from the button, and from timer events
	 * to simulate the click.
	 */
	@Override
	public void actionPerformed( ActionEvent e ) 
	{
		if ( e.getSource() == this.btnToggleStart )
		{
			if ( btnToggleStart.isSelected() )
			{
				btnToggleStart.setText( "Stop" );
				this.timer.setDelay( (Integer)this.spinnerInterval.getValue() );
				this.timer.start();
			}
			else
			{
				btnToggleStart.setText( "Start" );
				this.timer.stop();
			}
		}
		else if ( e.getSource() == this.timer )
		{
			
			try
			{
				this.spinnerCountdown.setValue( (Integer)spinnerCountdown.getValue() - (Integer)spinnerInterval.getValue() );
				Robot robot = new Robot();
				robot.mousePress( InputEvent.BUTTON1_MASK );
				robot.mouseRelease( InputEvent.BUTTON1_MASK );
			}
			catch ( AWTException awte )
			{
				JOptionPane.showMessageDialog( this, "Error attempting to click:\n" + awte.getMessage() );
				awte.printStackTrace();
				this.timer.stop();
				this.btnToggleStart.setText( "Start" );
				this.btnToggleStart.setSelected( false );
			}
		
			if ( (Integer)this.spinnerCountdown.getValue() <= 0 )
			{
				this.timer.stop();
				this.btnToggleStart.setText( "Start" );
				this.btnToggleStart.setSelected( false );
			}
		}
	}
	
}
