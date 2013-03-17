package com.serwylo.ops.electrodes.phases.identifyingElectrodes

class ChannelsParser extends TokenParser{

	ChannelsParser( Closure onParse = null ) {
		super( onParse )
	}

	protected Object doParse( List<String> values, List<List<String>> additionalRows ) {
		assertHasValue( values )
		String value = values[ 0 ]
		value ? value as Integer : 0
	}

	protected String getToken() {
		"[Channels]"
	}
	
}
