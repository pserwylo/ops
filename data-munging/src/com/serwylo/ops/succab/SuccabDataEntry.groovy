package com.serwylo.ops.succab

import com.serwylo.uno.spreadsheet.SpreadsheetConnector
import com.sun.star.sheet.XSpreadsheetDocument

public class SuccabDataEntry
{
	private SpreadsheetConnector connector
	private XSpreadsheetDocument unifiedSpreadsheet

	private Map<String, Participant> participants

	private String searchDir = ""

	public static void main( String[] args ) {
		new SuccabDataEntry().create()
		System.exit( 0 )
	}

	SuccabDataEntry() {
		searchDir = "/home/pete/Documents/people/laura/uni/data-entry/DATA_ASC"
	}

	private void searchForFiles() {
		participants = [:]
		String[] possibleSuffixes = [ "drt2", "rt1t2", "rt2t2", "srt2", "swt2" ]
		File dir = new File( searchDir )
		File[] children = dir.listFiles()

		for ( File child : children ) {

			String name = child.name.toLowerCase()
			if ( !name.endsWith( ".asc" ) ) {
				continue
			}

			// Does any file in here look like it belongs to a set of five files for a particular participant?
			String participantId
			for ( String suffix in possibleSuffixes ) {

				if ( name.endsWith( suffix + ".asc" ) ) {

					// Cool, now we know the participant id...
					participantId = name.substring( 0, name.length() - ( suffix + ".asc" ).length() )

					// But because it can belong to a set of five files, we may have already visited its friends before...
					if ( !participants.containsKey( participantId ) ) {

						// Keep track of which files are missing, so that we can bail on this participant if we don't have a full set...
						List<String> missingData = []
						for ( String s in possibleSuffixes )
						{
							File f = new File( searchDir + File.separator + ( participantId + s + ".asc" ).toUpperCase() )
							if ( !f.exists() )
							{
								missingData.add( s )
							}
						}

						println "Found participant: $participantId"
						Participant participant = new Participant( participantId )
						if ( missingData.size() > 0 ) {
							println "Cannot find ${missingData.join( ", " )} for $participantId"
							participant.missingData = missingData
						}
						participants.put( participantId, participant )
					}
				}
			}
		}
	}

	public void create()
	{
		searchForFiles()
		connector = new SpreadsheetConnector()

		for ( Map.Entry<String, Participant> entry in participants )
		{
			Participant participant = entry.value
			ParticipantDataEnterer enterer = new ParticipantDataEnterer( connector, participant )
			enterer.enterData()
			break
		}
	}

}