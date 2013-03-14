package com.serwylo.ops.nback.core;

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

import java.util.ArrayList;
import java.util.Random;

/**
 * The sequence deals with the generation of numbers, deciding when to display
 * a target, and deciding when to show a focus sign. This is all done inside 
 * the generateNextNumber() function, while the current state (whether a target
 * or a focus thing is being shown, what number is being shown, etc) is available
 * through the accessor functions.
 * @author Peter Serwylo
 */
public class Sequence 
{
	
	private ArrayList<Integer> history = new ArrayList<Integer>();
	private NBackProperties properties;
	private Random random;
	
	private int currentNumber;
	private boolean currentIsTarget;
	private int numbersSinceLastFocus;
	private int currentFocusDuration;
	private boolean isFocusing;
	private long timeNumberShown;

	public boolean isTarget() { return this.currentIsTarget; }
	public int getCurrentNumber() { return this.currentNumber; }
	public boolean isFocusing() { return this.isFocusing; }
	public ArrayList<Integer> getHistory() { return this.history; }
	public long getTimeNumberShown() { return this.timeNumberShown; }
	
	public Sequence( NBackProperties properties )
	{
		this.properties = properties;
		this.random = new Random( this.properties.getRandomSeed() );
		
		// This is a simple way to start off focussing if required...
		if ( this.properties.requiresFocus() && this.properties.startWithFocus() )
		{
			this.numbersSinceLastFocus = this.properties.getNumbersBetweenFocus();
		}
	}
	
	public int generateNextNumber()
	{
		// If we hit the amount of numbers that we need to focus, then we do that...
		if ( this.properties.requiresFocus() 
			&& ! this.isFocusing
			&& this.numbersSinceLastFocus == this.properties.getNumbersBetweenFocus() )
		{
			this.isFocusing = true;
			this.currentFocusDuration = 1;

			this.numbersSinceLastFocus = 0;
			System.out.print( " +" );
			return -1;
		}
		// If in manual mode, then make sure we allow a number of ticks before stopping focusing..
		else if ( this.isFocusing && this.properties.getFocusTicks() > 0 && this.currentFocusDuration < this.properties.getFocusTicks() )
		{
			this.currentFocusDuration ++;
			System.out.print( "+" );
			return -1;
		}
		// And if we have just returned from a focus, then we need to reset the focus counter
		// and reassign an appropriate timeout to the timer...
		else if ( this.isFocusing )
		{
			this.isFocusing = false;
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
		this.history.add( number );
		this.numbersSinceLastFocus ++;
		this.timeNumberShown = System.currentTimeMillis();
		
		System.out.print( " " + number + ( makeTarget ? "T" : "" ) );
		return this.currentNumber;
	}
	
}
