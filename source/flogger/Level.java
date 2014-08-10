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

import java.util.HashMap;

public class 
	Level
{
	private int _value = 0;
	public int getValue() { return _value; }
	
	private String _label = null;
	public String getLabel() { return _label; }
	public String toString() { return _label; }
	
	public Level( int value, String label )
	{
		_value = value;
		_label = label;
		addLevel( this );
	}
	
	private static HashMap<String,Level> _labels = new HashMap<String,Level>();
	
	protected static synchronized void addLevel( Level level )
	{
		_labels.put( level.getLabel().toUpperCase(), level );
	}
	public static synchronized Level getLevel( String label )
	{
		Level level = _labels.get( label.toUpperCase() );
		return level;
	}
	
	public static Level OFF   = new Level( Integer.MIN_VALUE, "OFF" );
	public static Level FATAL = new Level( 1000, "FATAL" );
	public static Level ERROR = new Level( 2000, "ERROR" );
	public static Level WARN  = new Level( 3000, "WARN" );
	public static Level INFO  = new Level( 4000, "INFO" );
	public static Level DEBUG = new Level( 5000, "DEBUG" );
	public static Level TRACE = new Level( 6000, "TRACE" );
	public static Level ALL   = new Level( Integer.MAX_VALUE, "ALL" );
}
