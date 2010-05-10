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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * A TimedNBack task ticks over numbers at a constant rate.
 * This is stored in the NBackProperties class as the timeBetweenNumbers property.
 * If the time ticks over and the user has not submitted a result, then we submit
 * an incorrect forced result and keep on going.
 * @author Peter Serwylo
 */
public class TimedNBack extends AbstractNBack implements ActionListener
{

	private Timer timer;
	
	public TimedNBack( NBackProperties properties )
	{
		super( properties );
		this.timer = new Timer( properties.getTimeBetweenNumbers(), this );
	}
	
	/**
	 * Reset the timer and make it tick over straight away after adding a result.
	 * Does not accept results if we are focusing.
	 */
	public void submitResult( boolean isTarget, boolean wasForced )
	{
		if ( this.hasStarted && !this.numberSequence.isFocusing() )
		{
			// Don't ever allow forced results to be correct, so we take the opposite
			// of what we know will be correct if forced...
			boolean resultValue = wasForced ? ! this.numberSequence.isTarget() : isTarget;
			this.addResult( resultValue, wasForced );
			if ( ! this.hasResultForThisTick && ! wasForced )
			{
				this.hasResultForThisTick = true;
				this.timer.setInitialDelay( 0 );
				this.timer.restart();
			}
		}
	}
	
	/**
	 * Obviously starts the timer, but also notes down the time we started.
	 * That way, all results will have an idea of how far they are from the
	 * start of the test.
	 */
	public void start()
	{
		if ( !this.hasStarted )
		{
			this.hasStarted = true;
			this.timeStartedTest = System.currentTimeMillis();
	
			this.timer.setInitialDelay( 0 );
			this.timer.setRepeats( true );
			this.timer.start();
		}
	}

	/**
	 * Prevents the test from proceeding by stopping the timer.
	 * No more events will be dispatched once stopped.
	 */
	public void stop()
	{
		if ( this.hasStarted )
		{
			this.timer.stop();
		}
	}

	/**
	 * If we did not receive a result for the past tick, we will submit a 
	 * forced result now. We then we ask the numberSequence to generate the 
	 * next number in the sequence. If it is a focus tick, and the properties
	 * 'focusTick' and 'focusTime' are not set and set respectively, then we
	 * will change the delay in the timer to focusTime, to allow an arbitrary
	 * amount of focusing time.
	 */
	@Override
	public void actionPerformed( ActionEvent e ) 
	{		
		// If the user didn't submit an answer, then force an answer from them...
		if ( ! this.hasResultForThisTick && this.numberSequence.getHistory().size() > 0 )
		{
			this.submitResult( false, true );
		}
		else
		{
			hasResultForThisTick = false;
		}
		
		boolean wasFocusing = this.numberSequence.isFocusing();
		this.numberSequence.generateNextNumber();
		
		// When we are done, stop the timer, alert the listeners, and then return.
		if ( this.numberSequence.getHistory().size() >= this.properties.getTotalNumbers() )
		{
			this.stop();
			this.isCompleted = true;
			this.fireAction( ACTION_COMPLETE );
		}
		else if ( this.numberSequence.isFocusing() )
		{
			// Make sure to wait for the appropriate amount of time.
			// But only wait longer if we are not waiting for a certain amount 
			// of ticks. It is a completely arbitrary decision to make the ticks
			// override the timing, however it makes sense, because the Sequence
			// deals with ticks at a lower level than the TimedNBack task deals
			// with timing...
			if ( ! wasFocusing && this.properties.getFocusTicks() == 0 )
			{
				this.timer.setInitialDelay( this.properties.getFocusTime() );
				this.timer.restart();
			}
			this.fireAction( ACTION_FOCUS );
		}
		else
		{
			// Set back to the original delay, because we just finished focusing...
			if ( wasFocusing )
			{
				this.timer.setDelay( this.properties.getTimeBetweenNumbers() );
			}
			this.fireAction( ACTION_TICK );
		}
	}
	
}
