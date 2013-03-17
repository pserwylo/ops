package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.Phase
import groovy.swing.SwingBuilder

import javax.swing.JComponent
import javax.swing.JPanel

/**
 *
 */
class ConfirmWeirdElectrodes extends ElectrodesPhase {


	@Override
	boolean requiresUserInteraction() {
		true
	}

	@Override
	String getName() {
		"Confirm suspect electrodes"
	}

	@Override
	void execute() {



	}

	SwingBuilder getGui() {
		new SwingBuilder().edt {
			panel() {

			}
		}
	}

	private List<JPanel> getWidgets() {
		model.weirdColumns.each { colIndex ->
			String colName = model.headers.electrodeLabels.find { }


		}
	}
	
}
