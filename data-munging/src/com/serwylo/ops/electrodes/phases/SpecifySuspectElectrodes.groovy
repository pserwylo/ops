package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.PhaseFailedException
import com.serwylo.ops.electrodes.gui.ElectrodeSelector

import javax.swing.JComponent
import java.awt.Dimension

/**
 *
 */
class SpecifySuspectElectrodes extends ElectrodesPhase {

	ElectrodeSelector selector

	@Override
	boolean requiresUserInteraction() {
		true
	}

	@Override
	String getName() {
		"Ask for suspect electrodes"
	}

	@Override
	String getDescription() {
		"If you kept a diary while you were collecting data, you may have noticed electrodes with weird values. " +
		"In this case, you have the option to specify electrodes that you don't trust."
	}

	@Override
	boolean execute() {
		data.confirmedDeadColumns = selector.selectedElectrodes
		return true
	}

	JComponent getGui() {
		try {
			selector = new ElectrodeSelector( data.headers.electrodeLabels )
			selector.deadElectrodes          = data.deadColumns
			selector.weirdElectrodes         = data.weirdColumns
			selector.forceSelectedElectrodes = data.deadColumns
			// selector.selectedElectrodes      = data.weirdColumns + data.deadColumns
			selector.minimumSize             = new Dimension( 600, 400 )
			selector.preferredSize           = new Dimension( 600, 400 )
			selector
		} catch ( Exception e ) {
			throw new PhaseFailedException( this, "Error specifying suspect electrodes: $e.message", e )
		}
	}
	
}