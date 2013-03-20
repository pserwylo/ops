package com.serwylo.ops

import com.serwylo.ops.gui.ProgressEvent
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

	ProgressListener progressListener

	protected void dispatchProgressEvent( int progress, String currentDescription ) {
		if ( progressListener ) {
			progressListener.onProgress( new ProgressEvent( progress : progress, currentDescription : currentDescription ) )
		}
	}

	interface ProgressListener {
		void onProgress( ProgressEvent event )
	}

}
