package com.serwylo.pafbrain.nback;
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.Timer;


public class NBack
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
	private boolean isCompleted = false;
	
	private int currentNumber;
	private boolean currentIsTarget;
	private long timeNumberShown;
	private long timeStartedTest;
	private int numbersSinceLastFocus;
	private int currentFocusDuration;
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
			this.timeStartedTest = System.currentTimeMillis();
			
			if ( this.properties.isTimed() )
			{
				this.timer = new Timer
				( 
					this.properties.getTimeBetweenNumbers(), 
					new ActionListener() 
					{	
						public void actionPerformed( ActionEvent e ) { nextNumber(); }
					}
				);
				this.timer.setInitialDelay( 0 );
				this.timer.setRepeats( true );
				this.timer.start();
			}
			else
			{
				// Manually start the first next number if not timed...
				this.nextNumber();
			}
		}
	}
	
	/**
	 * Prevents the test from proceeding by stopping the timer.
	 * No more events will be dispatched once stopped.
	 */
	public void stop()
	{
		if ( this.hasStarted && this.properties.isTimed() )
		{
			this.timer.stop();
		}
	}

	public void submitResult( boolean isTarget, boolean wasForced )
	{
		if ( this.hasStarted && ( ! this.isFocusing || ! this.properties.isTimed() ) )
		{
			int timeFromStart = (int)( System.currentTimeMillis() - this.timeStartedTest );
			int timeFromNumberShown = (int)( System.currentTimeMillis() - this.timeNumberShown );
			this.results.add( new Result( currentNumber, isTarget == currentIsTarget, currentIsTarget, wasForced, timeFromStart, timeFromNumberShown ) );
			
			if ( ! this.hasResultForThisTime && ( ! wasForced || ! this.properties.isTimed() ) )
			{
				this.hasResultForThisTime = true;
				
				if ( this.properties.isTimed() )
				{
					this.timer.setInitialDelay( 0 );
					this.timer.restart();
				}
				else
				{
					this.nextNumber();
				}
			}
		}
	}
	
	public void submitResult( boolean isTarget )
	{
		this.submitResult( isTarget, false );
	}

	public boolean hasStarted() { return this.hasStarted; }
	public boolean isCompleted() { return this.isCompleted; }
	public boolean isCurrentlyTarget() { return this.currentIsTarget; }
	public int getCurrentNumber() { return this.currentNumber; }
	public void addActionListener( ActionListener listener ) { this.listeners.add( listener ); }
	public NBackProperties getProperties() { return this.properties; }
		
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
	
	/**
	 * Proceed the test by selecting the next number and displaying it.
	 * If we are properties.getNumbersBetweenFocus() from the last focus, we will 
	 * for-go selecting a number in favour of pausing and focusing.
	 */
	public void nextNumber() 
	{
		// If the user didn't submit an answer, then force an answer from them...
		if ( ! hasResultForThisTime && history.size() > 0 )
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
			this.stop();
			this.isCompleted = true;
			return;
		}
		
		// If we hit the amount of numbers that we need to focus, then we do that...
		if ( this.properties.requiresFocus() 
			&& ! this.isFocusing
			&& this.numbersSinceLastFocus == this.properties.getNumbersBetweenFocus() )
		{
			this.isFocusing = true;
			this.currentFocusDuration = 1;
			
			if ( this.properties.isTimed() )
			{
				this.timer.setInitialDelay( this.properties.getFocusTime() );
				this.timer.restart();
			}

			this.numbersSinceLastFocus = 0;
			System.out.print( " FOCUS+" );
			this.dispatchEvent( ACTION_FOCUS );
			return;
		}
		// If in manual mode, then make sure we allow a number of ticks before stopping focusing..
		else if ( this.isFocusing && !this.properties.isTimed() && this.currentFocusDuration < this.properties.getFocusTicks() )
		{
			this.currentFocusDuration ++;
			System.out.print( "+" );
			return;
		}
		// And if we have just returned from a focus, then we need to reset the focus counter
		// and reassign an appropriate timeout to the timer...
		else if ( this.isFocusing )
		{
			this.isFocusing = false;
			if ( this.properties.isTimed() )
			{
				this.timer.setDelay( this.properties.getTimeBetweenNumbers() );
			}
			// Don't return, because we now want to select a number to display to the user...
		}
		
		// Otherwise we want to set up a new number...
		int targetNumber = -1;
		boolean makeTarget = false;
		int number;
		
		// Cannot create a target unless there is enough items in the history to go back that far.
		if ( history.size() > this.properties.getN() )
		{
			// Do not allow targets to span over a focus period (if enabled).
			// Also don't allow a target if it results in two of the same numbers in a row...
			if ( ( ! this.properties.requiresFocus() || this.numbersSinceLastFocus > this.properties.getN() ) 
				&& targetNumber != history.get( history.size() - 1 ) )
			{
				makeTarget = this.random.nextFloat() < this.properties.getTargetPercentage();
			}
			targetNumber = history.get( history.size() - this.properties.getN() );
		}
		
		if ( makeTarget )
		{
			number = targetNumber;
		}
		else
		{
			// We don't want to show a number which clashes with the previous one,
			// or which is a target...
			do 
			{
				number = (int)( ( this.random.nextFloat() * 10000 ) / 1000 );
			} while ( number == targetNumber || number == this.currentNumber );
		}
		
		this.currentIsTarget = makeTarget;
		this.currentNumber = number;
		this.timeNumberShown = System.currentTimeMillis();
		this.history.add( number );
		this.numbersSinceLastFocus ++;
		
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
