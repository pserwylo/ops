package com.serwylo.ops.gui

import com.serwylo.ops.Phase
import com.serwylo.ops.PhaseFailedException
import com.serwylo.ops.electrodes.Model
import com.serwylo.ops.electrodes.phases.*
import groovy.swing.SwingBuilder

import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.BorderLayout
import java.awt.Color

abstract class DataMungingGui {

	private static final COLOUR_COMPLETED_PHASE  = Color.GREEN
	private static final COLOUR_CURRENT_PHASE    = Color.BLUE
	private static final COLOUR_FUTURE_PHASE     = Color.GRAY

	protected Map<Phase, JLabel> phaseComponents = [:]
	private SwingBuilder uiBuilder               = new SwingBuilder()

	Phase   currentPhase
	JPanel  currentPhasePanel
	JButton currentPhaseDoneButton
	JFrame  windowFrame

	public show() {

		createPhaseComponents()

		ui.edt {
			windowFrame = frame( title : title, size : [ 800, 640 ], show : true, defaultCloseOperation: JFrame.EXIT_ON_CLOSE ) {
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
				}

				currentPhasePanel = panel( constraints : BorderLayout.CENTER )

				currentPhaseDoneButton = button( "Done", actionPerformed: phaseDone, constraints: BorderLayout.SOUTH )

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
			println "PHASE RUNNER: Created new thread for $currentPhase"
			new Thread( phaseRunner ).run()
		}
	}

	private void unhighlightPhase( Phase phase ) {
		getLabelFor( phase ).foreground = COLOUR_COMPLETED_PHASE
		currentPhasePanel.removeAll()
		currentPhasePanel.revalidate()
		currentPhasePanel.repaint()
	}

	private void highlightPhase( Phase phase ) {
		try {
			JComponent gui = phase.gui
			getLabelFor( phase ).foreground = COLOUR_CURRENT_PHASE
			if ( gui ) {
				currentPhasePanel.add( gui )
				currentPhasePanel.revalidate()
				currentPhasePanel.repaint()
			}
		currentPhaseDoneButton.visible = phase.requiresUserInteraction()
		} catch ( PhaseFailedException e ) {
			System.err.println "Failed phase $e.phase.name: $e.message"
		}
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

	Runnable phaseRunner = new PhaseRunner()
	Runnable doneClicked = new DoneClicked()

	def phaseDone = {
		println "PHASE RUNNER: Clicked 'DONE'. Queuing phase runner thread."
		SwingUtilities.invokeLater( doneClicked )
	}

	Phase getCurrentPhase() {
		currentPhase
	}

	class DoneClicked implements Runnable {
		@Override
		void run() {
			println "DONE CLICKED: Start ${getCurrentPhase()}"
			if ( getCurrentPhase().execute() ) {
				println "DONE CLICKED: Executing done, nextPhase()"
				nextPhase()
			}
			println "DONE CLICKED: Done"
		}
	}

	class PhaseRunner implements Runnable {
		@Override
		void run() {
			println "PHASE RUNNER: Start"
			if ( !getCurrentPhase().requiresUserInteraction() ) {
				println "PHASE RUNNER: Doesn't require interaction, so attempting execute continually."
				try {

					boolean continueOn = false
					while ( !continueOn ) {
						continueOn = getCurrentPhase().execute()
					}

					println "PHASE RUNNER: nextPhase() started - ${getCurrentPhase()}"
					nextPhase()
					println "PHASE RUNNER: nextPhase() completed"

				} catch ( PhaseFailedException e ) {
					System.err.println "Failed phase $e.phase.name: $e.message"
				}
			} else {
				println "PHASE RUNNER: Not invoking execute (instead waiting for user interaction)"
				// Wait for the user to click the "Done" button.
			}
			println "PHASE RUNNER: run() completed"
		}
	}

}
