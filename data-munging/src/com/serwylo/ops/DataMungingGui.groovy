package com.serwylo.ops

import com.serwylo.ops.Phase
import com.serwylo.ops.PhaseFailedException
import com.serwylo.ops.electrodes.Model
import com.serwylo.ops.electrodes.phases.*
import groovy.swing.SwingBuilder

import javax.swing.BoxLayout
import javax.swing.JLabel
import java.awt.BorderLayout
import java.awt.Color

abstract class DataMungingGui {

	private static final COLOUR_COMPLETED_PHASE  = Color.GREEN
	private static final COLOUR_CURRENT_PHASE    = Color.BLUE
	private static final COLOUR_FUTURE_PHASE     = Color.GRAY

	protected Map<Phase, JLabel> phaseComponents = [:]
	private SwingBuilder uiBuilder               = new SwingBuilder()

	private Phase currentPhase

	public show() {

		createPhaseComponents()

		ui.edt {
			frame( title : title, size : [ 300, 300 ], show : true ) {
				borderLayout()

				menuBar( constraints : BorderLayout.NORTH ) {
					menu( label : "File", mnemonic: "f" ) {
						menuItem( label : "Quit", mnemonic: "q", actionPerformed : quit )
					}
					menu( label : "Help", mnemonic: "h" ) {
						menuItem( label : "Instructions", mnemonic: "i", actionPerformed : help )
					}
				}

				panel( constraints : BorderLayout.WEST ) {
					boxLayout( axis : BoxLayout.Y_AXIS )
					label( text : "WEST" )
				}

				panel( constraints : BorderLayout.CENTER ) {
					flowLayout()
					label( text : "CENTER" )
				}

				panel( constraints : BorderLayout.EAST ) {
					boxLayout( axis : BoxLayout.Y_AXIS )
					phaseComponents.each { entry ->
						widget( entry.value )
					}
				}
			}
		}

		nextPhase()
	}

	abstract List<Phase> getPhases()

	abstract String getTitle()

	protected SwingBuilder getUi() {
		this.uiBuilder
	}

	private void createPhaseComponents() {
		List<Phase> phaseList = getPhases()
		phaseList.eachWithIndex { phase, index ->
			JLabel label = ui.label( text : "${index + 1}: $phase.name", foreground: COLOUR_FUTURE_PHASE )
			phaseComponents.putAt( phase, label )
		}
	}

	protected void nextPhase() {

		boolean allCompleted = false

		if ( currentPhase ) {
			unhighlightPhase( currentPhase )
			Integer index = phases.indexOf( currentPhase )
			if ( index < phases.size() - 1 ) {
				currentPhase = phases[ index + 1 ]
			} else {
				allCompleted = true
			}
		} else {
			currentPhase = phases[ 0 ]
		}

		if ( !allCompleted ) {
			highlightPhase( currentPhase )
			println "Starting phase: $currentPhase"
			try {
				currentPhase.execute()
				nextPhase()
			} catch ( PhaseFailedException e ) {

			}
		}

	}

	private void unhighlightPhase( Phase phase ) {
		getLabelFor( phase ).foreground = COLOUR_COMPLETED_PHASE
	}

	private void highlightPhase( Phase phase ) {
		getLabelFor( phase ).foreground = COLOUR_CURRENT_PHASE
	}

	private JLabel getLabelFor( Phase phase ) {
		phaseComponents.getAt( phase )
	}

	protected def quit = {
		System.exit( 0 )
	}

	protected def help = {
		// TODO: Navigate to GitHub wiki.
	}

}
