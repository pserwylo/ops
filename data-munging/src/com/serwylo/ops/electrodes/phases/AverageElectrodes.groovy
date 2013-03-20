package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.electrodes.gui.ElectrodeSelector
import com.serwylo.uno.utils.ColumnUtils
import com.sun.star.table.XCellRange


/**
 *
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

		data.confirmedDeadColumns.each { electrode ->

			List<String> toAverage = data.electrodesToAverage[ electrode ]

			for ( def row in startRow..endRow ) {

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

			Integer colIndex = data.headers.electrodeLabels[ electrode ]
			String colLetter = ColumnUtils.indexToName( colIndex )
			String rangeAddress = "$colLetter$startRow:$colLetter$endRow"
			XCellRange range = data.document[ 0 ][ rangeAddress ]
			range.formulasFromArrays = formulas
		}
		return true
	}

}