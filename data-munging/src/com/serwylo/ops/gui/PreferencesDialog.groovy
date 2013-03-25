package com.serwylo.ops.gui

import com.serwylo.ops.OpsPreferences
import com.serwylo.uno.spreadsheet.CsvOptions
import com.serwylo.uno.utils.OfficeFinder
import com.serwylo.uno.utils.OfficeNotFoundException
import groovy.swing.SwingBuilder
import ooo.connector.server.OfficePath

import javax.swing.BoxLayout
import javax.swing.JDialog
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.JTextField
import java.awt.Dimension

class PreferencesDialog {

	public JTextField fieldOfficeDirectory
	public JDialog    dialog

	PreferencesDialog() {

		SwingBuilder ui = new SwingBuilder()

		ui.edt {
			dialog = dialog( title: "Preferences", size: [ 600, 400 ] as Dimension, visible: true, modal: true ) {
				boxLayout( axis: BoxLayout.Y_AXIS )

				panel() {
					flowLayout()
					label( "Libre/OpenOffice" )
					fieldOfficeDirectory = textField( text: OpsPreferences.instance.officePath, columns: 20, focusLost: persistOfficeDirectory )
					button( "Default", actionPerformed: defaultOfficeDirectory )
					button( "Browse", actionPerformed:  browseOfficeDirectory )
				}

				button( "Close", actionPerformed: close )
			}
		}
	}

	public def close = {
		dialog.visible = false
	}

	protected void setOfficeDirectory( String dir ) {
		fieldOfficeDirectory.text          = dir
		OpsPreferences.instance.officePath = dir
	}

	public def persistOfficeDirectory = {
		setOfficeDirectory( fieldOfficeDirectory.text )
	}

	public def defaultOfficeDirectory = {
		officeDirectory = OfficeFinder.createFinder().path?.binaryFile
	}

	public def browseOfficeDirectory = {

		JFileChooser fc = new SwingBuilder().fileChooser(
			dialogTitle: "Path to LibreOffice or OpenOffice",
			fileSelectionMode: JFileChooser.DIRECTORIES_ONLY
		)

		if ( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {
			OfficeFinder finder = OfficeFinder.createFinder()
			finder.searchDir    = fc.selectedFile
			try {
				OfficePath path = finder.path
				setOfficeDirectory( path.binaryFile.absolutePath )
			} catch ( OfficeNotFoundException e ) {
				JOptionPane.showMessageDialog( dialog, "Could not find OpenOffice or LibreOffice in this folder.", "Office not found", JOptionPane.ERROR_MESSAGE )
				setOfficeDirectory( "" )
			}

		}

	}

}
