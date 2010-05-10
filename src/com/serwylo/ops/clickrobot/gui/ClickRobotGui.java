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
	private JSpinner spinnerInterval;
	private JToggleButton btnToggleStart;
	
	private Timer timer;
	
	public ClickRobotGui()
	{
		this.timer = new Timer( 1000, this );

		this.setTitle( "Fred the Mouse" );
		this.setLayout( new BorderLayout() );
		this.setMinimumSize( new Dimension( 220, 100 ) );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout() );
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
		panel.add( new JLabel( "Interval (ms): " ) );
		panel.add( this.spinnerInterval );
		this.add( panel, BorderLayout.NORTH );
		
		panel = new JPanel();
		panel.setLayout( new GridBagLayout() );
		this.btnToggleStart = new JToggleButton( "Start" );
		this.btnToggleStart.addActionListener( this );
		panel.add( this.btnToggleStart );
		this.add( panel, BorderLayout.CENTER );
		
		this.labelError = new JLabel();
		this.add( this.labelError, BorderLayout.SOUTH );
		
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
				Robot robot = new Robot();
				robot.mousePress( InputEvent.BUTTON1_MASK );
				robot.mouseRelease( InputEvent.BUTTON1_MASK );
			}
			catch ( AWTException awte )
			{
				this.labelError.setText( "Error attempting to click:\n" + awte.getMessage() );
				awte.printStackTrace();
				this.timer.stop();
			}
		}
	}
	
}
