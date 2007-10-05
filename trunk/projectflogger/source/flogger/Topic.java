package flogger;

/**
 * Project Flogger
 * 
 * Hosted at http://code.google.com/p/projectflogger/
 * 
 * <Appropriate LGPL blurb goes here.>
 */

import java.util.ArrayList;

public class 
	Topic 
{
	public final static String DEFAULT = "default";

	public Topic() {}
	
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

	private String _match = DEFAULT;

	public void setMatch( String match )
	{
		if( match == null )
		{
			throw new NullParameterException( "match" );
		}
		_match = match;
	}
	
	public String getMatch() 
	{ 
		return _match; 
	}
	
	private ArrayList<Adapter> _adapters = new ArrayList<Adapter>();
	
	public void addAdapter( Adapter adapter )
	{
		if( adapter == null )
		{
			throw new NullParameterException( "adapter" );
		}
		_adapters.add( adapter );
	}
	
	public void enter( Flogger logger, String message )
	{
		event( ">>>", logger, null, message );
	}
	
	public void exit( Flogger logger, String message )
	{
		event( "<<<", logger, null, message );
	}

	public void log( Flogger logger, Throwable t, String message) 
	{
		event( "   ", logger, t, message );
	}

	protected void event( String symbol, Flogger logger, Throwable throwable, String message )
	{
		for( Adapter adapter : _adapters )
		{
			adapter.event( symbol, logger, throwable, message );
		}
	}

}
