package com.serwylo.ops.electrodes.phases.identifyingElectrodes

class ContinuousDataParser extends TokenParser{

	ContinuousDataParser( Closure onParse = null ) {
		super( onParse )
	}

	protected Object doParse( List<String> values, List<List<String>> additionalRows ) {
		null
	}

	protected String getToken() {
		"[Continuous Data]"
	}

	public int requiredRows() {
		1
	}
	
}
