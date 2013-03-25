package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.PhaseFailedException
import com.serwylo.uno.spreadsheet.CsvOptions
import com.sun.star.comp.helper.BootstrapException
import groovy.swing.SwingBuilder

import javax.swing.JFileChooser
import javax.swing.JOptionPane

class LoadData extends ElectrodesPhase {

	private static final String DEMO_FILE = "/home/pete/Documents/people/felicia/data-short.csv"

	@Override
	boolean requiresUserInteraction() {
		false
	}

	@Override
	String getName() {
		return "Load data file"
	}

	String errorMessage( BootstrapException e ) {
		while ( e.targetException != null && e.targetException != e ) {
			e = e.targetException
		}
		e.message
	}

	@Override
	boolean execute() throws PhaseFailedException {

		if ( DEMO_FILE ) {
			CsvOptions options = new CsvOptions( fieldDelimiters: CsvOptions.TAB, hideFrame: false )
			dispatchIndeterminateProgressEvent( "Loading file '$DEMO_FILE'..." )

			try {
				data.load( new File( DEMO_FILE ), options )
			} catch ( BootstrapException e ) {
				throw new PhaseFailedException( this, "Error connecting to spreadsheet application.\n\n(${errorMessage( e )}" )
			}

			return true
		}

		dispatchProgressEvent( 0, "Choosing data file to load..." )

		JFileChooser fc = new SwingBuilder().fileChooser(
			dialogTitle: "Load electrodes data file",
			fileSelectionMode: JFileChooser.FILES_ONLY,
			fileFilter: new CsvFileFilter(),
		)

		if ( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			CsvOptions options = new CsvOptions( fieldDelimiters: CsvOptions.TAB, hideFrame : false )
			dispatchIndeterminateProgressEvent( "Loading file '$fc.selectedFile'..." )
			try {
				data.load( fc.selectedFile, options )
			} catch ( BootstrapException e ) {
				throw new PhaseFailedException( this, "Error connecting to spreadsheet application.\n\n(${errorMessage( e )}" )
			}
			return true
		} else {
			return false
		}

	}

	class CsvFileFilter extends javax.swing.filechooser.FileFilter {

		@Override
		String getDescription() {
			"EEG ASCII file (*.csv)"
		}

		@Override
		boolean accept(File file) {
			file.isDirectory() || file.path.toLowerCase().endsWith( "*.csv" )
		}
	}

}
