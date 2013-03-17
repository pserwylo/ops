package com.serwylo.ops.electrodes.phases.identifyingElectrodes

class TypeParser extends TokenParser {

	TypeParser( Closure onParse = null ) {
		super( onParse )
	}

	protected Object doParse( List<String> values, List<List<String>> additionalRows ) {
		assertHasValue( values )
		values[ 0 ]
	}

	protected String getToken() {
		"[Type]"
	}
	
}
