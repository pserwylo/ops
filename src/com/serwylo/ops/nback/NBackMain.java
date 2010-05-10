package com.serwylo.ops.nback;

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

import com.serwylo.ops.nback.core.AbstractNBack;
import com.serwylo.ops.nback.core.InteractiveNBack;
import com.serwylo.ops.nback.core.InteractiveTimedNBack;
import com.serwylo.ops.nback.core.NBackProperties;
import com.serwylo.ops.nback.core.TimedNBack;
import com.serwylo.ops.nback.gui.NBackGui;

import javax.swing.JOptionPane;

/**
 * Prompts the user for their participant ID (will be used to save the file
 * to once done). Reads the properties, creates an appropriate n back task,
 * and passes it to the NBackGUI.
 * @author Peter Serwylo
 */
public class NBackMain
{

	public static void main( String[] args )
	{
		String userId;
		do
		{
			userId = JOptionPane.showInputDialog( "Please enter the participant code.", "A1" );
		} while ( userId.trim().length() == 0 );
		
		NBackProperties properties = new NBackProperties();
		
		try
		{
			properties.read();
		}
		catch ( Exception e )
		{
			JOptionPane.showMessageDialog( null, "Error reading properties file:\n" + e.getMessage() + "\nWill attempt to continue anyway." );
		}
		
		AbstractNBack nback;
		if ( properties.isInteractiveTimed() )
		{
			nback = new InteractiveTimedNBack( properties );
			System.out.println( "Creating Interactive/Timed Task" );
		}
		else if ( properties.isTimed() )
		{
			nback = new TimedNBack( properties );
			System.out.println( "Creating Timed Task" );
		}
		else
		{
			nback = new InteractiveNBack( properties );
			System.out.println( "Interactive Task" );
		}
		new NBackGui( nback, userId );
	}
	
}
