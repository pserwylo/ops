package com.serwylo.ops.electrodes.phases.identifyingElectrodes

class TokenParserException extends Exception {

	TokenParser parser

	public TokenParserException( TokenParser parser, String message, Throwable cause = null, boolean enableSuppression = false, boolean writeableStackTrace = false ) {
		super( message, cause, enableSuppression, writeableStackTrace )
		this.parser = parser
	}

}
