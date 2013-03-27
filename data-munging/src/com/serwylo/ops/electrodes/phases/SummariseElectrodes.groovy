package com.serwylo.ops.electrodes.phases

import com.serwylo.uno.spreadsheet.Calculator
import com.sun.star.table.XCellRange

/**
 *
 */
class SummariseElectrodes extends ElectrodesPhase {

	Double lowerThreshold = -30
	Double upperThreshold = 50

	@Override
	boolean requiresUserInteraction() {
		false
	}

	@Override
	String getName() {
		"Summarise electrodes"
	}

	@Override
	String getDescription() {
		"Suspect electrodes have values that are far above or below the usual value." +
		"These are not guaranteed to be stuffy, but we will ask you to confirm when we've found them anyway."
	}

	@Override
	boolean execute() {

		Calculator calc        = new Calculator( data.document, 0 )
		XCellRange valueRange  = data.document[ 0 ].getCellRangeByPosition( 0, data.headers.startRow, data.headers.electrodeLabels.size() - 1, 1000000 )

		dispatchProgressEvent( 33, "Calculating maximum value for each column..." )
		List<Double> maxValues = calc.maxForEachColumn( valueRange )

		dispatchProgressEvent( 66, "Calculating minimum value for each column..." )
		List<Double> minValues = calc.minForEachColumn( valueRange )

		// TODO: Std-dev and mean?

		data.headers.electrodeLabels.each { entry ->
			String label = entry.key
			int    index = entry.value
			data.maxValues.put( label, maxValues[ index ] )
			data.minValues.put( label, minValues[ index ] )
		}

		true
	}
	
}
