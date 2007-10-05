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

public class 
	ConsoleAdapter
extends
	Adapter
{

	@Override
	public void append( String message )
		throws IOException
	{
		System.out.println( message );
	}

}
