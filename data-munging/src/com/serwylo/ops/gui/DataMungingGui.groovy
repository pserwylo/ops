package com.serwylo.ops.gui

import com.serwylo.ops.Phase
import com.serwylo.ops.Phase.ProgressListener
import com.serwylo.ops.PhaseFailedException
import groovy.swing.SwingBuilder

import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JProgressBar
import java.awt.BorderLayout
import java.awt.Color

abstract class DataMungingGui implements ProgressListener {

	private static final COLOUR_COMPLETED_PHASE  = Color.GREEN
	private static final COLOUR_CURRENT_PHASE    = Color.BLUE
	private static final COLOUR_FUTURE_PHASE     = Color.GRAY

	protected Map<Phase, JLabel> phaseComponents = [:]
	private SwingBuilder uiBuilder               = new SwingBuilder()

	Phase        currentPhase
	JPanel       currentPhasePanel
	JButton      currentPhaseDoneButton
	JFrame       windowFrame
	JLabel       currentStatusLabel
	JProgressBar currentProgressBar



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

				panel( constraints : BorderLayout.CENTER ) {
					boxLayout( axis : BoxLayout.Y_AXIS )
					currentPhasePanel      = panel( constraints : BorderLayout.CENTER )
					currentPhaseDoneButton = button( "Done", actionPerformed: phaseDone, constraints: BorderLayout.SOUTH )
				}

				panel( constraints: BorderLayout.SOUTH ) {
					boxLayout( axis : BoxLayout.Y_AXIS )
					currentStatusLabel = label()
					currentProgressBar = progressBar( minimum : 0, maximum : 100 )
				}

				panel( constraints : BorderLayout.EAST ) {
					boxLayout( axis : BoxLayout.Y_AXIS )
					phaseComponents.each { entry ->
						widget( entry.value )
					}
				}
			}
		}

		phaseRunner = new PhaseRunner( phases[ 0 ] )
		new Thread( phaseRunner ).run()
	}

	abstract List<Phase> getPhases()

	abstract String getTitle()

	protected SwingBuilder getUi() {
		this.uiBuilder
	}

	public void onProgress( ProgressEvent event ) {
		currentStatusLabel.text  = event.currentDescription
		if ( event.indeterminate ) {
			currentProgressBar.indeterminate = true
		} else {
			currentProgressBar.indeterminate = false
			currentProgressBar.value = event.progress
		}
	}

	protected void resetProgress() {
		onProgress( new ProgressEvent( progress: 0, currentDescription: "" ) )
	}

	private void createPhaseComponents() {
		List<Phase> phaseList = getPhases()
		phaseList.eachWithIndex { phase, index ->
			JLabel label = ui.label( text : "${index + 1}: $phase.name", foreground: COLOUR_FUTURE_PHASE )
			phaseComponents.putAt( phase, label )
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
			getLabelFor( phase ).foreground = COLOUR_CURRENT_PHASE
			resetProgress()
			phase.progressListener = this
			JComponent gui         = phase.gui
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

	PhaseRunner phaseRunner

	def phaseDone = {
		println "Y: Clicked done. Queuing next phase."
		int index = getPhases().indexOf( phaseRunner.currentPhase )
		println "Y: Found phase index at $index"
		if ( index != -1 ) {
			if ( index < phases.size() - 1 ) {
				Phase next = phases[ index + 1 ]
				println "Y: Next phase will be - $next"
				synchronized ( phaseRunner.guiWaitLock ) {
					phaseRunner.nextPhaseAfterGui = next
					phaseRunner.guiWaitLock.notify()
				}
			} else {
				println "Y: All completed"
				phaseRunner.allCompleted = true
			}
		}
	}

	Phase getCurrentPhase() {
		currentPhase
	}

	class PhaseRunner implements Runnable {

		public Phase   nextPhase
		public Phase   nextPhaseAfterGui
		public boolean allCompleted = false
		public final Object guiWaitLock = new Object()

		private Phase   currentPhase
		private boolean pendingGuiResponse = false

		PhaseRunner( Phase firstPhase ) {
			nextPhase = firstPhase
		}

		@Override
		void run() {

			while ( true ) {

				if ( allCompleted ) {
					println "X: All completed. Bailing loop."
					break
				}

				if ( nextPhase ) {
					println "X: Next phase ($nextPhase)"
					if ( currentPhase ) {
						println "X: First, lets unhighlight $currentPhase"
						unhighlightPhase( currentPhase )
					}
					println "X: Starting phase $nextPhase"
					currentPhase = nextPhase
					println "X: Highlighting phase - $currentPhase"
					highlightPhase( currentPhase )
					nextPhase          = null
					nextPhaseAfterGui  = null
					pendingGuiResponse = false
				}

				if ( nextPhaseAfterGui ) {
					println "X: Gui phase - about to start next phase. Lets execute first."
					if ( !currentPhase.execute() ) {
						nextPhaseAfterGui = null
						continue
					}
					println "X: Done executing gui phase"
					nextPhase          = nextPhaseAfterGui
					nextPhaseAfterGui  = null
					pendingGuiResponse = false
				}

				if ( pendingGuiResponse ) {
					synchronized ( guiWaitLock ) {
						guiWaitLock.wait()
						continue
					}
				}

				if ( !currentPhase.requiresUserInteraction() ) {

					boolean continueOn = false
					while ( !continueOn ) {
						println "X: No interaction, attempting execution"
						continueOn = currentPhase.execute()
						if ( continueOn ) {
							println "X: Execution fine, roceeding"
						}
					}

					int index = getPhases().indexOf( currentPhase )
					if ( index != -1 ) {
						if ( index < getPhases().size() - 1 ) {
							println "X: Completed no interaction, queueing next phase"
							nextPhase = getPhases()[ index + 1 ]
						} else {
							println "X: Completed no interaction, all completed"
							allCompleted = true
						}
					}
				} else {
					pendingGuiResponse = true
					println "X: Pending gui response"
				}
			}
		}
	}

}
