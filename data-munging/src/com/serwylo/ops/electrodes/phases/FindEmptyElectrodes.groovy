package com.serwylo.ops.electrodes.phases

import com.serwylo.uno.utils.ColumnUtils
import com.sun.star.table.XCellRange

class FindEmptyElectrodes extends ElectrodesPhase {

	@Override
	boolean requiresUserInteraction() {
		false
	}

	@Override
	String getName() {
		"Find dead electrodes"
	}

	@Override
	String getDescription() {
		"Dead electrodes are those which don't have any data at all. " +
		"That is, those who have a column in the spreadsheet that is completely empty."
	}

	@Override
	void execute() {
		List<Object> firstRow = getFirstRow()
		data.headers.electrodeLabels.each { entry ->

			Integer index  = entry.value
			String colName = entry.key
			assert( firstRow.size() > index )

			if ( isEmpty( firstRow[ index ] ) ) {
				markAsDead( colName )
			}
		}

	}

	private void markAsDead( String colName ) {
		data.deadColumns.add( colName )
		// TODO: Highlight columns red (or at least the header label)
	}

	private List<Object> getFirstRow() {
		String colName = ColumnUtils.indexToName( data.headers.electrodeLabels.size() - 1 )
		int rowIndex   = data.headers.startRow + 1
		XCellRange range = data.document[ 0 ][ "A$rowIndex:$colName$rowIndex" ]

		range.cellRangeData.dataArray[ 0 ]
	}

	/**
	 * If we ask for the
	 * @param data
	 * @return
	 */
	private boolean isEmpty( Object data ) {
		( data instanceof String )
	}
	
}
