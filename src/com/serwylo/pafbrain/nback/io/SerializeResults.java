package com.serwylo.pafbrain.nback.io;

import java.util.ArrayList;

import com.serwylo.pafbrain.nback.core.Result;

public class SerializeResults 
{

	public static String serializeResultList( String columns, ArrayList<Result> results )
	{
		String[] splitColumns = columns.split( "," );
		return SerializeResults.serializeResultList( splitColumns, results );
	}
	
	public static String serializeResultList( String[] columns, ArrayList<Result> results )
	{
		StringBuffer buffer = new StringBuffer();
		
		for ( int i = 0; i < columns.length; i ++ )
		{
			if ( i != 0 ) buffer.append( ',' );
			buffer.append( columns[ i ] );
		}
		buffer.append( '\n' );
		
		for ( Result result : results )
		{
			buffer.append( SerializeResults.serializeResult( columns, result ) );
			buffer.append( '\n' );
		}
		return buffer.toString();
	}
	
	private static String serializeResult( String[] columns, Result result )
	{
		
		StringBuffer buffer = new StringBuffer();
		for ( int i = 0; i < columns.length; i ++ )
		{
			if ( i > 0 )
			{
				buffer.append( ',' );
			}
			
			String column = columns[ i ].toLowerCase();
			if ( column.equals( "time" ) )
			{
				buffer.append( result.time );
			}
			else if ( column.equals( "number" ) )
			{
				buffer.append( result.number );
			}
			else if ( column.equals( "timedout" ) )
			{
				buffer.append( result.timedOut );
			}
			else if ( column.equals( "timefromnumbershown" ) )
			{
				buffer.append( result.timeFromNumberShown );
			}
			else if ( column.equals( "timefromstartoftest" ) )
			{
				buffer.append( result.timeFromStartOfTest );
			}
			else if ( column.equals( "wassuccessfull" ) )
			{
				buffer.append( result.wasSuccessfull );
			}
			else if ( column.equals( "wastarget" ) )
			{
				buffer.append( result.wasTarget );
			}
		}
		return buffer.toString();
	}
	
}
