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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class 
	FileAdapter 
extends 
	Adapter 
{
	public void setProperties( HashMap<String,String> props )
	{
		super.setProperties( props );
		if( props.containsKey( "filename" ))
		{
			String temp = props.get( "filename" ).trim();
			if( temp.length() > 0 )
			{
				setFileName( temp );
			}
		}
		
		if( props.containsKey( "extension" ))
		{
			String temp = props.get( "extension" ).trim();
			if( temp.length() > 0 )
			{
				setExtension( temp );
			}
		}
		
		if( props.containsKey( "directory" ))
		{
			String temp = props.get( "directory" ).trim();
			if( temp.length() > 0 )
			{
				File dir = new File( temp );
				setDirectory( dir );
			}
		}

	}
	
	public final static String DEFAULT_FILENAME;
	public final static String DEFAULT_EXTENSION  = "log";
	public final static File DEFAULT_DIRECTORY = new File( "." );
	
	static
	{
		String temp = FileAdapter.class.getPackage().getName();
		if( temp.endsWith( DEFAULT_EXTENSION ))
		{
			int nth = temp.lastIndexOf( DEFAULT_EXTENSION );
			temp = temp.substring(0, nth );
		}
		if( temp.endsWith( "." ))
		{
			int nth = temp.lastIndexOf( "." );
			temp = temp.substring(0, nth );
		}
		DEFAULT_FILENAME = temp;
		
	}
	
	private String _filename = DEFAULT_FILENAME;

	public void setFileName( String filename )
	{
		if( filename == null )
		{
			throw new NullPointerException( "filename param cannot be null" );
		}
		_filename = filename;
	}
	
	public String getFileName()
	{
		return _filename;
	}
	
	private String _extension = DEFAULT_EXTENSION;
	
	public void setExtension( String extension )
	{
		if( extension == null )
		{
			throw new NullPointerException( "extension param cannot be null" );
		}
		_extension = extension;
	}
	
	public String getExtension()
	{
		return _extension;
	}
	
	private File _directory = DEFAULT_DIRECTORY;

	public void setDirectory( File directory )
	{
		if( directory == null )
		{
			throw new NullPointerException( "directory param cannot be null" );
		}
		try 
		{
			directory = directory.getCanonicalFile();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if( directory.isFile() )
		{
			throw new IllegalArgumentException( directory.getAbsolutePath().toString() + " is not a directory" );
		}
			
		if( !directory.exists() )
		{
			directory.mkdirs();
		}

		if( directory.isHidden() || directory.getName().startsWith( "." ))
		{
			throw new IllegalArgumentException( directory.getAbsolutePath().toString() + " is hidden" );
		}
		
		_directory = directory;
	}
	
	public File getDirectory()
	{
		return _directory;
	}

	protected FileOutputStream _out = null;
	
	public FileOutputStream getOutputStream()
		throws IOException
	{
		if( _out == null )
		{
			String filename = getFileName();
			String extension = getExtension();
			File dir = getDirectory();
			File file = new File( dir, filename + "." + extension ).getAbsoluteFile();
			_out = new FileOutputStream( file, true );
		}
		return _out;
	}

	@Override
	public final void append( String message )
		throws IOException
	{
		FileOutputStream out = getOutputStream();
		out.write( message.getBytes() );
		String linesep = System.getProperty( "line.separator" );
		out.write( linesep.getBytes() );
		out.getFD().sync();
	}
	
	public void close() throws IOException
	{
		if( _out != null )
		{
			_out.close();
		}
	}
	
	public void showConfig()
		throws IOException
	{
		super.showConfig();
		String name = getName();
		append( name + "|filename=" + getFileName() );
		append( name + "|extension=" + getExtension() );
		append( name + "|directory=" + getDirectory() );
	}
	
}
