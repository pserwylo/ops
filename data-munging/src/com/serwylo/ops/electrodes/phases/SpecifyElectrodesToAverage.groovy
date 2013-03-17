package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.Phase

/**
 * Show a GUI with an image of the electrode cap.
 * Highlight each dead electrode. For each dead one, ask them
 * to specify which to average.
 */
class SpecifyElectrodesToAverage extends ElectrodesPhase {

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

	}
	
}
