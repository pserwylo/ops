package com.serwylo.ops

import groovy.swing.SwingBuilder

import javax.swing.JComponent

abstract class Phase {

	abstract boolean requiresUserInteraction()

	abstract String getName()

	abstract boolean execute()

	String getDescription() {
		""
	}

	JComponent getGui() {
		null
	}

	String toString() {
		name
	}

}
