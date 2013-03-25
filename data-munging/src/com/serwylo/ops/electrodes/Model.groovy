package com.serwylo.ops.electrodes

import com.serwylo.ops.OpsPreferences
import com.serwylo.ops.electrodes.phases.identifyingElectrodes.Headers
import com.serwylo.uno.spreadsheet.CsvOptions
import com.serwylo.uno.spreadsheet.SpreadsheetConnector
import com.sun.star.comp.helper.BootstrapException
import com.sun.star.sheet.XSpreadsheetDocument
import ooo.connector.server.OfficePath

class Model {

	SpreadsheetConnector connector
	XSpreadsheetDocument document                = null
	CsvOptions options                           = null
	File file                                    = null
	Headers headers                              = null
	List<String> deadColumns                     = []
	List<String> weirdColumns                    = []
	List<String> confirmedDeadColumns            = []
	Map<String,List<String>> electrodesToAverage = [:]

	void load( File file, CsvOptions options ) throws BootstrapException {
		this.file    = file
		this.options = options
		connector    = new SpreadsheetConnector( OpsPreferences.instance.officePath )
		document     = connector.open( file.absolutePath, options )
	}


}
