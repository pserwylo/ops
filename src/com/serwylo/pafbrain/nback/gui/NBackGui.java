package com.serwylo.pafbrain.nback.gui;
/*
 * Copyright (c) 2010 Peter Serwylo
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import com.serwylo.pafbrain.nback.core.AbstractNBack;
import com.serwylo.pafbrain.nback.io.SerializeResults;


@SuppressWarnings("serial")
public class NBackGui extends JFrame implements KeyListener, ActionListener, MouseListener
{

	private JLabel nBackLabel, focusLabel;
	private AbstractNBack nback;
	private String userId;
	
	public NBackGui( AbstractNBack nbackInstance, String userId )
	{
		this.nback = nbackInstance;
		this.nback.addActionListener( this );
		this.userId = userId;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.CENTER ) );
		
		this.nBackLabel = new JLabel( "NBack - Click to begin" );
		this.nBackLabel.setFont( new Font( Font.SERIF, Font.BOLD, 50 ) );
		this.nBackLabel.setHorizontalAlignment( JLabel.CENTER );
		panel.add( this.nBackLabel );
		
		this.focusLabel = new JLabel( "" );
		this.focusLabel.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 100 ) );
		this.focusLabel.setHorizontalAlignment( JLabel.CENTER );
		panel.add( this.focusLabel );
		
		this.addKeyListener( this );
		this.addMouseListener( this );
		
		this.setLayout( new GridBagLayout() );
		this.add( panel, new GridBagConstraints() );

		this.setSize( 800, 300 );
		this.setVisible( true );
		this.setExtendedState( JFrame.MAXIMIZED_BOTH );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		
	}

	public void saveResults( String participantId )
	{
		String path = this.nback.getProperties().getSaveDirectory() + File.separatorChar + "nback." + participantId + ".csv";
		System.out.println( "\nSaving file to: " + path );
		try
		{
			BufferedWriter output = new BufferedWriter( new FileWriter( path ) );
			output.write( SerializeResults.serializeResultList( "number,time,timedOut,timeFromNumberShown,timeFromStartOfTest,wasSuccessfull,wasTarget", this.nback.getResults() ) );
			output.close();
		}
		catch ( IOException ioe )
		{
			JOptionPane.showMessageDialog( null, "Error saving results to file '" + path + "'\n\n" + ioe.getMessage() );
		}
	}
	
	/**
	 * Receives events from the NBack object, and responds by showing appropriate 
	 * information in the GUI. 
	 * 	An ACTION_TICK will display the current number to the user
	 * 	An ACTION_FOCUS will display a + in the centre of the screen
	 * 	An ACTION_COMPLETE will tell the test to save its results, and then 
	 * 	display a completed message.
	 * We don't care whether these came from timed or interactive tests.
	 */
	public void actionPerformed( ActionEvent e )
	{
		if ( e.getID() == AbstractNBack.ACTION_TICK )
		{
			this.nBackLabel.setText( this.nback.getSequence().getCurrentNumber() + "" );
			this.focusLabel.setText( "" );
		}
		else if ( e.getID() == AbstractNBack.ACTION_FOCUS )
		{
			this.nBackLabel.setText( "" );
			this.focusLabel.setText( "+" );
		}
		else if ( e.getID() == AbstractNBack.ACTION_COMPLETE )
		{
			this.saveResults( this.userId );
			this.nBackLabel.setText( "Test Complete" );
		}
	}
	

	/**
	 * If keyboard is enabled, then the user can select whether an item is a 
	 * target or not by pressing Enter of Space.
	 * Regardless of whether the keyboard is enabled, pressing escape will 
	 * prompt the user to save results before closing the application.
	 */
	@Override
	public void keyPressed( KeyEvent e ) 
	{
		// If is target
		if ( e.getKeyCode() == KeyEvent.VK_ENTER )
		{
			this.nback.submitResult( true );
		}
		// If isn't target
		else if ( e.getKeyCode() == KeyEvent.VK_SPACE )
		{
			this.nback.submitResult( false );
		}
		else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
		{
			// May as well attempt to save the results if we quit early. They
			// wont be complete, but at least they will get something...
			if ( this.nback.hasStarted() && !this.nback.isCompleted() )
			{
				int result = JOptionPane.showConfirmDialog( this, "Save results?" );
				if ( result == JOptionPane.OK_OPTION )
				{
					this.saveResults( this.userId );
				}
				this.nback.stop();
			}
			System.exit( 1 );
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if ( !this.nback.hasStarted() )
		{
			this.nback.start();
		}
		else if ( ! this.nback.isCompleted() && ! this.nback.getProperties().isTimed() )
		{
			// Non timed tasks use the mouse click as a trigger to tick over to
			// the next number.
			this.nback.submitResult( false, true );
		}
	}

	@Override
	public void keyReleased( KeyEvent e ) {}

	@Override
	public void keyTyped( KeyEvent e ) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
