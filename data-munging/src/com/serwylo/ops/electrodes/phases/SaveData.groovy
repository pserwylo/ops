package com.serwylo.ops.electrodes.phases

import com.serwylo.ops.PhaseFailedException
import com.serwylo.uno.spreadsheet.CsvOptions
import com.sun.star.comp.helper.BootstrapException
import com.sun.star.task.ErrorCodeIOException
import groovy.swing.SwingBuilder

import javax.swing.*

class SaveData extends ElectrodesPhase {

	@Override
	boolean requiresUserInteraction() {
		false
	}

	@Override
	String getName() {
		return "Save averaged data"
	}

	@Override
	boolean execute() throws PhaseFailedException {
		dispatchIndeterminateProgressEvent( "Saving averaged data file..." )
		try {
			data.save()
		} catch ( ErrorCodeIOException e ) {
			// TODO: Get ErrCode to be more interesting.
			throw new PhaseFailedException( this, "Error saving averaged data file." )
		}
		data.document.close()
		return true
	}

}
