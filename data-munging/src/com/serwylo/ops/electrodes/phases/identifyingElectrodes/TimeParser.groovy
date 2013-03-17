package com.serwylo.ops.electrodes.phases.identifyingElectrodes

class TimeParser extends TokenParser {

	TimeParser( Closure onParse = null ) {
		super( onParse )
	}

	protected Object doParse( List<String> values, List<List<String>> additionalRows ) {
		assertHasValue( values )
		values[ 0 ]
	}

	protected String getToken() {
		"[Time]"
	}
	
}
