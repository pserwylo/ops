package com.serwylo.ops.electrodes.phases.identifyingElectrodes

class NonFatalTokenParserException extends Exception {

	TokenParser parser

	public NonFatalTokenParserException( TokenParser parser, String message, Throwable cause = null, boolean enableSuppression = false, boolean writeableStackTrace = false ) {
		super( message, cause, enableSuppression, writeableStackTrace )
		this.parser = parser
	}

}
