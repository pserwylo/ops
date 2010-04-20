import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.Timer;


public class NBack implements ActionListener
{

	public static final int ACTION_TICK = 1;
	public static final int ACTION_COMPLETE = 2;
	
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>( 1 );
	private ArrayList<Integer> history = new ArrayList<Integer>();
	private ArrayList<Result> results = new ArrayList<Result>();
	private boolean hasStarted = false;
	private boolean hasResultForThisTime = false;
	
	private int propN, propTotalNumbers, propTotalTime, propTimeBetweenNumbers;
	private float propTargetPercentage;
	private long propRandomSeed;
	private String propSaveDirectory;
	
	private int currentNumber;
	private boolean currentIsTarget;
	private long timeNumberShown;
	private long timeStartedTest;

	private Timer timer;
	private Random random;
	
	public NBack()
	{
	}
	
	public void submitResult( boolean isTarget )
	{
		if ( !this.hasResultForThisTime )
		{
			int timeFromStart = (int)( System.currentTimeMillis() - this.timeStartedTest );
			int timeFromNumberShown = (int)( System.currentTimeMillis() - this.timeNumberShown );
			this.results.add( new Result( currentNumber, isTarget == currentIsTarget, currentIsTarget, timeFromStart, timeFromNumberShown ) );
			this.hasResultForThisTime = true;
			this.timer.setInitialDelay( 0 );
			this.timer.restart();
		}
	}

	public boolean isCurrentlyTarget() { return this.currentIsTarget; }
	public int getCurrentNumber() { return this.currentNumber; }
	public void addActionListener( ActionListener listener ) { this.listeners.add( listener ); }
	
	public void saveResults( String participantId )
	{
		String path = this.propSaveDirectory + File.separatorChar + "nback." + participantId + ".csv";
		System.out.println( "\nSaving file to: " + path );
		try
		{
			BufferedWriter output = new BufferedWriter( new FileWriter( path ) );
			output.write
			( 
				"Number," +
				"Target?," +
				"Successfull?," +
				"Response," +
				"Time\n" 
			);
			for ( int i = 0; i < this.results.size(); i ++ )
			{
				Result result = this.results.get( i );
				output.write
				( 
					result.number + "," + 
					result.wasTarget + "," + 
					result.wasSuccessfull + "," + 
					result.timedOut + "," + 
					result.timeFromNumberShown + "\n" 
				);
			}
			output.close();
		}
		catch ( IOException ioe )
		{
			JOptionPane.showMessageDialog( null, "Error saving results to file '" + path + "'\n\n" + ioe.getMessage() );
		}
	}
	
	public void start()
	{
		if ( !this.hasStarted )
		{
			this.readProperties();
			random = new Random( this.propRandomSeed );
			this.hasStarted = true;
			this.timer = new Timer( this.propTimeBetweenNumbers, this );
			this.timer.setRepeats( true );
			this.timeStartedTest = System.currentTimeMillis();
			this.timer.start();
		}
	}

	@Override
	public void actionPerformed( ActionEvent e ) 
	{
		// If the user didn't submit an answer, then force an answer from them...
		if ( !hasResultForThisTime && history.size() > 0 )
		{
			int timeFromStart = (int)( System.currentTimeMillis() - this.timeStartedTest );
			int timeFromNumberShown = (int)( System.currentTimeMillis() - this.timeNumberShown );
			results.add( new Result( currentNumber, false, currentIsTarget, true, timeFromStart, timeFromNumberShown ) );
		}
		else
		{
			hasResultForThisTime = false;
		}
	
		// When we are done, stop the timer, alert the listeners, and then return.
		if ( this.history.size() >= this.propTotalNumbers )
		{
			this.dispatchEvent( ACTION_COMPLETE );
			this.timer.stop();
			return;
		}
		
		int targetNumber = -1;
		boolean makeTarget = false;
		int number;
		
		// Cannot create a target unless there is enough items in the history to go back that far...
		if ( history.size() > this.propN )
		{
			makeTarget = this.random.nextFloat() < this.propTargetPercentage;
			targetNumber = history.get( history.size() - this.propN );
		}
		
		if ( makeTarget )
		{
			number = targetNumber;
		}
		else
		{
			do 
			{
				number = (int)( ( this.random.nextFloat() * 10000 ) / 1000 );
			} while ( number == targetNumber || number == this.currentNumber );
		}
		
		this.currentIsTarget = makeTarget;
		this.currentNumber = number;
		this.timeNumberShown = System.currentTimeMillis();
		this.history.add( number );
		
		System.out.print( " " + number + ( makeTarget ? "T" : "" ) );
		
		// Alert the GUI or whoever else cares that we have just ticked over...
		this.dispatchEvent( ACTION_TICK );
	}
	
	/**
	 * Send an event with id of actionId to any listeners.
	 * @param actionId
	 */
	private void dispatchEvent( int actionId )
	{
		for ( ActionListener listener : this.listeners )
		{
			listener.actionPerformed( new ActionEvent( this, actionId, "" ) );
		}	
	}
	
	/**
	 * Check the nback.properties file and assign the properties from it to the appropriate member variables.
	 */
	private void readProperties()
	{
		Properties properties = new Properties();
		try
		{
			properties.load( new FileReader( "nback.properties" ) );
		}
		catch( IOException ioe )
		{
			JOptionPane.showMessageDialog( null, "Error reading configuration file 'nback.properties': \n" + ioe.getMessage() );
		}

		String n = properties.getProperty( "n" );
		String totalNumbers = properties.getProperty( "totalNumbers" );
		String totalTime = properties.getProperty( "totalTime" );
		String targetPercentage = properties.getProperty( "targetPercentage" );
		String timeBetweenNumbers = properties.getProperty( "timeBetweenNumbers" );
		String randomSeed = properties.getProperty( "randomSeed" );
		String saveDirectory = properties.getProperty( "saveDirectory" );

		this.propN = ( n != null ) ? Integer.parseInt( n ) : 3;
		this.propTotalNumbers = ( totalNumbers != null ) ? Integer.parseInt( totalNumbers ) : 20;
		this.propTotalTime = ( totalTime != null ) ? Integer.parseInt( properties.getProperty( "totalTime" ) ) : 0;
		this.propTargetPercentage = ( targetPercentage != null ) ? ( Float.parseFloat( targetPercentage ) / 100 ) : 0.10f;
		this.propTimeBetweenNumbers = ( timeBetweenNumbers != null ) ? Integer.parseInt( timeBetweenNumbers ) : 1000;
		this.propRandomSeed = ( randomSeed != null ) ? Integer.parseInt( randomSeed ) : System.currentTimeMillis();
		this.propSaveDirectory = ( saveDirectory != null ) ? saveDirectory : System.getProperty( "user.home" );
		
		if ( this.propTotalTime != 0 )
		{
			this.propTotalNumbers = this.propTotalTime * 1000 / this.propTimeBetweenNumbers;
		}

		System.out.println( "Properties:" );
		System.out.println( "  n = " + this.propN );
		System.out.println( "  totalNumbers = " + this.propTotalNumbers );
		System.out.println( "  propTargetPercentage = " + this.propTargetPercentage );
		System.out.println( "  timeBetweenNumbers = " + this.propTimeBetweenNumbers );
	}
		
}
