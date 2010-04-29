package com.serwylo.pafbrain.nback.core;

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
 * An InteractiveNBack task takes its queues to tick over from when forced
 * results are submitted. This is, in our GUI's case, done via mouse clicks.
 * More importantly, it will in lab experiments, be done via an fMRI machine
 * simulating mouse clicks on the computer.
 */
public class InteractiveNBack extends AbstractNBack
{

	public InteractiveNBack( NBackProperties properties ) 
	{
		super( properties );
	}

	/**
	 * As soon as we start, fire up the first numer by calling the nextNumber()
	 * method.
	 */
	public void start()
	{
		if ( ! this.hasStarted )
		{
			this.nextNumber();
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
			this.nextNumber();
		}
		else
		{
			if ( ! this.hasResultForThisTick && ! this.numberSequence.isFocusing() )
			{
				hasResultForThisTick = true;
				this.addResult( isTarget, false );
				// TODO: Depending on a particular, but as yet non existent property,
				// tick over to the next number if we submit a result.
				this.nextNumber();
			}
		}
	}
	
	/**
	 * Simply sets a flag saying whether or not we have results for this tick,
	 * generates the next number, and then dispatches the appropriate action.
	 */
	private void nextNumber()
	{
		hasResultForThisTick = false;
		this.numberSequence.generateNextNumber();
		
		// When we are done, stop the timer, alert the listeners, and then return.
		if ( this.numberSequence.getHistory().size() >= this.properties.getTotalNumbers() )
		{
			this.isCompleted = true;
			this.fireAction( ACTION_COMPLETE );
		}
		else if ( this.numberSequence.isFocusing() )
		{
			// Make sure to wait for the appropriate amount of time...
			this.fireAction( ACTION_FOCUS );
		}
		else
		{
			// Set back to the original delay, because we just finished focusing...
			this.fireAction( ACTION_TICK );
		}
	}

}
