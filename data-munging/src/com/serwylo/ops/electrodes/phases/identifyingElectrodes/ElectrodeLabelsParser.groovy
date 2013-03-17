package com.serwylo.ops.electrodes.phases.identifyingElectrodes

class ElectrodeLabelsParser extends TokenParser{

	ElectrodeLabelsParser( Closure onParse = null ) {
		super( onParse )
	}

	protected Object doParse( List<String> values, List<List<String>> additionalRows ) {
		if ( additionalRows.size() == 0 ) {
			throw new TokenParserException( this, "Expected another row of values after $token." )
		}

		Map<String, Integer> labelIndexMap = [:]
		List<String> electrodeLabels = additionalRows.pop()
		for ( def i in 0..( electrodeLabels.size()-1 ) ) {
			String label = electrodeLabels[ i ]
			if ( label?.length() > 2 ) {
				label = label.substring( 1, label.length() - 1 ).trim()
				labelIndexMap.put( label, i )
			} else {
				// Probably at the end of the list of electrodes, because we overcompensated when pulling in data to
				// look after, so no point continuing.
				break;
			}
		}

		return labelIndexMap
	}

	protected String getToken() {
		"[Electrode Labels]"
	}

	public int requiredRows() {
		2
	}
	
}
