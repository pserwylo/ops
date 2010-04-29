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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Reads and saves a set of properties to do with the NBack test from/to a file.
 * @author Peter Serwylo
 */
public class NBackProperties 
{

	public static final int DEFAULT_N = 3;
	public static final int DEFAULT_TOTAL_NUMBERS = 20;
	public static final int DEFAULT_TOTAL_TIME = 0;
	public static final int DEFAULT_TIME_BETWEEN_NUMBERS = 0;
	public static final int DEFAULT_NUMBERS_BETWEEN_FOCUS = 0;
	public static final int DEFAULT_FOCUS_TIME = 0;
	public static final float DEFAULT_TARGET_PERCENTAGE = 0.1f;
	public static final long DEFAULT_RANDOM_SEED = System.currentTimeMillis();
	public static final String DEFAULT_SAVE_DIRECTORY = System.getProperty( "user.home" );
	public static final int DEFAULT_FOCUS_TICKS = 0;
	
	private int propN, propTotalNumbers, propTotalTime, propTimeBetweenNumbers, 
		propNumbersBetweenFocus, propFocusTime, propFocusTicks;
	private float propTargetPercentage;
	private long propRandomSeed;
	private String propSaveDirectory;

	public int getN() { return this.propN; }
	public int getTotalNumbers() { return this.propTotalNumbers; }
	public int getTotalTime() { return this.propTotalTime; }
	public int getTimeBetweenNumbers() { return this.propTimeBetweenNumbers; }
	public int getNumbersBetweenFocus() { return this.propNumbersBetweenFocus; }
	public int getFocusTime() { return this.propFocusTime; }
	public float getTargetPercentage() { return this.propTargetPercentage; }
	public long getRandomSeed() { return this.propRandomSeed; }
	public String getSaveDirectory() { return this.propSaveDirectory; }
	public int getFocusTicks() { return this.propFocusTicks; }
	
	
	public boolean isTimed() { return this.propTimeBetweenNumbers > 0; }
	
	public boolean requiresFocus() 
	{ 
		return ( this.propFocusTime > 0 || this.propFocusTicks > 0 ) && this.propNumbersBetweenFocus > 0; 
	}


	/**
	 * Check the nback.properties file and assign the properties from it to the appropriate member variables.
	 */
	public void read()
	{
		Properties properties = new Properties();
		try
		{
			properties.load( new FileReader( "nback.properties" ) );
		}
		catch( IOException ioe )
		{
			JOptionPane.showMessageDialog( null, "Error reading configuration file 'nback.properties': \n" + ioe.getMessage() );
		}

		String n = properties.getProperty( "n" );
		String totalNumbers = properties.getProperty( "totalNumbers" );
		String totalTime = properties.getProperty( "totalTime" );
		String targetPercentage = properties.getProperty( "targetPercentage" );
		String timeBetweenNumbers = properties.getProperty( "timeBetweenNumbers" );
		String randomSeed = properties.getProperty( "randomSeed" );
		String saveDirectory = properties.getProperty( "saveDirectory" );
		String numbersBetweenFocus = properties.getProperty( "numbersBetweenFocus" );
		String focusTime = properties.getProperty( "focusTime" );
		String focusTicks = properties.getProperty( "focusTicks" );

		this.propN = ( n != null ) ? Integer.parseInt( n ) : DEFAULT_N;
		this.propTotalNumbers = ( totalNumbers != null ) ? Integer.parseInt( totalNumbers ) : DEFAULT_TOTAL_NUMBERS;
		this.propTotalTime = ( totalTime != null ) ? Integer.parseInt( properties.getProperty( "totalTime" ) ) : DEFAULT_TOTAL_TIME;
		this.propTargetPercentage = ( targetPercentage != null ) ? ( Float.parseFloat( targetPercentage ) / 100 ) : DEFAULT_TARGET_PERCENTAGE;
		this.propTimeBetweenNumbers = ( timeBetweenNumbers != null ) ? Integer.parseInt( timeBetweenNumbers ) : DEFAULT_TIME_BETWEEN_NUMBERS;
		this.propRandomSeed = ( randomSeed != null ) ? Integer.parseInt( randomSeed ) : DEFAULT_RANDOM_SEED;
		this.propSaveDirectory = ( saveDirectory != null ) ? saveDirectory : DEFAULT_SAVE_DIRECTORY;
		this.propNumbersBetweenFocus = ( numbersBetweenFocus != null ) ? Integer.parseInt( numbersBetweenFocus ) : DEFAULT_NUMBERS_BETWEEN_FOCUS;
		this.propFocusTime = ( focusTime != null ) ? Integer.parseInt( focusTime ) : DEFAULT_FOCUS_TIME;
		this.propFocusTicks = ( focusTicks != null ) ? Integer.parseInt( focusTicks ) : DEFAULT_FOCUS_TICKS;
		
		File saveDirectoryFile = new File( this.propSaveDirectory );
		if ( !saveDirectoryFile.exists() || !saveDirectoryFile.isDirectory() )
		{
			JOptionPane.showMessageDialog( null, "Cannot find directory at '" + saveDirectory + "'.\n\nThe task will continue, but results will not be saved." );
		}
		
		if ( this.propTotalTime > 0 )
		{
			this.propTotalNumbers = this.propTotalTime * 1000 / this.propTimeBetweenNumbers;
		}

		System.out.println( "Loaded Properties:" );
		System.out.println( "  n = " + this.propN );
		System.out.println( "  totalNumbers = " + this.propTotalNumbers );
		System.out.println( "  targetPercentage = " + this.propTargetPercentage );
		System.out.println( "  timeBetweenNumbers = " + this.propTimeBetweenNumbers );
		System.out.println( "  timeRandomSeed = " + this.propRandomSeed );
		System.out.println( "  timeSaveDirectory = " + this.propSaveDirectory );
		System.out.println( "  timeNumbersBetweenFocus = " + this.propNumbersBetweenFocus );
		System.out.println( "  timeFocusTime = " + this.propFocusTime );
		
	}
	
}
