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


public class Flogger
{
	public static Flogger newFlogger( Level level )
	{
		Flogger log = newFlogger( null, Topic.DEFAULT, level );
		return log;
	}

	public static Flogger newFlogger( String topicName, Level level )
	{
		Flogger log = newFlogger( null, topicName, level );
		return log;
	}
	
	public static Flogger newFlogger( Class clazz, String topicName, Level level )
	{
		Flogger log = new Flogger( clazz, topicName, level );
		return log;
	}
	
	public static final Flogger FATAL = newFlogger( Flogger.class, Topic.DEFAULT, Level.FATAL ); 
	public static final Flogger ERROR = newFlogger( Flogger.class, Topic.DEFAULT, Level.ERROR ); 
	public static final Flogger WARN  = newFlogger( Flogger.class, Topic.DEFAULT, Level.WARN ); 
	public static final Flogger INFO  = newFlogger( Flogger.class, Topic.DEFAULT, Level.INFO ); 
	public static final Flogger DEBUG = newFlogger( Flogger.class, Topic.DEFAULT, Level.DEBUG ); 
	public static final Flogger TRACE = newFlogger( Flogger.class, Topic.DEFAULT, Level.TRACE ); 
	public static final Flogger ALL   = newFlogger( Flogger.class, Topic.DEFAULT, Level.ALL ); 
	
	public Flogger( Class clazz, String topicName, Level level )
	{
		setTopicName( topicName );
		setLevel( level );
		
		if( clazz == null )
		{
			Exception e = new Exception();
			StackTraceElement[] elements = e.getStackTrace();
			String loggerClazz = getClass().getName();
			loop:
			for( StackTraceElement element : elements )
			{
				if( element.getClassName().equals( loggerClazz )) continue;
				_longClass = element.getClassName();
				int nth = _longClass.lastIndexOf( '.' );
				_shortClass = _longClass.substring( nth + 1 );
				break loop;
			}
		}
		else
		{
			_longClass = clazz.getName();
			_shortClass = clazz.getSimpleName();
		}
	}

	private String _topicName = Topic.DEFAULT;
	
	/**
	 * Setting a topic name clears any topic value.
	 * 
	 * @param topicName
	 */
	public synchronized void setTopicName( String topicName )
	{
		if( topicName == null )
		{
			throw new NullParameterException( "topicName" );
		}
		_topicName = topicName;
		_topic = null;
		clearFloggerProxy();
	}
	
	public synchronized String getTopicName()
	{
		return _topicName;
	}
	
	private Topic _topic;
	
	public synchronized void setTopic( Topic topic )
	{
		if( topic == null )
		{
			throw new NullParameterException( "topic" );
		}

		_topic = topic;
		clearFloggerProxy();
	}

	public synchronized Topic getTopic() 
	{
		if( _topic == null )
		{
			Config config = Config.getInstance();
			String topicName = getTopicName();
			_topic = config.getTopic( topicName );
			if( config.getShowConfig() )
			{
				_topic.showConfig();
			}
		}
		return _topic; 
	}
	
	private Level _level = Level.ALL;
	
	private synchronized void setLevel( Level level ) 
	{
		if( level == null )
		{
			throw new NullParameterException( "level" );
		}
		_level = level;
		clearFloggerProxy();
	}
	
	public synchronized Level getLevel() 
	{ 
		return _level; 
	}
	
	private String _longClass;
	
	public String getLongClass()
	{
		return _longClass;
	}
	
	private String _shortClass;
	
	public String getShortClass()
	{
		return _shortClass;
	}
	
	protected synchronized boolean disabled()
	{
		if( Config.getInstance().disabled() )
		{
			return true;
		}
		boolean higherLevel = getLevel().getValue() > getTopic().getLevel().getValue();
		return higherLevel;
	}
	
	private ProxyFlogger _floggerProxy = null;
	private synchronized ProxyFlogger getFloggerProxy()
	{
		if( _floggerProxy == null )
		{
			if( disabled() )
			{
				_floggerProxy = new ProxyFlogger();
			}
			else
			{
				_floggerProxy = new RealFlogger();
			}
		}
		return _floggerProxy;
	}
	
	synchronized void clearFloggerProxy()
	{
		_floggerProxy = null;
	}
	
	public void enter( Object message )
	{
		getFloggerProxy().enter( this, message );
	}
	
	public void exit( Object message )
	{
		getFloggerProxy().exit( this, (String) message );
	}

	public void log()
	{
		getFloggerProxy().log( this );
	}
	
	public void log( Object message )
	{
		getFloggerProxy().log( this, message );
	}
	
	public void log( String template, Object a )
	{
		getFloggerProxy().log( this, template, a );
	}
	
	public void log( String template, Object a, Object b )
	{
		getFloggerProxy().log( this, template, a, b );
	}
	
	public void log( String template, Object a, Object b, Object c )
	{
		getFloggerProxy().log( this, template, a, b, c );
	}
	
	public void log( String template, Object... a )
	{
		getFloggerProxy().log( this, template, a );
	}
	
	public void log( Throwable throwable )
	{
		getFloggerProxy().log( this, throwable );
	}
	
	public void log( Throwable throwable, Object message )
	{
		getFloggerProxy().log( this, throwable, message );
	}
	
	public void log( Throwable throwable, String template, Object a )
	{
		getFloggerProxy().log( this, throwable, template, a );
	}
	
	public void log( Throwable throwable, String template, Object a, Object b )
	{
		getFloggerProxy().log( this, throwable, template, a, b );
	}
	
	public void log( Throwable throwable, String template, Object a, Object b, Object c )
	{
		getFloggerProxy().log( this, throwable, template, a, b, c );
	}
	
	public void log( Throwable throwable, String template, Object... a )
	{
		getFloggerProxy().log( this, throwable, template, a );
	}
}

// End of Flogger.java