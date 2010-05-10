package com.serwylo.ops.nback.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

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

/**
 * An InteractiveTimedNBack task takes its queues to tick over from when forced
 * results are submitted. This is, in our GUI's case, done via mouse clicks. Once
 * the process is started, it will show a series of numbers, separated by a timer,
 * until a certain amount of clicks are received. Once this number is hit, we then
 * switch to focusing, finish focusing, then restart the timer to help prevent
 * drift in the reaction times.
 */
public class InteractiveTimedNBack extends AbstractNBack implements ActionListener, IInteractiveNBack
{

	private int segmentsPassed = 0;
	private boolean showingNumbers = true;
	private int ticksSinceLastSync = 0;
	private int numbersSinceLastSync = 0;
	private Timer timer;
	private int ticksSinceSegmentDone = 0;
	
	public InteractiveTimedNBack( NBackProperties properties ) 
	{
		super( properties );
		int timeBetweenNumbers = ( properties.getTicksPerSegment() * properties.getTimePerTick() ) / properties.getNumbersPerSegment();
		System.out.println( "Time between numbers = " + timeBetweenNumbers );
		this.timer = new Timer( timeBetweenNumbers, this );
	}

	/**
	 * As soon as we start, fire up the first numer by calling the nextNumber()
	 * method.
	 */
	public void start()
	{
		if ( ! this.hasStarted )
		{
			if ( this.properties.requiresFocus() && this.properties.startWithFocus() )
			{
				this.showingNumbers = false;
				this.numbersSinceLastSync = this.properties.getNumbersPerSegment();
			}
			else
			{
				this.timer.start();
			}
			super.start();
		}
	}
	
	@Override
	public void submitResult( boolean isTarget, boolean wasForced ) 
	{
		// Forced results with an interactive n back are our cue to move on to
		// the next number. This allows us to keep time with whoever/whatever
		// is doing the clicking...
		if ( wasForced )
		{
			if ( ! this.hasResultForThisTick && ! this.numberSequence.isFocusing() )
			{
				// Forced results are NEVER correct, so always pass through
				// 'false' as the first param.
				this.addResult( !this.numberSequence.isTarget(), true );
				hasResultForThisTick = true;
			}
			// this.nextNumber();
		}
		else
		{
			if ( ! this.hasResultForThisTick && ! this.numberSequence.isFocusing() )
			{
				hasResultForThisTick = true;
				this.addResult( isTarget, false );
				// TODO: Depending on a particular, but as yet non existent property,
				// tick over to the next number if we submit a result.
				// this.nextNumber();
			}
		}
	}
	
	@Override
	public void actionPerformed( ActionEvent e )
	{
		// Make sure that if we drift beyond the clicking, then we don't go 
		// beyond our allotted numbers...
		if ( this.numbersSinceLastSync < this.properties.getNumbersPerSegment() )
		{
			if ( !this.hasResultForThisTick )
			{
				this.submitResult( false, true );
			}
			
			this.hasResultForThisTick = false;
			this.numberSequence.generateNextNumber();
			
			// When we are done, stop the timer, alert the listeners, and then return.
			this.fireAction( ACTION_TICK );
			this.numbersSinceLastSync ++;
		}
	}

	@Override
	public void receiveClick() 
	{
		System.out.print( "c" );
		if ( this.showingNumbers && this.ticksSinceLastSync >= this.properties.getTicksPerSegment() )
		{
			this.segmentsPassed ++;
			if ( this.segmentsPassed >= this.properties.getTotalSegments() )
			{
				this.isCompleted = true;
				this.timer.stop();
				this.fireAction( ACTION_COMPLETE );
				return;
			}
			else
			{
				this.timer.stop();
				this.showingNumbers = false;
				this.ticksSinceLastSync = 0;
			}
		}
		
		if ( !this.showingNumbers )
		{
			// If we just started the focus block, make sure we are aactually focusing.
			// We do this by continually generating numbers until we hit a focus wall...
			if ( this.ticksSinceLastSync == 0 )
			{
				do 
				{
					this.numberSequence.generateNextNumber();
				} while ( !this.numberSequence.isFocusing() );
			}
			else
			{
				this.numberSequence.generateNextNumber();
			}
			
			if ( this.numberSequence.isFocusing() )
			{
				this.fireAction( ACTION_FOCUS );
			}
			else
			{
				this.fireAction( ACTION_TICK );
				this.showingNumbers = true;
				this.numbersSinceLastSync = 1;
				this.ticksSinceLastSync = 0;
				this.timer.start();
			}
		}
		
		this.ticksSinceLastSync ++;
	}

}
