package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.electrodes.ElectrodesPreferences
import com.serwylo.ops.electrodes.gui.WeirdElectrodeSelector

import javax.swing.*
import java.awt.*

/**
 *
 */
class SpecifySuspectElectrodes extends ElectrodesPhase {

	WeirdElectrodeSelector selector

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
		ElectrodesPreferences.weirdMin = selector.minValue
		ElectrodesPreferences.weirdMax = selector.maxValue
		return true
	}

	JComponent getGui() {
		selector = new WeirdElectrodeSelector( data )
		selector.deadElectrodes          = data.deadColumns
		selector.forceSelectedElectrodes = data.deadColumns
		selector.minimumSize             = new Dimension( 600, 400 )
		selector.preferredSize           = new Dimension( 600, 400 )
		if ( ElectrodesPreferences.hasWeirdMax() && ElectrodesPreferences.hasWeirdMin() ) {
			selector.minValue = ElectrodesPreferences.weirdMin
			selector.maxValue = ElectrodesPreferences.weirdMax
		}
		selector
	}
	
}