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

		String[][] formulas = new String[ endRow - startRow + 1 ][]

		double progressLeap = 100.0 / ( data.confirmedDeadColumns.size() )
		int oneHundredth    = ( endRow + 1 - startRow ) / 100

		data.confirmedDeadColumns.eachWithIndex { electrode, index ->

			List<String> toAverage = data.electrodesToAverage[ electrode ]

			double baseProgress = (double)index / ( data.confirmedDeadColumns.size() ) * 100
			dispatchProgressEvent( baseProgress, "Preparing averages for $electrode..." )

			double totalProgress = baseProgress

			for ( def row in startRow..endRow ) {

				if ( oneHundredth > 0 && row % oneHundredth == 0 ) {
					double miniProgress = ( row - startRow ) / ( endRow - startRow )
					double absoluteMini = miniProgress * progressLeap
					totalProgress       = baseProgress + absoluteMini
					dispatchProgressEvent( totalProgress, "Preparing averages for $electrode (${(int)(miniProgress * 100 )}%)..." )
				}

				String cellsToAverage = toAverage.collect {
					String colIndexName = ColumnUtils.indexToName( data.headers.electrodeLabels[ it ] )
					"$colIndexName$row"
				}.join( ";" )
				String formula = "=AVERAGE($cellsToAverage)"

				if ( !formulas[ row - startRow ] ) {
					formulas[ row - startRow ] = new String[ 1 ]
				}

				formulas[ row - startRow ][ 0 ] = formula
			}

			dispatchProgressEvent( totalProgress, "Sending averages of $electrode to spreadsheet..." )

			int colIndex = data.headers.electrodeLabels[ electrode ]
			String colLetter = ColumnUtils.indexToName( colIndex )
			String rangeAddress = "$colLetter$startRow:$colLetter$endRow"
			XCellRange range = data.document[ 0 ][ rangeAddress ]
			range.formulasFromArrays = formulas
		}
		return true
	}

}