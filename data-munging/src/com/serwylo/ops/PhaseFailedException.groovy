package com.serwylo.ops

/**
 * Thrown when something caused the phase to fail.
 */
class PhaseFailedException extends Exception {

	Phase phase

	PhaseFailedException( Phase phase, String message, Throwable cause = null, boolean enableSuppression = false, boolean writeableStackTrace = false ) {
		super( message, cause, enableSuppression, writeableStackTrace )
		this.phase = phase
	}

}
