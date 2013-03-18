package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.PhaseFailedException
import com.serwylo.ops.electrodes.phases.identifyingElectrodes.Headers
import com.sun.star.sheet.XCellRangeFormula
import com.sun.star.table.XCellRange

class IdentifyElectrodes extends ElectrodesPhase {

	@Override
	String getName() {
		"Identify electrodes"
	}

	@Override
	boolean requiresUserInteraction() {
		false
	}

	@Override
	String getDescription() {
		"Figures out which columns in the data file correspond to which electrodes."
	}

	@Override
	void execute() throws PhaseFailedException {
		XCellRange header = data.document[ 0 ][ "A1:ZZ20" ]
		XCellRangeFormula formulas = header.cellRangeFormula
		Headers values = new Headers()
		values.parse( formulas.formulaArray )
		data.headers = values
	}

}
