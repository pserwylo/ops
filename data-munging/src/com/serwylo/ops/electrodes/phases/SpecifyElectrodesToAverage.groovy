package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.electrodes.gui.ElectrodeSelector

import javax.swing.*
import java.awt.*

/**
 * Show a GUI with an image of the electrode cap.
 * Highlight each dead electrode. For each dead one, ask them
 * to specify which to average.
 *
 * TODO: Make this an aggregate phase, which has several phases itself which are only created at run-time.
 */
class SpecifyElectrodesToAverage extends ElectrodesPhase {

	private double index = 0 // The node we are up to, for progress purposes...
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
	boolean execute() {
		data.electrodesToAverage[ currentElectrode ] = selector.selectedElectrodes
		return nextElectrode() == null
	}

	JComponent getGui() {
		selector                 = new ElectrodeSelector( data.headers.electrodeLabels )
		selector.minimumSize     = new Dimension( 600, 400 )
		selector.preferredSize   = new Dimension( 600, 400 )
		nextElectrode()
		selector
	}

	private String nextElectrode() {
		if ( !currentElectrode ) {
			if ( data.confirmedDeadColumns.size() > 0 ) {
				currentElectrode = data.confirmedDeadColumns[ 0 ]
			}
		} else {
			int index = data.confirmedDeadColumns.indexOf( currentElectrode )
			if ( index == -1 || index >= data.confirmedDeadColumns.size() - 1 ) {
				currentElectrode = null
			} else {
				currentElectrode = data.confirmedDeadColumns[ index + 1 ]
			}
		}

		if ( currentElectrode ) {
			selector.resetAll()
			selector.deadElectrodes         = data.confirmedDeadColumns - [ currentElectrode ]
			selector.weirdElectrodes        = [ currentElectrode ]
			selector.unselectableElectrodes = data.confirmedDeadColumns
			selector.selectedElectrodes     = selector.calcNearestElectrodes( currentElectrode, 8 )

			dispatchProgressEvent( index / data.confirmedDeadColumns.size() * 100, "Selecting electrodes to average for $currentElectrode..." )

			index ++
		}

		selector.repaint()
		currentElectrode
	}
}
