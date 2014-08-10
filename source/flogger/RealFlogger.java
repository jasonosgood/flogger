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


class 
	RealFlogger
extends
	ProxyFlogger
{
	public RealFlogger()
	{
	}

	public void enter( Flogger flogger, Object message )
	{
		if( message != null )
		{
			message = message.toString();
		}
		Topic topic = flogger.getTopic();
		topic.enter( flogger, (String) message );
	}
	
	public void exit( Flogger flogger, Object message )
	{
		if( message != null )
		{
			message = message.toString();
		}
		Topic topic = flogger.getTopic();
		topic.exit( flogger, (String) message );
	}

	public void log( Flogger flogger )
	{
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, null );
	}
	
	public void log( Flogger flogger, Object message )
	{
		if( message != null )
		{
			message = message.toString();
		}
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, (String) message );
	}
	
	public void log( Flogger flogger, String format, Object a )
	{
		checkFormat( format );
		String message = format( flogger, format, a );
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, message );
	}
	
	public void log( Flogger flogger, String format, Object a, Object b )
	{
		checkFormat( format );
		String message = format( flogger, format, a, b );
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, message );
	}
	
	public void log( Flogger flogger, String format, Object a, Object b, Object c )
	{
		checkFormat( format );
		String message = format( flogger, format, a, b, c );
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, message );
	}
	
	public void log( Flogger flogger, String format, Object... a )
	{
		checkFormat( format );
		String message = format( flogger, format, a );
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, message );
	}
	
	public void log( Flogger flogger, Throwable throwable )
	{
		checkThrowable( throwable );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, null );
	}
	
	public void log( Flogger flogger, Throwable throwable, Object message )
	{
		checkThrowable( throwable );
		if( message != null )
		{
			message = message.toString();
		}
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, (String) message );
	}
	
	public void log( Flogger flogger, Throwable throwable, String format, Object a )
	{
		checkThrowable( throwable );
		checkFormat( format );
		String message = format( flogger, format, a );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, message );
	}
	
	public void log( Flogger flogger, Throwable throwable, String format, Object a, Object b )
	{
		checkThrowable( throwable );
		checkFormat( format );
		String message = format( flogger, format, a, b );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, message );
	}
	
	public void log( Flogger flogger, Throwable throwable, String format, Object a, Object b, Object c )
	{
		checkThrowable( throwable );
		checkFormat( format );
		String message = format( flogger, format, a, b, c );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, message );
	}
	
	public void log( Flogger flogger, Throwable throwable, String format, Object... a )
	{
		checkThrowable( throwable );
		checkFormat( format );
		String message = format( flogger, format, a );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, message );
	}
	
	private final void checkFormat( String format )
	{
		if( format == null )
		{
			throw new NullParameterException( "format" );
		}
	}
	
	private final void checkThrowable( Throwable throwable )
	{
		if( throwable == null )
		{
			throw new NullParameterException( "throwable" );
		}
	}
	
	/* Utility method to catch, report, and swallow any formatting errors (exceptions). Ensures
	 * that Flogger doesn't blow up the application.
	 * 
	 */
	private String format( Flogger flogger, String format, Object... args )
	{
		String result = null;
		try
		{
			result = String.format( format, args );
		}
		catch( Exception e )
		{
			// Add formatting error to the log stream
			StringBuilder sb = new StringBuilder();
			sb.append( '"' );
			sb.append( format );
			sb.append( '"' );
			for( Object a : args )
			{
				sb.append( ", " );
				if( a == null )
				{
					sb.append( "null" );
				}
				else
				{
					sb.append( a.toString() );
				}
			}
			result = sb.toString();
			FloggerException fe = new FloggerException( result, e );
			log( flogger, fe );
		}
		return result;
	}
}
