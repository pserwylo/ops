package com.serwylo.ops.electrodes

import com.serwylo.ops.OpsPreferences

class ElectrodesPreferences extends OpsPreferences {

	private static final String WEIRD_MIN = "weirdMin";
	private static final String WEIRD_MAX = "weirdMax";

	public static boolean hasWeirdMin() {
		weirdMin != 0
	}

	public static boolean hasWeirdMax() {
		weirdMax != 0
	}

	public static Double getWeirdMin() {
		instance.preferences.getDouble( WEIRD_MIN, 0 )
	}

	public static void setWeirdMin( Double value ) {
		instance.preferences.putDouble( WEIRD_MIN, value )
	}

	public static Double getWeirdMax() {
		instance.preferences.getDouble( WEIRD_MAX, 0 )
	}

	public static void setWeirdMax( Double value ) {
		instance.preferences.putDouble( WEIRD_MAX, value )
	}

}
