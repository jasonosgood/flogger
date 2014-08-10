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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public abstract class 
	Adapter 
{

	public abstract void append( String message ) throws IOException;
	
	/**
	 * Override this method to support additional properties added by subclasses. 
	 * Be sure to first call super.setProperties( props );
	 */
	public void setProperties( HashMap<String,String> props )
	{
		if( props == null )
		{
			throw new NullParameterException( "props" );
		}
		if( props.containsKey( "adapter" ))
		{
			String temp = props.get( "adapter" ).trim();
			if( temp.length() > 0 )
			{
				setDescription( temp );
			}
		}
		if( props.containsKey( "level" ))
		{
			String temp = props.get( "level" ).trim();
			if( temp.length() > 0 )
			{
				Level level = Level.getLevel( temp );
				setLevel( level );
			}
		}
		
		if( props.containsKey( "stacktrace" ))
		{
			String temp = props.get( "stacktrace" ).trim();
			if( temp.length() > 0 )
			{
				boolean stackTrace = Boolean.parseBoolean( temp );
				setStackTrace( stackTrace );
			}
		}
		if( props.containsKey( "layout" ))
		{
			String temp = props.get( "layout" ).trim();
			if( temp.length() > 0 )
			{
				setLayout( temp );
			}
		}
	}

	private String _name;
	
	public void setName( String name )
	{
		_name = name;
	}
	
	public String getName()
	{
		return _name;
	}
	
	private String _decription;
	
	public void setDescription( String description )
	{
		if( description == null )
		{
			throw new NullParameterException( "description" );
		}
		_decription = description;
	}
	
	public String getDescription() 
	{
		return _decription;
	}
	
	private Level _level = Level.ALL;
	
	public void setLevel( Level level ) 
	{
		if( level == null )
		{
			throw new NullParameterException( "level" );
		}
		_level = level; 
	}

	public Level getLevel() 
	{ 
		return _level; 
	}

	private boolean _stackTrace = true;
	
	public synchronized void setStackTrace( boolean stackTrace ) 
	{ 
		_stackTrace = stackTrace; 
	}
	
	public synchronized boolean getStackTrace() 
	{ 
		return _stackTrace; 
	}
	
	public final static String DEFAULT_LAYOUT = "{timestamp}tD {timestamp}tT|{symbol}s|{level}s|{thread}s|{shortclass}s|{message}s";
//	public final static String DEFAULT_DAILY_DIRECTORY = ".";
//	public final static String DEFAULT_DAILY_PATTERN = "yyyy-MM-dd";
//	public final static String DEFAULT_DAILY_SUFFIX = "log";
	

	private String _layout = DEFAULT_LAYOUT;
	
	// TODO: Validate layout (syntax)
	public void setLayout( String layout )
	{
		if( layout == null )
		{
			throw new NullParameterException( "layout" );
		}
		_layout = layout;
	}
	
	public String getLayout() 
	{
		return _layout;
	}
	
	private String replace( String layout, String param, int nth ) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append( '%' );
		sb.append( nth++ );
		sb.append( '$' );
		return layout.replace( param, sb.toString() );
	}
	
	private ArrayList<Object> _values = new ArrayList<Object>();
	
	protected synchronized void event( String symbol, Flogger logger, Throwable throwable, String message )
	{
		// TODO: Is this check necessary?
		// Bail out if logging is disabled
		if( logger.getLevel().getValue() > getLevel().getValue() )
		{
			return;
		}

		String layout = getLayout();
		_values.clear();
		int nth = 1;

		if( layout.contains( "{timestamp}" ))
		{
			layout = replace( layout, "{timestamp}", nth++ );
			_values.add( new Date() );
		}
		if( layout.contains( "{thread}" ))
		{
			layout = replace( layout, "{thread}", nth++ );
			String thread = Thread.currentThread().getName();
			_values.add( thread );
		}
		if( layout.contains( "{level}" ))
		{
			layout = replace( layout, "{level}", nth++ );
			String level = logger.getLevel().toString();
			_values.add( level );
		}
		if( layout.contains( "{longclass}" ))
		{
			layout = replace( layout, "{longclass}", nth++ );
			String longClass = logger.getLongClass();
			_values.add( longClass );
		}
		if( layout.contains( "{shortclass}" ))
		{
			layout = replace( layout, "{shortclass}", nth++ );
			String shortClass = logger.getShortClass();
			_values.add( shortClass );
		}
		if( layout.contains( "{message}" ))
		{
			layout = replace( layout, "{message}", nth++ );
			_values.add( message == null ? "" : message );
		}
		if( layout.contains( "{symbol}" ))
		{
			layout = replace( layout, "{symbol}", nth++ );
			_values.add( symbol );
		}
		
		Object args[] = _values.toArray();
		String result = String.format( layout, args );

		try
		{
			append( result );
	
			if( getStackTrace() && throwable != null )
			{
				StringWriter writer = new StringWriter();
				PrintWriter printer = new PrintWriter( writer );
				throwable.printStackTrace( printer );
				append( writer.toString() );
			}
		}
		catch( IOException e )
		{
			e.printStackTrace( System.err );
		}
	}
	
	public void showConfig()
		throws IOException
	{
		String name = getName();
		append( name + "|adapter=" + getDescription() );
		append( name + "|class=" + getClass().getName() );
		append( name + "|level=" + getLevel() );
		append( name + "|stacktrace=" + getStackTrace() );
		append( name + "|layout=" + getLayout() );
	}
	
}
