package flogger.example.config;

import static flogger.Flogger.INFO;

import flogger.Config;

public class 
	ConfigDisabledPropsFile 
{
	public static void main( String[] args )
	{
		// Flogger Properties override
		Config config = Config.getInstance();
		System.setProperty( "flogger.disabled", "true" );
		System.out.println( "disabled: " + config.disabled() );
		INFO.log( "hello world" );
	}
}
