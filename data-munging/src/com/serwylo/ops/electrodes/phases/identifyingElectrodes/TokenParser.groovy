package com.serwylo.ops.electrodes.phases.identifyingElectrodes

abstract class TokenParser {

	protected Closure onParse

	public TokenParser( Closure onParse ) {
		this.onParse = onParse
	}

	Object parse( List<String> values, List<List<String>> additionalRows ) {
		Object val = doParse( values, additionalRows )
		if ( onParse ) {
			onParse( val )
		}
		return val
	}

	abstract protected Object doParse( List<String> values, List<List<String>> additionalRows )

	abstract protected String getToken()

	public int requiredRows() {
		1
	}

	String toString() {
		"Parses token: $token"
	}

	/**
	 * Checks that values has at least one value, that has a stringlength of greater than one.
	 * If not, throws a TokenParserException.
	 * @param values
	 */
	protected assertHasValue( List<String> values ) {
		if ( !( values.size() > 0 && values[ 0 ]?.length() > 0 ) ) {
			throw new TokenParserException( this, "Expected value after $token. Got nothing." )
		}
	}

}
