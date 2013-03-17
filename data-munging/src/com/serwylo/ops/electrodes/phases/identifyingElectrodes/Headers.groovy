package com.serwylo.ops.electrodes.phases.identifyingElectrodes

class Headers {

	String time
	int channels
	int rate
	String type
	String rows
	int startRow
	Map<String,Integer> electrodeLabels

	private List<TokenParser> parserList = [
		new TimeParser           ( { time            = it } ),
		new ChannelsParser       ( { channels        = it } ),
		new RateParser           ( { rate            = it } ),
		new TypeParser           ( { type            = it } ),
		new RowsParser           ( { rows            = it } ),
		new ElectrodeLabelsParser( { electrodeLabels = it } ),
		new ContinuousDataParser (),
	]

	public void parse( String[][] data ) {
		int rowCount = data.length - 1
		for ( int rowIndex = 0; rowIndex < rowCount; rowIndex ++ ) {
			List<String> row = data[ rowIndex ].toList()
			String token = row.remove( 0 )
			TokenParser parser = getParserFor( token )

			if ( !parser ) {
				continue
			}

			if ( parser instanceof ContinuousDataParser ) {
				startRow = rowIndex + 1
			}

			List<List<String>> additionalRows = []
			int requiredRows = parser.requiredRows()
			while ( requiredRows > 1 && rowIndex < rowCount ) {
				rowIndex ++
				requiredRows --
				additionalRows.add( data[ rowIndex ].toList() )
			}

			parser.parse( row, additionalRows )
		}
	}

	private TokenParser getParserFor( String t ) {
		parserList.find { t == it.token }
	}

}
