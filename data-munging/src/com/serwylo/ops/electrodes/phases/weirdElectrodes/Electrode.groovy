package com.serwylo.ops.electrodes.phases.weirdElectrodes

class Electrode {

	public static int upperThreshold
	public static int lowerThreshold

	private String colName
	private int colIndex

	public Double  maxValue = null
	public Double  minValue = null
	public boolean isEmpty  = false
	public boolean confirmedWeird = false
	public boolean manuallySpecifiedAsWeird = false

	Electrode( String name, int colIndex ) {
		this.colName = name
		this.colIndex = colIndex
	}

	String getName() {
		colName
	}

	int getColIndex() {
		colIndex
	}

	boolean isDead() {
		isEmpty || confirmedWeird || manuallySpecifiedAsWeird
	}

	boolean isWeird() {
		maxValue > upperThreshold || minValue < lowerThreshold
	}

	boolean isTooLow() {
		minValue < lowerThreshold
	}

	boolean isTooHigh() {
		maxValue < upperThreshold
	}

	String toString() {
		String output = colName
		if ( isDead() ) {
			" (dead)"
		} else if ( isWeird() ) {
			output += " (weird because "
			output += isTooLow() ? "$minValue < $lowerThreshold" : "$maxValue > $upperThreshold"
			output += ")"
		} else if ( manuallySpecifiedAsWeird ) {
			output += " (weird because you said so)"
		}
		output
	}

}
