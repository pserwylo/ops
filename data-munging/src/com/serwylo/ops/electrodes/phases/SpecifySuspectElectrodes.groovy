package com.serwylo.ops.electrodes.phases

import com.kitfox.svg.SVGCache
import com.kitfox.svg.SVGDiagram
import com.kitfox.svg.SVGElement
import com.kitfox.svg.animation.AnimationElement
import com.kitfox.svg.app.beans.SVGPanel
import com.serwylo.ops.PhaseFailedException
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

	SVGPanel   panel
	SVGDiagram svgDiagram
	private Set<String> selectedElectrodes = []

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
		// TODO: Implement
	}

	JComponent getGui() {
		panel                 = new SVGPanel()
		File svgFile          = new File( "/home/pete/code/ops/data-munging/electrodes/electrode-layout-68.svg" )
		URI uri               = SVGCache.getSVGUniverse().loadSVG( new FileReader( svgFile ), "electrode-layout" )
		svgDiagram            = SVGCache.getSVGUniverse().getDiagram( uri )
		panel.antiAlias       = true
		panel.svgURI          = uri
		panel.minimumSize     = new Dimension( 600, 400 )
		panel.preferredSize   = new Dimension( 600, 400 )
		panel.scaleToFit      = true

		List<String> electrodeLabels = data.headers.electrodeLabels.keySet().collect { "electrode" + it.toUpperCase() }

		data.headers.electrodeLabels.each { entry ->
			String colName     = entry.key
			String svgId       = "element${colName.toUpperCase()}"
			SVGElement element = svgDiagram.getElement( svgId )

			if ( element == null ) {
				throw new PhaseFailedException( SpecifySuspectElectrodes.this, "Couldn't find electrode $colName in the image." )
			}

			if ( data.deadColumns.contains( colName ) ) {
				dead( element )
			}

			if ( data.weirdColumns.contains( colName ) ) {
				weird( element )
			} else {
				unselected( element )
			}
		}

		panel.addMouseListener( new MouseAdapter() {
			@Override
			void mouseClicked(MouseEvent e) {

				Point scaledPoint = [
					(int)( e.x * svgDiagram.width / panel.width ),
					(int)( e.y * svgDiagram.height / panel.height )
				]

				List<List<SVGElement>> found = svgDiagram.pick( scaledPoint, null )

				SVGElement electrodeElement
				for ( List<SVGElement> pathToFound in found ) {
					SVGElement element = pathToFound.pop()
					String electrodeId = element.id
					if ( electrodeLabels.contains( electrodeId ) ) {
						electrodeElement = element
						break;
					}
				}

				if ( electrodeElement ) {
					toggleElectrode( electrodeElement )
				}
			}
		})

		return panel
	}

	private String labelFromElement( SVGElement element ) {
		element.id.substring( "electrode".size() )
	}

	void toggleElectrode( SVGElement electrodeElement ) {
		String electrodeName = labelFromElement( electrodeElement )
		if ( selectedElectrodes.contains( electrodeName ) ) {
			unselected( electrodeElement )
		} else {
			selected( electrodeElement )
		}
		panel.repaint()
	}

	private void selected( SVGElement element ) {
		selectedElectrodes.add( labelFromElement( element ) )
		element.setAttribute( "stroke", AnimationElement.AT_CSS, "red" )
		element.setAttribute( "stroke-width", AnimationElement.AT_CSS, "6" )
		element.setAttribute( "fill-opacity", AnimationElement.AT_CSS, "1.0" )
	}

	private void unselected( SVGElement element ) {
		String id = labelFromElement( element )
		if ( selectedElectrodes.contains( id ) ) {
			selectedElectrodes.remove( id )
		}

		element.setAttribute( "stroke", AnimationElement.AT_CSS, "black" )
		element.setAttribute( "stroke-width", AnimationElement.AT_CSS, "1" )
		element.setAttribute( "fill-opacity", AnimationElement.AT_CSS, "0.3" )
	}

	private void dead( SVGElement element ) {
		element.setAttribute( "fill", AnimationElement.AT_CSS, "gray" )
	}

	private void weird( SVGElement element ) {
		element.setAttribute( "fill", AnimationElement.AT_CSS, "orange" )
	}
	
}