package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.Phase
import com.serwylo.ops.electrodes.gui.ElectrodeSelector

import javax.swing.JComponent
import java.awt.Dimension

/**
 * Show a GUI with an image of the electrode cap.
 * Highlight each dead electrode. For each dead one, ask them
 * to specify which to average.
 *
 * TODO: Make this an aggregate phase, which has several phases itself which are only created at run-time.
 */
class SpecifyElectrodesToAverage extends ElectrodesPhase {

	ElectrodeSelector selector
	String currentElectrode

	@Override
	boolean requiresUserInteraction() {
		true
	}

	@Override
	String getName() {
		"Choose electrodes to average"
	}

	@Override
	String getDescription() {
		"When we have an idea about which electrodes are dead, you will be asked to fix them. " +
		"This involves manually selecting the electrodes which you would like to average to get a value for the dead one."
	}

	@Override
	void execute() {
		data.electrodesToAverage[ currentElectrode ] = selector.selectedElectrodes
		println "For $currentElectrode, average $selector.selectedElectrodes"
	}

	JComponent getGui() {
		currentElectrode        = data.confirmedDeadColumns[ 0 ]
		selector                = new ElectrodeSelector( data.headers.electrodeLabels )
		selector.deadElectrodes = [ currentElectrode ]
		selector.minimumSize    = new Dimension( 600, 400 )
		selector.preferredSize  = new Dimension( 600, 400 )
		selector
	}
}
