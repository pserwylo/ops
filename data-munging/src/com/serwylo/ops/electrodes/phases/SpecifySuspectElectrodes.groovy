package com.serwylo.ops.electrodes.phases

import com.kitfox.svg.SVGCache
import com.kitfox.svg.SVGDiagram
import com.kitfox.svg.SVGElement
import com.kitfox.svg.animation.AnimationElement
import com.kitfox.svg.app.beans.SVGPanel
import com.serwylo.ops.PhaseFailedException
import com.serwylo.ops.electrodes.gui.ElectrodeSelector
import com.sun.star.auth.InvalidArgumentException
import groovy.swing.SwingBuilder

import javax.swing.JComponent
import javax.swing.SwingUtilities
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

/**
 *
 */
class SpecifySuspectElectrodes extends ElectrodesPhase {

	ElectrodeSelector selector

	@Override
	boolean requiresUserInteraction() {
		true
	}

	@Override
	String getName() {
		"Ask for suspect electrodes"
	}

	@Override
	String getDescription() {
		"If you kept a diary while you were collecting data, you may have noticed electrodes with weird values. " +
		"In this case, you have the option to specify electrodes that you don't trust."
	}

	@Override
	void execute() {
		data.confirmedDeadColumns = selector.selectedElectrodes
	}

	JComponent getGui() {
		try {
			selector = new ElectrodeSelector( data.headers.electrodeLabels )
			selector.deadElectrodes     = data.deadColumns
			selector.weirdElectrodes    = data.weirdColumns
			// selector.selectedElectrodes = data.weirdColumns + data.deadColumns
			selector.minimumSize        = new Dimension( 600, 400 )
			selector.preferredSize      = new Dimension( 600, 400 )
			selector
		} catch ( Exception e ) {
			throw new PhaseFailedException( this, "Error specifying suspect electrodes: $e.message", e )
		}
	}
	
}