package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.Phase

class PromptSuspectColumns extends ElectrodesPhase {

	@Override
	boolean requiresUserInteraction() {
		true
	}

	@Override
	String getName() {
		return "Enter suspect electrodes"
	}

	@Override
	void execute() {

	}
	
}
