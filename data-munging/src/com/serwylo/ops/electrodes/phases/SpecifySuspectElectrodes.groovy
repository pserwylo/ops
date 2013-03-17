package com.serwylo.ops.electrodes.phases

/**
 *
 */
class SpecifySuspectElectrodes extends ElectrodesPhase {

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
	void execute() {

		Thread.sleep( 500 )

	}
	
}
