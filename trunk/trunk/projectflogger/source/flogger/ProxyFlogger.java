package flogger;

/**
 * Project Flogger
 * 
 * Hosted at http://code.google.com/p/projectflogger/
 * 
 * Contact: projectflogger@gmail.com
 * 
 * <Appropriate LGPL blurb goes here.>
 */

/**
 * Does nothing. Implementation of the Null Object design pattern. Permits JVM to optimize
 * away all calls to parent Flogger. (true?)
 * 
 */

class 
	ProxyFlogger
{
	
	public ProxyFlogger()
	{
	}
	
	public synchronized void enter( Flogger flogger, Object message )
	{
		// Does nothing
	}
	
	public synchronized void exit( Flogger flogger, Object message )
	{
		// Does nothing
	}

	public synchronized void log( Flogger flogger )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, Object message )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, String format, Object a )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, String format, Object a, Object b )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, String format, Object a, Object b, Object c )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, String format, Object... a )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, Object message )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, String format, Object a )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, String format, Object a, Object b )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, String format, Object a, Object b, Object c )
	{
		// Does nothing
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, String format, Object... a )
	{
		// Does nothing
	}	
}

// End of ProxyFlogger
