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

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Main extends JFrame implements KeyListener, ActionListener
{

	private JLabel nBackLabel, focusLabel;
	private NBack nback;
	private String userId;
	
	public Main( String userId )
	{
		this.userId = userId;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.CENTER ) );
		
		this.nBackLabel = new JLabel( "NBack - Click to begin" );
		this.nBackLabel.setFont( new Font( Font.SERIF, Font.BOLD, 50 ) );
		this.nBackLabel.setHorizontalAlignment( JLabel.CENTER );
		panel.add( this.nBackLabel );
		
		this.focusLabel = new JLabel( "" );
		this.focusLabel.setFont( new Font( Font.SANS_SERIF, Font.BOLD, 100 ) );
		this.focusLabel.setHorizontalAlignment( JLabel.CENTER );
		panel.add( this.focusLabel );
		
		this.addKeyListener( this );
		this.addMouseListener
		( 
			new MouseAdapter() 
			{
				public void mouseClicked( MouseEvent event )
				{
					nback.start();
				}
			}
		);
		this.setLayout( new GridBagLayout() );
		this.add( panel, new GridBagConstraints() );
		this.setSize( 800, 300 );
		this.setVisible( true );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
		
		nback = new NBack();
		nback.addActionListener( this );
	}
	
	public static void main( String[] args )
	{
		String userId;
		do
		{
			userId = JOptionPane.showInputDialog( "Please enter the participant code.", "A1" );
		} while ( userId.trim().length() == 0 );
		
		new Main( userId );
	}
	
	
	public void actionPerformed( ActionEvent e )
	{
		if ( e.getID() == NBack.ACTION_TICK )
		{
			this.nBackLabel.setText( this.nback.getCurrentNumber() + "" );
			this.focusLabel.setText( "" );
		}
		else if ( e.getID() == NBack.ACTION_FOCUS )
		{
			this.nBackLabel.setText( "" );
			this.focusLabel.setText( "+" );
		}
		else if ( e.getID() == NBack.ACTION_COMPLETE )
		{
			this.nback.saveResults( this.userId );
			System.exit( 1 );
		}
	}
	

	@Override
	public void keyPressed( KeyEvent e ) 
	{
		// If is target
		if ( e.getKeyCode() == KeyEvent.VK_ENTER )
		{
			this.nback.submitResult( true );
		}
		// If isn't target
		else if ( e.getKeyCode() == KeyEvent.VK_SPACE )
		{
			this.nback.submitResult( false );
		}
		else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
		{
			this.nback.saveResults( this.userId );
			System.exit( 1 );
		}
	}

	@Override
	public void keyReleased( KeyEvent e ) 
	{
		
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}

}
