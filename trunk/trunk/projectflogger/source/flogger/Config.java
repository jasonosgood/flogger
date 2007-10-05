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
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// TODO: automatic reload with retry interval

public class Config 
{
	private Config() { }
	
	private static Config _instance;
	private static Object _lock = new Object();
	public static Config getInstance()
	{
		synchronized( _lock )
		{
			if( _instance == null )
			{
				_instance = new Config();
			}
		}
		return _instance;
	}
	

	public final static String PROPERTIES_FILENAME;
	static 
	{
		Package p = Config.class.getPackage();
		PROPERTIES_FILENAME = p.getName() + ".properties";
	}
	
	private Properties _props = null;
	
	public Properties getProperties()
	{
		synchronized( _lock )
		{
			InputStream in = ClassLoader.getSystemResourceAsStream( PROPERTIES_FILENAME );
			try
			{
				if( in != null )
				{
					_props = new Properties();
					_props.load( in );
				}
			}
			catch( IOException e ) 
			{
				_props = null;
				e.printStackTrace();
			}
		}
		return _props;
	}
	
	// null value means "undefined"
	private Boolean _disabled = null;

	/**
	 * Disabling Config looses all configuration property data, forcing a reload next time
	 * its needed.
	 * 
	 * @param disabled
	 */
	public void disabled( boolean disabled )
	{
		synchronized( _lock )
		{
			if( disabled )
			{
				_disabled = Boolean.TRUE;
				_props = null;
				_topicMap = null;
			}
			else
			{
				_disabled = null;
			}
		}
	}

	public boolean disabled()
	{
		synchronized( _lock )
		{
			if( _disabled == null )
			{
				boolean temp = Boolean.getBoolean( "flogger.disabled" );
				_disabled = temp ? Boolean.TRUE : Boolean.FALSE;
			}
			if( _disabled == null )
			{
				Properties props = getProperties();
				String temp1 = props.getProperty( "flogger.disabled", "false" );
				boolean temp2 = Boolean.parseBoolean( temp1 );
				_disabled = temp2 ? Boolean.TRUE : Boolean.FALSE;
			}
		}
		return _disabled.booleanValue();
	}
	
	private HashMap<String,Topic> _topicMap = null;
	
	protected void loadTopicMap()
	{
		synchronized( _lock )
		{
			if( _topicMap != null ) return;
			_topicMap = new HashMap<String,Topic>();
			
			Properties props = getProperties();
			if( props == null ) return;
			
			Enumeration<Object> i = props.keys();
			HashMap<String,HashMap<String,String>> names = new HashMap<String,HashMap<String,String>>();
			while( i.hasMoreElements() )
			{
				String key = (String) i.nextElement();
				int nth = key.indexOf( '|' );
				// Test for no match (-1), first char (0), and last char
	//					 TODO report error
				if( nth < 1 || nth + 1 == key.length() ) continue;
				String name = key.substring( 0, nth ).trim().toLowerCase();
				String param = key.substring( nth + 1 ).trim().toLowerCase();
				String value = props.getProperty( key );
	
				HashMap<String,String> nested = null;
				if( names.containsKey( name ))
				{
					nested = names.get( name );
				}
				else
				{
					nested = new HashMap<String,String>();
					names.put( name, nested );
				}
				nested.put( param, value );
			}
			
			HashMap<String,Adapter> adapters = new HashMap<String,Adapter>();
			for( Map.Entry<String,HashMap<String, String>> nameEntry : names.entrySet() )
			{
				String adapterName = nameEntry.getKey();
				HashMap<String, String> nested = nameEntry.getValue();
				if( nested.containsKey( "adapter" ))
				{
					String clazzName = nested.get( "class" );
					
					Class clazz = null;
					if( clazzName == null )
					{
						String c2 = ConsoleAdapter.class.getSimpleName();
						String blurb = "class not defined for adapter '" + adapterName + "', defaulting to class " + c2;
						System.err.println( blurb );
						clazz = ConsoleAdapter.class;
					}
					else
					{
						// First try provided class name, then try "simple" class name (no package name provided)
						// Use introspection for future proofing
						try
						{
							clazz = Class.forName( clazzName );
						} 
						catch( ClassNotFoundException e1 ) 
						{
							try
							{
								Package p = Config.class.getPackage();
								String temp = p.getName() + "." + clazzName;
								clazz = Class.forName( temp );
							} 
							catch( ClassNotFoundException e2 ) {}
						}
					}
					if( clazz == null )
					{
						String c2 = ConsoleAdapter.class.getSimpleName();
						String blurb = "cannot find class " + clazzName + " for adapter '" + adapterName + "', using " + c2;
						System.err.println( blurb );
						clazz = ConsoleAdapter.class;
					}
	
					if( !Adapter.class.isAssignableFrom( clazz ))
					{
						String c1 = Adapter.class.getSimpleName();
						String c2 = ConsoleAdapter.class.getSimpleName();
						String blurb = "class " + clazzName + " for adapter '" + adapterName + "' does not extend class " + c1 + ", using " + c2;
						System.err.println( blurb );
						clazz = ConsoleAdapter.class;
					}
					
					try 
					{
						Adapter adapter = (Adapter) clazz.newInstance();
						adapter.setName( adapterName );
						adapter.setProperties( nested );
						adapters.put( adapterName, adapter );
					} 
					catch( InstantiationException e ) 
					{
						System.err.println( e.getMessage() );
					} 
					catch( IllegalAccessException e )
					{
						System.err.println( e.getMessage() );
					}
				}
			}
	
			for( Map.Entry<String,HashMap<String, String>> nameEntry : names.entrySet() )
			{
				String topicName = nameEntry.getKey();
				HashMap<String, String> nested = nameEntry.getValue();
				if( nested.containsKey( "topic" ))
				{
					Topic topic = new Topic();
					topic.setName( topicName );
	
					topic.setDescription( nested.get( "topic" ));
					
					if( nested.containsKey( "match" ))
					{
						topic.setMatch( nested.get( "match" ));
					}
					
					if( nested.containsKey( "level" ))
					{
						String temp = nested.get( "level" );
						Level level = Level.getLevel( temp );
						topic.setLevel( level );
					}
	
					if( nested.containsKey( "adapters" ))
					{
						String temp = nested.get( "adapters" ).toLowerCase();
						String[] adapterNames = temp.split( "," );
						for( String adapterName : adapterNames )
						{
							adapterName = adapterName.trim();
							if( adapters.containsKey( adapterName ))
							{
								Adapter adapter = adapters.get( adapterName );
								topic.addAdapter( adapter );
							}
						}
					}
					_topicMap.put( topicName, topic );
				}
			}
			
		}
	}

	
	public Topic getTopic( String name )
	{
		loadTopicMap();
		
		Topic topic;
		if( _topicMap.containsKey( name ))
		{
			topic = _topicMap.get( name );
		}
		else
		{
			topic = new Topic();
			topic.setMatch( name );
			_topicMap.put( name, topic );
		}
		return topic;
	}
}
