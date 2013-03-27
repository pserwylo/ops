package com.serwylo.ops.electrodes.gui

import com.serwylo.ops.electrodes.Model
import groovy.swing.SwingBuilder

import javax.swing.*
import java.awt.*
import java.text.DecimalFormat
import java.util.List

class WeirdElectrodeSelector extends ElectrodeSelector {

	private Model   model
	private JPanel  sliderPanel
	private JSlider minSlider, maxSlider
	private JLabel  minLabel, maxLabel

	public double minValue = 0
	public double maxValue = 0

	WeirdElectrodeSelector( Model model ) {
		super( model.headers.electrodeLabels )
		this.model = model

		remove( panel )
		setLayout( new BorderLayout() )
		add( panel, BorderLayout.CENTER )

		double min = calcMin()
		double max = calcMax()

		SwingBuilder ui = new SwingBuilder()

		sliderPanel = ui.panel() {
			flowLayout()
			label( "Min" )
			minLabel  = label()
			minSlider = slider( minimum: min, maximum: max, paintLabels: true, stateChanged: { setMinValue( minSlider.value ) } )
			maxSlider = slider( minimum: min, maximum: max, paintLabels: true, stateChanged: { setMaxValue( maxSlider.value ) } )
			maxLabel  = label()
			label( "Max" )
		}

		int oneTenth = (int)( model.minValues.size() / 20 )

		List<Double> allMinValues = model.minValues.values().sort()
		setMinValue( allMinValues[ oneTenth ], false )

		List<Double> allMaxValues = model.maxValues.values().sort()
		setMaxValue( allMaxValues[ allMaxValues.size() - 1 - oneTenth ], false )

		add( sliderPanel, BorderLayout.SOUTH )
		updateWeirdElectrodes()
	}

	public void setMinValue( double value, boolean update = true ) {
		minValue = value
		minSlider.value = value
		DecimalFormat format = new DecimalFormat( "0.00" )
		minLabel.text = format.format( value )
		if ( update ) {
			updateWeirdElectrodes()
		}
	}

	public void setMaxValue( double value, boolean update = true ) {
		maxValue = value
		maxSlider.value = value
		DecimalFormat format = new DecimalFormat( "0.00" )
		maxLabel.text = format.format( value )
		if ( update ) {
			updateWeirdElectrodes()
		}
	}

	protected void updateWeirdElectrodes() {
		List<String> nonDead = model.headers.electrodeLabels.keySet().toList() - deadElectrodes
		List<String> outsideRange = nonDead.findAll { label ->
			model.minValues[ label ] < minValue || model.maxValues[ label ] > maxValue
		}
		weirdElectrodes = outsideRange
		panel.repaint()
	}

	protected Double calcMin() {
		model.minValues.values().min()
	}

	protected Double calcMax() {
		model.maxValues.values().max()
	}

}
