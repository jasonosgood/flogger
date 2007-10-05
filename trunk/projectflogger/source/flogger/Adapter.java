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
		if( props.containsKey( "template" ))
		{
			String temp = props.get( "template" ).trim();
			if( temp.length() > 0 )
			{
				setTemplate( temp );
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
	
	public final static String DEFAULT_TEMPLATE = "{timestamp}tD {timestamp}tT|{symbol}s|{level}s|{thread}s|{shortclass}s|{message}s";
//	public final static String DEFAULT_DAILY_DIRECTORY = ".";
//	public final static String DEFAULT_DAILY_PATTERN = "yyyy-MM-dd";
//	public final static String DEFAULT_DAILY_SUFFIX = "log";
	

	private String _template = DEFAULT_TEMPLATE;
	
	// TODO: Validate template (syntax)
	public void setTemplate( String template )
	{
		if( template == null )
		{
			throw new NullParameterException( "template" );
		}
		_template = template;
	}
	
	public String getTemplate() 
	{
		return _template;
	}
	
	private String replace( String template, String param, int nth ) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append( '%' );
		sb.append( nth++ );
		sb.append( '$' );
		return template.replace( param, sb.toString() );
	}
	
	private ArrayList<Object> _values = new ArrayList<Object>();
	
	protected synchronized void event( String symbol, Flogger logger, Throwable throwable, String message )
	{
		// Bail out if logging is disabled
		if( logger.getLevel().getValue() > getLevel().getValue() )
		{
			return;
		}

		String template = getTemplate();
		_values.clear();
		int nth = 1;

		if( template.contains( "{timestamp}" ))
		{
			template = replace( template, "{timestamp}", nth++ );
			_values.add( new Date() );
		}
		if( template.contains( "{thread}" ))
		{
			template = replace( template, "{thread}", nth++ );
			String thread = Thread.currentThread().getName();
			_values.add( thread );
		}
		if( template.contains( "{level}" ))
		{
			template = replace( template, "{level}", nth++ );
			String level = logger.getLevel().toString();
			_values.add( level );
		}
		if( template.contains( "{longclass}" ))
		{
			template = replace( template, "{longclass}", nth++ );
			String longClass = logger.getLongClass();
			_values.add( longClass );
		}
		if( template.contains( "{shortclass}" ))
		{
			template = replace( template, "{shortclass}", nth++ );
			String shortClass = logger.getShortClass();
			_values.add( shortClass );
		}
		if( template.contains( "{message}" ))
		{
			template = replace( template, "{message}", nth++ );
			_values.add( message == null ? "" : message );
		}
		if( template.contains( "{symbol}" ))
		{
			template = replace( template, "{symbol}", nth++ );
			_values.add( symbol );
		}
		
		Object args[] = _values.toArray();
		String result = String.format( template, args );

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
	
}
