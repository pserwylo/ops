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

public class InteractiveNBack extends AbstractNBack
{

	public InteractiveNBack( NBackProperties properties ) 
	{
		super( properties );
	}

	@Override
	public void submitResult( boolean isTarget, boolean wasForced ) 
	{
		// Forced results with an interactive n back are our cue to move on to
		// the next number. This allows us to keep time with whoever/whatever
		// is doing the clicking...
		if ( wasForced )
		{
			if ( ! this.hasResultForThisTime && ! this.numberSequence.isFocusing() )
			{
				// Forced results are NEVER correct, so always pass through
				// 'false' as the first param.
				this.addResult( false, true );
				hasResultForThisTime = true;
			}
			this.nextNumber();
		}
		else
		{
			if ( ! this.hasResultForThisTime && ! this.numberSequence.isFocusing() )
			{
				hasResultForThisTime = true;
				this.addResult( isTarget, false );
				// TODO: Depending on a particular, but as yet non existent property,
				// tick over to the next number if we submit a result.
				this.nextNumber();
			}
		}
	}
	
	private void nextNumber()
	{
		hasResultForThisTime = false;
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
