package com.serwylo.ops

import groovy.swing.SwingBuilder

import javax.swing.JComponent

abstract class Phase {

	abstract boolean requiresUserInteraction()

	abstract String getName()

	abstract void execute()

	String getDescription() {
		""
	}

	String toString() {
		name
	}

	SwingBuilder getGui() {

	}

}
