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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.Timer;


public class NBack implements ActionListener
{

	public static final int ACTION_TICK = 1;
	public static final int ACTION_FOCUS = 2;
	public static final int ACTION_COMPLETE = 3;
	
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>( 1 );
	private ArrayList<Integer> history = new ArrayList<Integer>();
	private ArrayList<Result> results = new ArrayList<Result>();
	
	private NBackProperties properties;
	
	private boolean hasStarted = false;
	private boolean hasResultForThisTime = false;
	
	private int currentNumber;
	private boolean currentIsTarget;
	private long timeNumberShown;
	private long timeStartedTest;
	private boolean isFocusing;

	private Timer timer;
	private Random random;
	
	public NBack()
	{
		this.properties = new NBackProperties();
		this.properties.read();
	}
	
	public void start()
	{
		if ( !this.hasStarted )
		{
			random = new Random( this.properties.getRandomSeed() );
			this.hasStarted = true;
			this.timer = new Timer( this.properties.getTimeBetweenNumbers(), this );
			this.timer.setInitialDelay( 0 );
			this.timer.setRepeats( true );
			this.timeStartedTest = System.currentTimeMillis();
			this.timer.start();
		}
	}

	private void submitResult( boolean isTarget, boolean wasForced )
	{
		if ( !this.isFocusing )
		{
			int timeFromStart = (int)( System.currentTimeMillis() - this.timeStartedTest );
			int timeFromNumberShown = (int)( System.currentTimeMillis() - this.timeNumberShown );
			this.results.add( new Result( currentNumber, isTarget == currentIsTarget, currentIsTarget, wasForced, timeFromStart, timeFromNumberShown ) );
			
			if ( !wasForced && !this.hasResultForThisTime )
			{
				this.hasResultForThisTime = true;
				this.timer.setInitialDelay( 0 );
				this.timer.restart();
			
			}
		}
	}
	
	public void submitResult( boolean isTarget )
	{
		this.submitResult( isTarget, false );
	}

	public boolean isCurrentlyTarget() { return this.currentIsTarget; }
	public int getCurrentNumber() { return this.currentNumber; }
	public void addActionListener( ActionListener listener ) { this.listeners.add( listener ); }
	
	public void saveResults( String participantId )
	{
		String path = this.properties.getSaveDirectory() + File.separatorChar + "nback." + participantId + ".csv";
		System.out.println( "\nSaving file to: " + path );
		try
		{
			BufferedWriter output = new BufferedWriter( new FileWriter( path ) );
			output.write
			( 
				"Number," +
				"Target?," +
				"Successfull?," +
				"Response," +
				"Time\n" 
			);
			for ( int i = 0; i < this.results.size(); i ++ )
			{
				Result result = this.results.get( i );
				output.write
				( 
					result.number + "," + 
					result.wasTarget + "," + 
					result.wasSuccessfull + "," + 
					result.timedOut + "," + 
					result.timeFromNumberShown + "\n" 
				);
			}
			output.close();
		}
		catch ( IOException ioe )
		{
			JOptionPane.showMessageDialog( null, "Error saving results to file '" + path + "'\n\n" + ioe.getMessage() );
		}
	}
	
	@Override
	public void actionPerformed( ActionEvent e ) 
	{
		// If the user didn't submit an answer, then force an answer from them...
		if ( !hasResultForThisTime && history.size() > 0 )
		{
			this.submitResult( false, true );
		}
		else
		{
			hasResultForThisTime = false;
		}
	
		// When we are done, stop the timer, alert the listeners, and then return.
		if ( this.history.size() >= this.properties.getTotalNumbers() )
		{
			this.dispatchEvent( ACTION_COMPLETE );
			this.timer.stop();
			return;
		}
		
		// If we hit the amount of numbers that we need to focus, then we do that...
		int numbersSinceLastFocus = this.history.size() % this.properties.getNumbersBetweenFocus();
		if ( this.properties.requiresFocus() 
			&& !this.isFocusing
			&& numbersSinceLastFocus == this.properties.getNumbersBetweenFocus() - 1 )
		{
			this.isFocusing = true;
			this.timer.setInitialDelay( this.properties.getFocusTime() );
			this.timer.restart();
			System.out.print( " FOCUS" );
			this.dispatchEvent( ACTION_FOCUS );
			return;
		}
		// And if we have just returned from a focus, then we need to reset the focus counter
		// and reassign an appropriate timeout to the timer...
		else if ( this.isFocusing )
		{
			this.isFocusing = false;
			this.timer.setDelay( this.properties.getTimeBetweenNumbers() );
		}
		
		// Otherwise we want to set up a new number...
		int targetNumber = -1;
		boolean makeTarget = false;
		int number;
		
		// Cannot create a target unless there is enough items in the history to go back that far...
		if ( history.size() > this.properties.getN() )
		{
			makeTarget = this.random.nextFloat() < this.properties.getTargetPercentage();
			targetNumber = history.get( history.size() - this.properties.getN() );
		}
		
		if ( makeTarget )
		{
			number = targetNumber;
		}
		else
		{
			do 
			{
				number = (int)( ( this.random.nextFloat() * 10000 ) / 1000 );
			} while ( number == targetNumber || number == this.currentNumber );
		}
		
		this.currentIsTarget = makeTarget;
		this.currentNumber = number;
		this.timeNumberShown = System.currentTimeMillis();
		this.history.add( number );
		
		System.out.print( " " + number + ( makeTarget ? "T" : "" ) );
		
		// Alert the GUI or whoever else cares that we have just ticked over...
		this.dispatchEvent( ACTION_TICK );
	}
	
	/**
	 * Send an event with id of actionId to any listeners.
	 * @param actionId
	 */
	private void dispatchEvent( int actionId )
	{
		for ( ActionListener listener : this.listeners )
		{
			listener.actionPerformed( new ActionEvent( this, actionId, "" ) );
		}	
	}
		
}
