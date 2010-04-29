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

public class Result 
{

	public long time;
	public int timeFromStartOfTest;
	public int timeFromNumberShown;
	public int number;
	public boolean wasSuccessfull;
	public boolean wasTarget;
	public boolean timedOut;
	
	public Result( int number, boolean wasSuccessfull, boolean wasTarget, int timeFromStartOfTest, int timeFromNumberShown )
	{
		this( number, wasSuccessfull, wasTarget, false, timeFromStartOfTest, timeFromNumberShown );
	}
	
	public Result( int number, boolean wasSuccessfull, boolean wasTarget, boolean timedOut, int timeFromStartOfTest, int timeFromNumberShown )
	{
		this.time = System.currentTimeMillis();
		this.number = number;
		this.wasSuccessfull = wasSuccessfull;
		this.wasTarget = wasTarget;
		this.timedOut = timedOut;
		this.timeFromStartOfTest = timeFromStartOfTest;
		this.timeFromNumberShown = timeFromNumberShown;
	}
	
}
