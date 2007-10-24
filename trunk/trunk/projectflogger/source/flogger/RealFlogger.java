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

	public synchronized void enter( Flogger flogger, Object message )
	{
		if( message != null )
		{
			message = message.toString();
		}
		Topic topic = flogger.getTopic();
		topic.enter( flogger, (String) message );
	}
	
	public synchronized void exit( Flogger flogger, Object message )
	{
		if( message != null )
		{
			message = message.toString();
		}
		Topic topic = flogger.getTopic();
		topic.exit( flogger, (String) message );
	}

	public synchronized void log( Flogger flogger )
	{
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, null );
	}
	
	public synchronized void log( Flogger flogger, Object message )
	{
		if( message != null )
		{
			message = message.toString();
		}
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, (String) message );
	}
	
	public synchronized void log( Flogger flogger, String template, Object a )
	{
		checkTemplate( template );
		String message = String.format( template, a );
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, message );
	}
	
	public synchronized void log( Flogger flogger, String template, Object a, Object b )
	{
		checkTemplate( template );
		String message = String.format( template, a, b );
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, message );
	}
	
	public synchronized void log( Flogger flogger, String template, Object a, Object b, Object c )
	{
		checkTemplate( template );
		String message = String.format( template, a, b, c );
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, message );
	}
	
	public synchronized void log( Flogger flogger, String template, Object... a )
	{
		checkTemplate( template );
		String message = String.format( template, a );
		Topic topic = flogger.getTopic();
		topic.log( flogger, null, message );
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable )
	{
		checkThrowable( throwable );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, null );
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, Object message )
	{
		checkThrowable( throwable );
		if( message != null )
		{
			message = message.toString();
		}
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, (String) message );
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, String template, Object a )
	{
		checkThrowable( throwable );
		checkTemplate( template );
		String message = String.format( template, a );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, message );
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, String template, Object a, Object b )
	{
		checkThrowable( throwable );
		checkTemplate( template );
		String message = String.format( template, a, b );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, message );
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, String template, Object a, Object b, Object c )
	{
		checkThrowable( throwable );
		checkTemplate( template );
		String message = String.format( template, a, b, c );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, message );
	}
	
	public synchronized void log( Flogger flogger, Throwable throwable, String template, Object... a )
	{
		checkThrowable( throwable );
		checkTemplate( template );
		String message = String.format( template, a );
		Topic topic = flogger.getTopic();
		topic.log( flogger, throwable, message );
	}
	
	private final void checkTemplate( String template )
	{
		if( template == null )
		{
			throw new NullParameterException( "template" );
		}
	}
	
	private final void checkThrowable( Throwable throwable )
	{
		if( throwable == null )
		{
			throw new NullParameterException( "throwable" );
		}
	}
}
