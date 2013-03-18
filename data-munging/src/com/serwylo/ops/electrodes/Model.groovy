package com.serwylo.ops.electrodes

import com.serwylo.ops.electrodes.phases.identifyingElectrodes.Headers
import com.serwylo.uno.spreadsheet.CsvOptions
import com.serwylo.uno.spreadsheet.SpreadsheetConnector
import com.sun.star.sheet.XSpreadsheetDocument

class Model {

	SpreadsheetConnector connector
	XSpreadsheetDocument document                = null
	CsvOptions options                           = null
	File file                                    = null
	Headers headers                              = null
	List<String> deadColumns                     = []
	List<String> weirdColumns                    = []
	List<String> confirmedDeadColumns            = []
	List<String> manualDeadColumns               = []
	Map<String,List<String>> electrodesToAverage = [:]

	void load( File file, CsvOptions options ) {
		this.file    = file
		this.options = options
		connector    = new SpreadsheetConnector()
		print "Loading document $file.absolutePath..."
		document     = connector.open( file.absolutePath, options )
		println "Done!"
	}


}
