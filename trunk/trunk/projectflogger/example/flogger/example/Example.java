package flogger.example;

/**
 * Project Flogger
 * 
 * Hosted at http://code.google.com/p/projectflogger/
 * 
 * <Appropriate LGPL blurb goes here.>
 */


import static flogger.Flogger.*;
import flogger.Level;
import flogger.Flogger;

public class Example {

	public static void main( String[] args )
	{
		INFO.log( "howdy" );
		
		Flogger info = Flogger.INFO;
		Flogger debug = Flogger.DEBUG;
		info.enter( "enter" );
		info.log( 1 );
		info.log( "haha" );
		info.log( "haha %s", "a" );
		info.log( "haha %s %s", "a", "a" );
		info.log( "haha %s %s %s", "a", "a", "a" );
		info.log( "haha %s %s %s %s", "a", "a", "a", "a" );
		
		Exception e = new Exception( "example exception" );
		
		info.log( e );
		info.log( e, null );
		info.log( e, 1 );
		info.log( e, "haha" );
		info.log( e, "haha %s", "a" );
		info.log( e, "haha %s %s", "a", "a" );
		info.log( e, "haha %s %s %s", "a", "a", "a" );
		info.log( e, "haha %s %s %s %s", "a", "a", "a", "a" );
		info.exit( "exit" );

		Flogger debug2 = Flogger.newFlogger( "example", Level.DEBUG );
		debug2.log( 1 );
		debug2.log( "haha" );
		debug2.log( "haha %s", "a" );
		debug2.log( "haha %s %s", "a", "a" );
		debug2.log( "haha %s %s %s", "a", "a", "a" );
		debug2.log( "haha %s %s %s %s", "a", "a", "a", "a" );
	
	}
}
