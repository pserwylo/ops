package com.serwylo.ops.electrodes

import com.serwylo.ops.Converter
import com.serwylo.uno.spreadsheet.CsvOptions
import com.serwylo.uno.spreadsheet.SpreadsheetConnector
import com.serwylo.uno.utils.ColumnUtils
import com.serwylo.uno.utils.DataUtils
import com.sun.star.sheet.XSpreadsheetDocument
import com.sun.star.table.XCellRange

class ElectrodesConverter extends Converter {

	private SpreadsheetConnector connector = new SpreadsheetConnector()

	@Override
	void convert() {

		XSpreadsheetDocument doc = connector.open( "/home/pete/Documents/people/felicia/data.txt", new CsvOptions( startLine: 14, fieldDelimiters: CsvOptions.TAB, hideFrame: false ) )

		def numRange = 0..191060
		def columns = doc[ 0 ].columns
		for ( int i = 0; i < columns.size(); i ++ ) {

			XCellRange column = columns[ i ]

			println "Checking column $i (aka ${ColumnUtils.indexToName( i )})"
			println "Length: $column.address.EndRow (expected 191060)"
			if ( column.formula ) {
				println "Skipping (first value is $column.formula)"
			} else {
				println "Fixing (first value is empty)"

				List<String> columnsToAverage = []

				if ( i > 0 ) {
					columnsToAverage.add( ColumnUtils.indexToName( i - 1 ) )
				}

				if ( i < columns.size() - 1 ) {
					columnsToAverage.add( ColumnUtils.indexToName( i + 1 ) )
				}

				println "Averaging column(s) " + columnsToAverage.join( " and " )

				List<List<String>> values = numRange.collect { row ->
					List<String> cells = columnsToAverage.collect { col -> col + row }
					[ "=AVERAGE( ${cells.join( "," )})" ]
				}
				println "Sending ${values.size()} values to the spreadsheet..."
				column.formulas = values
				println "Sending complete"
			}


		}

	}
}
