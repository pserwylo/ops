package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.PhaseFailedException
import com.serwylo.uno.spreadsheet.CsvOptions
import groovy.swing.SwingBuilder

import javax.swing.JFileChooser

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

	@Override
	void execute() throws PhaseFailedException {

		if ( DEMO_FILE ) {
			CsvOptions options = new CsvOptions( fieldDelimiters: CsvOptions.TAB, hideFrame: false )
			data.load( new File( DEMO_FILE ), options )
			return
		}

		JFileChooser fc = new SwingBuilder().fileChooser(
			dialogTitle: "Load electrodes data file",
			fileSelectionMode: JFileChooser.FILES_ONLY,
			fileFilter: new CsvFileFilter(),
		)

		if ( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			CsvOptions options = new CsvOptions( fieldDelimiters: CsvOptions.TAB, hideFrame : false )
			data.load( fc.selectedFile, options )
		} else {
			throw new PhaseFailedException( this, "You must select a data file to load." )
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
