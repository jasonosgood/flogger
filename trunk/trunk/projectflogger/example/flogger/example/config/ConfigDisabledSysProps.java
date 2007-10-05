package flogger.example.config;

import static flogger.Flogger.INFO;

import java.util.Properties;

import flogger.Config;

public class 
	ConfigDisabledSysProps 
{
	public static void main( String[] args )
	{
		// System Properties override
		Config config = Config.getInstance();
		Properties props = config.getProperties();
		props.setProperty( "flogger.disabled", "true" );
		System.out.println( "disabled: " + config.disabled() );
		INFO.log( "hello world" );
	}
}
