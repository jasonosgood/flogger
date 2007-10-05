package flogger;

/**
 * Project Flogger
 * 
 * Hosted at http://code.google.com/p/projectflogger/
 * 
 * <Appropriate LGPL blurb goes here.>
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/*
 * Creates a new log file every day, aka "rollover".
 */

public class 
	RollingFileAdapter
extends 
	FileAdapter 
{
	public final static String DEFAULT_FILENAME_PATTERN = "yyyy-MM-dd";

	public void setProperties( HashMap<String,String> props )
	{
		super.setProperties( props );
		if( props.containsKey( "pattern" ))
		{
			String temp = props.get( "pattern" ).trim();
			if( temp.length() > 0 )
			{
				setPattern( temp );
			}
		}
	}	

	private String _pattern = DEFAULT_FILENAME_PATTERN;
	
	public void setPattern( String pattern )
	{
		if( pattern == null )
		{
			throw new NullParameterException( "pattern" );
		}
		_pattern = pattern;
		_formatter = null;
	}
	
	public String getPattern()
	{
		return _pattern;
	}
	
	// Not threadsafe, so we keep our own instance
	private SimpleDateFormat _formatter = null;
	
	private SimpleDateFormat getFormatter() 
	{
		if( _formatter == null )
		{
			_formatter = new SimpleDateFormat( _pattern );
		}
		return _formatter;
	}
	
	private File _file = null;

	public FileOutputStream getOutputStream() 
		throws IOException
	{
		long now = System.currentTimeMillis();
		long tomorrow = getTomorrow();
		if( now >= tomorrow )
		{
			close();
			Date today = new Date( getToday() );
			String date = getFormatter().format( today );
			String extension = getExtension();
			String filename = getFileName() + "." + date + "." + extension;
			_file = new File( getDirectory(), filename );
			_out = new FileOutputStream( _file, true );

			resetTomorrow();
		}
		return _out;
	}

	private long _tomorrow = 0L;

	private void resetTomorrow()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get( Calendar.YEAR );
		int month = calendar.get( Calendar.MONTH );
		int day = calendar.get( Calendar.DAY_OF_MONTH ) + 1;
		// clear all fields
		calendar.clear();
		// set tomorrow's date
		calendar.set( year, month, day );
		_tomorrow = calendar.getTimeInMillis();
	}

	/**
	 * Program logic ensures this value is (re)set before use
	 * @return
	 */
	private long getTomorrow()
	{
		return _tomorrow;
	}

	private long getToday()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get( Calendar.YEAR );
		int month = calendar.get( Calendar.MONTH );
		int day = calendar.get( Calendar.DAY_OF_MONTH );
		// clear all fields
		calendar.clear();
		// set tomorrow's date
		calendar.set( year, month, day );
		long today = calendar.getTimeInMillis();
		return today;
	}


	public void close() throws IOException
	{
		_tomorrow = 0L;
		super.close();
	}
}
