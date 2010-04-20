
public class Result 
{

	public long time;
	public int timeFromStartOfTest;
	public int timeFromNumberShown;
	public int number;
	public boolean wasSuccessfull;
	public boolean wasTarget;
	public boolean timedOut;
	
	public Result( int number, boolean wasSuccessfull, boolean wasTarget, int timeFromStartOfTest, int timeFromNumberShown )
	{
		this( number, wasSuccessfull, wasTarget, false, timeFromStartOfTest, timeFromNumberShown );
	}
	
	public Result( int number, boolean wasSuccessfull, boolean wasTarget, boolean timedOut, int timeFromStartOfTest, int timeFromNumberShown )
	{
		this.time = System.currentTimeMillis();
		this.number = number;
		this.wasSuccessfull = wasSuccessfull;
		this.wasTarget = wasTarget;
		this.timedOut = timedOut;
		this.timeFromStartOfTest = timeFromStartOfTest;
		this.timeFromNumberShown = timeFromNumberShown;
	}
	
}
