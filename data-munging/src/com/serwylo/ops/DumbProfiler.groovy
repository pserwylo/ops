package com.serwylo.ops

class DumbProfiler {

	private long startTime = System.currentTimeMillis()
	private long lastPrint = 0
	private long minTime   = 0
	private String label

	public DumbProfiler( String label = "", long minTime = 0 ) {
		this.label   = label
		this.minTime = minTime
		this.print "Starting"
	}

	void start() {
		startTime = System.currentTimeMillis()
	}

	private String format( long milliseconds ) {
		 return milliseconds
	}

	void print( String message = "" ) {

		String output = ""
		if ( label ) {
			output += label + ": "
		}

		output += format( System.currentTimeMillis() - startTime )

		long duration = System.currentTimeMillis() - lastPrint

		if ( lastPrint != 0 ) {
			output += " (" + format( duration ) + ")"
		}

		if ( message ) {
			output += " - $message"
		}

		if ( duration >= minTime ) {
			println output
		}

		lastPrint = System.currentTimeMillis()
	}

	private String convertToString() {
		String output =
		output
	}

}
