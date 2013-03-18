package com.serwylo.ops.electrodes.gui

import com.kitfox.svg.SVGCache
import com.kitfox.svg.SVGDiagram
import com.kitfox.svg.SVGElement
import com.kitfox.svg.animation.AnimationElement
import com.kitfox.svg.app.beans.SVGPanel
import sun.awt.image.ImageFormatException

import javax.swing.JPanel
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Point2D

class ElectrodeSelector extends JPanel {

	private SVGPanel             panel
	private SVGDiagram           diagram
	private SVGElement keyNormal, keyDead, keyWeird, keySelected
	private Map<String, Integer> electrodeLabels

	private List<String>         deadElectrodes = [], weirdElectrodes = [], selectedElectrodes = []

	public ElectrodeSelector( Map<String, Integer> electrodes ) {

		electrodeLabels  = electrodes
		panel            = new SVGPanel()
		URI uri          = loadSvg()
		diagram          = SVGCache.getSVGUniverse().getDiagram( uri )
		panel.svgURI     = loadSvg()
		panel.svgURI     = uri
		panel.antiAlias  = true
		panel.scaleToFit = true

		validateItemsInImage()

		keyNormal   = diagram.getElement( "keyNormal" )
		keyDead     = diagram.getElement( "keyDead" )
		keyWeird    = diagram.getElement( "keyWeird" )
		keySelected = diagram.getElement( "keySelected" )

		electrodes.keySet().each { colName ->
			SVGElement element = diagram.getElement( colName )
			reset( element )
		}

		panel.addMouseListener( new MouseAdapter() {
			@Override
			void mouseClicked( MouseEvent event ) {
				Point2D point = scaleMousePoint( event.point )
				List<List<SVGElement>> found = diagram.pick( point, null )
				found.each { pathToFound ->
					SVGElement element = pathToFound.pop()
					if ( electrodes.containsKey( element.id ) ) {
						toggleElectrode( element )
					}
				}
			}
		})

		add( panel )

		this.background = Color.WHITE
	}

	public void setPreferredSize( Dimension size ) {
		super.setPreferredSize( size )
		panel.setPreferredSize( size )
	}

	public void setMaximumSize( Dimension size ) {
		super.setMaximumSize( size )
		panel.setMaximumSize( size )
	}

	public void setMinimumSize( Dimension size ) {
		super.setMinimumSize( size )
		panel.setMinimumSize( size )
	}

	private void validateItemsInImage() {
		electrodeLabels.each { entry ->
			String colName     = entry.key
			SVGElement element = diagram.getElement( colName )
			if ( element == null ) {
				throw new ImageFormatException( "Couldn't find electrode $colName in the image." )
			}
		}

		[ "keyNormal", "keyDead", "keyWeird", "keySelected" ].each {
			if ( !diagram.getElement( it ) ) {
				throw new ImageFormatException( "Could not find key element '$it' in the image." )
			}
		}
	}

	protected Point2D scaleMousePoint( Point2D point ) {
		[
			(int)( point.x * diagram.width / panel.width ),
			(int)( point.y * diagram.height / panel.height )
		] as Point
	}

	protected URI loadSvg() {

		File svgFile = new File( "/home/pete/code/ops/data-munging/electrodes/electrode-layout-68.svg" )
		URI uri      = SVGCache.getSVGUniverse().loadSVG( new FileReader( svgFile ), "electrode-layout" )
		return uri
	}

	public void setSelectedElectrodes( List<String> electrodes ) {
		selectedElectrodes.each { colName -> reset( diagram.getElement( colName ) ) }
		selectedElectrodes = electrodes
		selectedElectrodes.each { colName -> selected( diagram.getElement( colName ) ) }
	}

	public List<String> getSelectedElectrodes() {
		selectedElectrodes
	}

	public void setDeadElectrodes( List<String> electrodes ) {
		this.deadElectrodes.each { colName -> reset( diagram.getElement( colName ) ) }
		this.deadElectrodes = electrodes
		this.deadElectrodes.each { colName -> dead( diagram.getElement( colName ) ) }
	}

	public void setWeirdElectrodes( List<String> electrodes ) {
		this.weirdElectrodes.each { colName -> reset( diagram.getElement( colName ) ) }
		this.weirdElectrodes = electrodes
		this.weirdElectrodes.each { colName -> weird( diagram.getElement( colName ) ) }
	}

	void toggleElectrode( SVGElement element ) {
		if ( selectedElectrodes.contains( element.id ) ) {
			selectedElectrodes.remove( element.id )
			unselected( element )
		} else {
			selectedElectrodes.add( element.id )
			selected( element )
		}
		panel.repaint()
	}

	private void reset( SVGElement element ) {
		normal( element )
		if ( selectedElectrodes.contains( element.id ) ) {
			selected( element )
		}
		if ( weirdElectrodes.contains( element.id ) ) {
			weird( element )
		}
	}

	/**
	 * Get #ffffff html hex number for a colour
	 * @see Integer#toHexString(int)
	 * @link http://www.mindprod.com/jgloss/hex.html
	 * @param colour Color object whose html colour number you want as a string
	 * @return # followed by exactly 6 hex digits
	 */
	private String colourToHex( Color colour ) {
		String string = Integer.toHexString( colour.getRGB() & 0xffffff )
		if ( string.length() < 6 )
		{
			// pad on left with zeros
			string = "000000".substring( 6 - string.length() ) + string
		}
		return "#$string"
	}

	private void copyStrokeStyle( SVGElement from, SVGElement to ) {
		String colourValue = colourToHex( from.getStyleAbsolute( "stroke" ).colorValue )
		to.setAttribute( "stroke", AnimationElement.AT_CSS, colourValue )
	}

	private void copyStrokeWidthStyle( SVGElement from, SVGElement to ) {
		to.setAttribute( "stroke-width", AnimationElement.AT_CSS, from.getStyleAbsolute( "stroke-width" ).doubleValue.toString() )
	}

	private void copyFillStyle( SVGElement from, SVGElement to ) {
		String colourValue = colourToHex( from.getStyleAbsolute( "fill" ).colorValue )
		to.setAttribute( "fill", AnimationElement.AT_CSS, colourValue )
	}

	private void copyFillOpacityStyle( SVGElement from, SVGElement to ) {
		to.setAttribute( "fill-opacity", AnimationElement.AT_CSS, from.getStyleAbsolute( "fill-opacity" ).doubleValue.toString() )
	}

	private void normal( SVGElement element ) {
		copyFillOpacityStyle( keyNormal, element )
		copyFillStyle( keyNormal, element )
		copyStrokeStyle( keyNormal, element )
		copyStrokeWidthStyle( keyNormal, element )
	}

	private void selected( SVGElement element ) {
		copyFillOpacityStyle( keySelected, element )
		copyStrokeStyle( keySelected, element )
		copyStrokeWidthStyle( keySelected, element )
	}

	private void unselected( SVGElement element ) {
		copyFillOpacityStyle( keyNormal, element )
		copyStrokeStyle( keyNormal, element )
		copyStrokeWidthStyle( keyNormal, element )
	}

	private void dead( SVGElement element ) {
		copyFillStyle( keyDead, element )
	}

	private void weird( SVGElement element ) {
		copyFillStyle( keyWeird, element )
	}

}
