package com.serwylo.pafbrain.nback.gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PreferencesDialog extends JDialog
{

	public PreferencesDialog()
	{
		FormLayout layout = new FormLayout(
			"pref, 4dlu, 50dlu, 4dlu, min", // columns
			"pref, 2dlu, pref, 2dlu, pref"); // rows
		
		layout.setRowGroups( new int[][] { { 1, 2, 3 } } );
		
		JPanel panel = new JPanel( layout );
		CellConstraints cc = new CellConstraints();
		
		panel.add( new JLabel( "Label1" ), cc.xy( 1, 1 ) );
		panel.add( new JTextField(), cc.xyw( 2, 1, 2 ) );
		panel.add( new JLabel( "Label2" ), cc.xy (1, 2) );
		panel.add( new JTextField(), cc.xy( 2, 2 ) );
		panel.add( new JLabel( "Label3" ), cc.xy( 1, 2 ) );
		panel.add( new JTextField(), cc.xy( 2, 3 ) );
		panel.add( new JButton("Bleh"), cc.xy( 3, 3 ) );

		this.add( panel );
		this.setSize( 400, 300 );
		this.setVisible( true );
	}
	
	public static void main( String[] args )
	{
		new PreferencesDialog();
	}
	
}
