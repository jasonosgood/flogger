package flogger;

public class 
	FloggerException 
extends 
	Exception 
{
	private static final long serialVersionUID = 1L;
	
	public FloggerException( String param )
	{
		super( param );
	}
	public FloggerException( String param, Throwable cause )
	{
		super( param, cause );
	}
}
