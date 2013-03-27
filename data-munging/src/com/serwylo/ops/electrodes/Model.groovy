package com.serwylo.ops.electrodes

import com.serwylo.ops.OpsPreferences
import com.serwylo.ops.electrodes.phases.identifyingElectrodes.Headers
import com.serwylo.uno.spreadsheet.CsvOptions
import com.serwylo.uno.spreadsheet.SpreadsheetConnector
import com.sun.star.comp.helper.BootstrapException
import com.sun.star.sheet.XSpreadsheetDocument

class Model {

	private static final String FILE_SUFFIX      = "-avg"

	SpreadsheetConnector connector
	XSpreadsheetDocument document                = null
	CsvOptions options                           = null
	File file                                    = null
	Headers headers                              = null
	List<String> deadColumns                     = []
	List<String> weirdColumns                    = []
	List<String> confirmedDeadColumns            = []
	Map<String,List<String>> electrodesToAverage = [:]
	Map<String, Double> maxValues                = [:]
	Map<String, Double> minValues                = [:]

	void load( File file, CsvOptions options ) throws BootstrapException {
		this.file    = file
		this.options = options
		connector    = new SpreadsheetConnector( OpsPreferences.instance.officePath )
		document     = connector.open( file.absolutePath, options )
	}

	void save() {
		int dotIndex     = file.name.lastIndexOf( '.' )
		String name      = file.name.substring( 0, dotIndex )
		String extension = file.name.substring( dotIndex )
		String newName   = "$name$FILE_SUFFIX$extension"
		connector.save( document, new File( file.parent, newName ).absolutePath )
	}


}
