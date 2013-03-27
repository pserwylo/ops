package com.serwylo.ops.electrodes

import com.serwylo.ops.Phase
import com.serwylo.ops.electrodes.phases.*
import com.serwylo.ops.gui.DataMungingGui

/**
 * Automatically load spreadsheet
 * Automatically identify empty columns
 * Automatically search for unreasonably high values in columns
 * Manually confirm suspect columns
 * Manually specify suspect columns
 * Prompt graphic with highlighting of dead electrodes
 * For each dead electrode, collect:
 *  - Manually specify which other electrodes to average
 * For each dead electrode:
 *  - Enter "=AVERAGE( ... )"
 */
class ElectrodesDataMungingGui extends DataMungingGui {

	private Model model = new Model()

	private List<Phase> phaseList = [
		new LoadData( data: model ),
		new IdentifyElectrodes( data: model ),
		new FindEmptyElectrodes( data: model ),
		new SummariseElectrodes( data: model ),
		new SpecifySuspectElectrodes( data: model ),
		new SpecifyElectrodesToAverage( data: model ),
		new AverageElectrodes( data: model ),
	]

	@Override
	List<Phase> getPhases() {
		phaseList
	}

	@Override
	String getTitle() {
		"Electrodes data munging"
	}
}
