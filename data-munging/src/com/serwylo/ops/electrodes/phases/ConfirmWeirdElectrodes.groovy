package com.serwylo.ops.electrodes.phases

import groovy.swing.SwingBuilder

import javax.swing.BoxLayout
import javax.swing.DefaultListCellRenderer
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ListCellRenderer
import java.awt.Component

/**
 *
 */
class ConfirmWeirdElectrodes extends ElectrodesPhase {


	private Map<String,JCheckBox> checkboxList

	@Override
	boolean requiresUserInteraction() {
		true
	}

	@Override
	String getName() {
		"Confirm suspect electrodes"
	}

	@Override
	void execute() {
		checkboxList.each {
			String colName     = it.key
			JCheckBox checkBox = it.value
			if ( checkBox.selected ) {
				println "Confirmed: $colName is weird"
			}
		}
	}

	JComponent getGui() {
		checkboxList = [:]
		new SwingBuilder().scrollPane() {
			panel() {
				boxLayout( axis: BoxLayout.Y_AXIS )
				data.weirdColumns.each { colName ->
					checkboxList.put( colName, checkBox( label: colName ) )
				}
			}
		}
	}
	
}
