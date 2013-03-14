package com.serwylo.ops.succab

import com.serwylo.uno.spreadsheet.CsvOptions
import com.serwylo.uno.spreadsheet.SpreadsheetConnector
import com.sun.star.sheet.XSpreadsheet
import com.sun.star.sheet.XSpreadsheetDocument
import com.sun.star.table.XCellRange

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class ParticipantDataEnterer
{

	private SpreadsheetConnector connector
	private XSpreadsheetDocument spreadsheet

	private String templateFilename = ""
	private final Participant participant;

	ParticipantDataEnterer( SpreadsheetConnector connector, Participant participant ) {
		this.connector = connector
		this.participant = participant
		this.templateFilename = "/home/pete/code/ops/data-munging/com.serwylo.ops.succab/template.ods"
	}

	void enterData() {
		if ( !copyTemplate() ) {
			println "Skipping: $participant.id (already exists)"
			return
		}

		println "Entering: $participant.id"
		loadDocument()
		long modifiedDate = 0

		try {
			List<List<String>> values = parseSource( participant.rt1Source )
			copyValuesToSheet( values, "RT1", 0, 2 )
			modifiedDate = new File( participant.getRt1Source() ).lastModified();
		}
		catch( IOException e )
		{
			//
		}

		try {
			List<List<String>> values = parseSource( participant.rt2Source );
			this.copyValuesToSheet( values, "RT2", 0, 2 );
			modifiedDate = new File( participant.getRt2Source() ).lastModified();
		}
		catch( IOException e )
		{
			//
		}

		try {
			List<List<String>> values = parseSource( participant.srt1Source );
			this.copyValuesToSheet( values, "Simple RM", 0, 2 );
			modifiedDate = new File( participant.getSrt1Source() ).lastModified();
		}
		catch( IOException e )
		{
			//
		}

		try {
			List<List<String>> values = parseSource( participant.swt1Source );
			this.copyValuesToSheet( values, "Spatial WM", 0, 2 );
			modifiedDate = new File( participant.getSwt1Source() ).lastModified();
		}
		catch ( IOException e )
		{
			//
		}

		try {
			List<List<String>> values = parseSource( participant.drSource );
			this.copyValuesToSheet( values, "Delayed RM", 0, 2 );
			modifiedDate = new File( participant.drSource ).lastModified();
		}
		catch( IOException e )
		{
			//
		}

		if ( modifiedDate > 0 )
		{
			List<List<String>> modifiedValues = [];
			List<String> row = [];
			modifiedValues.add( row );
			row.add( "Date saved" );

			Date date = new Date( modifiedDate );
			DateFormat format = new SimpleDateFormat( "yyyy/MM/dd" );
			
			row.add( format.format( date ) );
			copyValuesToSheet( modifiedValues, "Summary", 0, 5 );
		}

		this.saveDocument();
		this.closeDocument();
	}

	/**
	 * This will save the file as a .xls file.
	 */
	private void saveDocument()
	{
		/*PropertyValue overwrite = new PropertyValue();
		overwrite.Name = "Overwrite";
		overwrite.Value = true;

		PropertyValue filterName = new PropertyValue();
		filterName.Name = "FilterName";
		filterName.Value = "MS Excel 97";

		PropertyValue[] properties = [ overwrite, filterName ]

		XStorable storable = UnoRuntime.queryInterface( XStorable.class, component );
		File odsFile = new File( this.participant.getOdsFilename() );
		File xlsFile = new File( this.participant.getXlsFilename() );
		String url = "file:///" + xlsFile.getAbsolutePath().replace( '\\', '/' );

		try
		{
			// System.out.print( "Saving to '" + url + "'..." );
			storable.storeAsURL( url, properties );
			// System.out.println( "Done." );
		}
		catch( com.sun.star.io.IOException e )
		{
			System.err.println( "Error saving xls document '" + participant.getXlsFilename() + "'." );
			System.err.println( e.getMessage() );
			System.exit( 1 );
		}

		odsFile.delete();*/
	}

	private boolean copyTemplate()
	{
		File template = new File( templateFilename );
		File newFile = new File( participant.getOdsFilename() );
		File newXlsFile = new File( participant.getXlsFilename() );

		if ( !template.exists() )
		{
			System.err.println( "Could not find template '" + templateFilename + "'." );
			System.exit( 1 );
		}

		if ( newFile.exists())
		{
			System.err.println( "File '" + newFile.getName() + "' already exists. We will not overwrite it." );
			return false;
		}

		if ( newXlsFile.exists() )
		{
			System.err.println( "File '" + newXlsFile.getName() + "' already exists. We will not overwrite it." );
			return false;
		}

		try
		{
			// System.out.println( "Copying '" + templateFilename + "' to '" + newFilename + "'..." );
			FileInputStream reader = new FileInputStream( template );
			FileOutputStream writer = new FileOutputStream( newFile );
			byte[] buffer = new byte[4096];
			int readLength = reader.read( buffer );
			while( readLength > 0 )
			{
				writer.write( buffer, 0, readLength );
				readLength = reader.read( buffer );
			}
			reader.close();
			writer.close();
			// System.out.println( "Copied '" + templateFilename + "' to '" + newFilename + "'." );
		}
		catch ( IOException e )
		{
			System.err.println( "Error copying template '" + templateFilename + "' to '" + newFile.getName() + "'." );
			System.err.println( e.getMessage() );
			System.exit( 1 );
		}

		return true;
	}

	private void closeDocument()
	{
		// Closing the converted document. Use XCloseable.clsoe if the
		// interface is supported, otherwise use XComponent.dispose
		/*XCloseable xCloseable = UnoRuntime.queryInterface( XCloseable.class, spreadsheet );

		if ( xCloseable != null )
		{
			try
			{
				xCloseable.close(false);
			} catch ( CloseVetoException e )
			{
				System.err.println( "Cannot close document." );
				System.err.println( e.getMessage() );
			}
		}
		else
		{
			println "Cannot close document at all..."
			// component.dispose();
		}*/

	}

	private void loadDocument()
	{
		String url = "file://" + new File( participant.odsFilename ).canonicalPath
		this.spreadsheet = connector.open( url )
	}

	private void copyValuesToSheet( List<List<String>> values, String sheetName, int cellX, int cellY )
	{
		XSpreadsheet sheet = spreadsheet.sheets[ sheetName ]
		sheet.getCellRangeByPosition( cellX, values[0].size(), cellY, values.size() ).formulas = values
	}

	public List<List<String>> parseSource( String sourceFile ) throws IOException
	{
		String path = new File( sourceFile ).absolutePath
		CsvOptions options = new CsvOptions( fieldDelimiters: " ", startLine: 1 )
		XSpreadsheetDocument doc = connector.open( path, options )
		XCellRange range = doc.sheets[ 0 ]["A1:B100"]

		List<List<String>> values = [];
		// System.out.println( "Reading '" + new File( sourceFile ).getAbsolutePath() + "'..." );
		BufferedReader input = new BufferedReader( new FileReader( sourceFile ) );
		String line = input.readLine();
		while ( line != null )
		{
			line = line.trim();
			if ( line.length() > 0 )
			{
				String[] parts = line.split( " " );
				List<String> list = [];
				Collections.addAll( list, parts );
				values.add( list );
			}
			line = input.readLine();
		}
		return values;
	}

}
