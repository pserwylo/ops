package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.electrodes.gui.ElectrodeSelector
import com.serwylo.uno.utils.ColumnUtils
import com.sun.star.table.XCellRange


/**
 * TODO: Just add one cell, then ask libreoffice to calculate the needed by dragging the cell down.
 */
class AverageElectrodes extends ElectrodesPhase {

	@Override
	boolean requiresUserInteraction() {
		false
	}

	@Override
	String getName() {
		"Average electrodes"
	}

	@Override
	String getDescription() {
		"Fill the dead columns with averages of other columns you specified."
	}

	@Override
	boolean execute() {

		int startRow = data.headers.startRow + 1
		int endRow   = data.document[ 0 ].dimensions.Row + 1

		data.confirmedDeadColumns.eachWithIndex { electrode, index ->
			progress( index )
			average( electrode, startRow, endRow )
		}
		return true
	}

	private void average( String electrode, int startRow, int endRow ) {
		List<String> toAverage = data.electrodesToAverage[ electrode ]
		String formula         = generateFormula( toAverage, startRow )

		int colIndex           = data.headers.electrodeLabels[ electrode ]
		String colLetter       = ColumnUtils.indexToName( colIndex )
		String rangeAddress    = "$colLetter$startRow:$colLetter$endRow"
		XCellRange range       = data.document[ 0 ][ rangeAddress ]

		range.getCellByPosition( 0, 0 ).formula = formula
		range.fillDown()
	}

	private void progress( int colNumber ) {
		double progress  = (double)colNumber / ( data.confirmedDeadColumns.size() ) * 100
		String electrode = data.confirmedDeadColumns[ colNumber ]
		dispatchProgressEvent( progress, "Averaging $electrode..." )
	}

	private String generateFormula( List<String> toAverage, int startRow ) {

		String cellsToAverage = toAverage.collect {
			String colIndexName = ColumnUtils.indexToName( data.headers.electrodeLabels[ it ] )
			"$colIndexName$startRow"
		}.join( ";" )

		"=AVERAGE($cellsToAverage)"
	}

}