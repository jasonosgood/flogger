package flogger.example.config;

import flogger.Config;
import static flogger.Flogger.INFO;

public class 
	ConfigEnabled 
{
	public static void main( String[] args )
	{
		// Direct override
		Config config = Config.getInstance();
		config.disabled( false );
		config.setShowConfig( true );
		System.out.println( "disabled: " + config.disabled() );
		INFO.log( "hello world" );
		
	}
}
