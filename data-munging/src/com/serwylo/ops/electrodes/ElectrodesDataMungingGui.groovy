package com.serwylo.ops.electrodes

import com.serwylo.ops.DataMungingGui
import com.serwylo.ops.Phase
import com.serwylo.ops.electrodes.phases.ConfirmWeirdElectrodes
import com.serwylo.ops.electrodes.phases.FindEmptyElectrodes
import com.serwylo.ops.electrodes.phases.FindWeirdElectrodes
import com.serwylo.ops.electrodes.phases.IdentifyElectrodes
import com.serwylo.ops.electrodes.phases.LoadData
import com.serwylo.ops.electrodes.phases.SpecifyElectrodesToAverage
import com.serwylo.ops.electrodes.phases.SpecifySuspectElectrodes

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
		new LoadData( model: model ),
		new IdentifyElectrodes( model: model ),
		new FindEmptyElectrodes( model: model ),
		new FindWeirdElectrodes( model: model ),
		new ConfirmWeirdElectrodes( model: model ),
		new SpecifySuspectElectrodes( model: model ),
		new SpecifyElectrodesToAverage( model: model ),
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
