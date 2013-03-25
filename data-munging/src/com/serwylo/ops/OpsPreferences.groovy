package com.serwylo.ops

import com.serwylo.uno.utils.OfficeFinder

import java.util.prefs.Preferences

class OpsPreferences {

	protected final String OFFICE_PATH     = "officePath"
	private static OpsPreferences instance = new OpsPreferences()

	protected Preferences preferences      = Preferences.userNodeForPackage( OpsPreferences.class )

	public static OpsPreferences getInstance() {
		instance
	}

	public String getOfficePath() {
		String path = preferences.get( OFFICE_PATH, null )
		if ( !path ) {
			String defaultOfficePath = OfficeFinder.createFinder().path?.binaryFile ?: ""
			setOfficePath( defaultOfficePath )
			path = defaultOfficePath
		}
		path
	}

	public void setOfficePath( String path ) {
		preferences.put( OFFICE_PATH, path ?: "" )
	}

}
