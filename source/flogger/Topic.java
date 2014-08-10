package flogger;

/**
 * Project Flogger
 * 
 * Hosted at http://code.google.com/p/projectflogger/
 * 
 * <Appropriate LGPL blurb goes here.>
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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

	/**
	 * Outputs Topic's configuration in properties file syntax.
	 *
	 */
	void showConfig()
	{
		showConfig( "Flogger Config" );
		showConfig( "" );
		String name = getName();
		showConfig( name + "|topic=" + getDescription() );
		showConfig( name + "|match=" + getMatch() );
		showConfig( name + "|level=" + getLevel() );
		StringBuilder sb = new StringBuilder( name );
		sb.append( "|adapters=" );
		Iterator<Adapter> i = _adapters.iterator();
		if( i.hasNext() )
		{
			sb.append( i.next().getName() );
			
		}
		while( i.hasNext() )
		{
			sb.append( ", " );
			sb.append( i.next().getName() );
		}
		showConfig( sb.toString() );
		for( Adapter adapter : _adapters )
		{
			try
			{
				adapter.append( "" );
				adapter.showConfig();
			}
			catch( IOException e ) 
			{
				e.printStackTrace( System.err );
			}
			
		}
		showConfig( "" );
	}

	void showConfig( String message )
	{
		for( Adapter adapter : _adapters )
		{
			try 
			{
				adapter.append( message );
			} 
			catch( IOException e ) 
			{
				e.printStackTrace( System.err );
			}
		}
	}
}
