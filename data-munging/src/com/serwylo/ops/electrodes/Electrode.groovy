package com.serwylo.ops.electrodes

class Electrode {

	private String colName
	private int colIndex

	public Double  maxValue = null
	public Double  minValue = null
	public boolean isEmpty  = false

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

	String toString() {
		colName
	}

}
