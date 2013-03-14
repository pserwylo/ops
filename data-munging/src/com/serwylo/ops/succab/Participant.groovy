package com.serwylo.ops.succab

class Participant
{

	private String participantId;
	private String participantIdUpper;

	public List<String> missingData;

	private static final String SPREADSHEET_PREFIX = "com.serwylo.ops.succab-"
	private static final String SOURCE_RT1 = "RT1T2"
	private static final String SOURCE_RT2 = "RT2T2"
	private static final String SOURCE_SRT1 = "SRT2"
	private static final String SOURCE_SWT1 = "SWT2"
	private static final String SOURCE_DR = "DRT2"

	Participant( String id )
	{
		participantId      = id
		participantIdUpper = id.toUpperCase()
	}

	String getId()
	{
		participantId
	}

	protected String getIdUpper()
	{
		participantIdUpper
	}

	String getOdsFilename()
	{
		"/tmp/" + SPREADSHEET_PREFIX + "$id-t2.ods"
	}

	String getXlsFilename()
	{
		"/tmp/" + SPREADSHEET_PREFIX + "$id-t2.xls"
	}

	String getRt1Source()
	{
		idUpper + SOURCE_RT1 + ".ASC"
	}

	String getRt2Source()
	{
		idUpper + SOURCE_RT2 + ".ASC"
	}

	String getSrt1Source()
	{
		idUpper + SOURCE_SRT1 + ".ASC"
	}

	String getSwt1Source()
	{
		idUpper + SOURCE_SWT1 + ".ASC"
	}

	String getDrSource()
	{
		idUpper + SOURCE_DR + ".ASC"
	}

}
