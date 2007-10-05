package flogger;

public class 
	NullParameterException 
extends 
	NullPointerException 
{
	private static final long serialVersionUID = 1L;
	
	public NullParameterException( String param )
	{
		super( param );
	}
}
