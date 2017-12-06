package org.smaptest.server.jetty;

import java.net.URL;
import org.mortbay.jetty.Server;
import org.mortbay.xml.XmlConfiguration;

public class StartJetty
{

    public static void main( String[] args )
    {
        try
        {
        	XmlConfiguration config = new XmlConfiguration(findResourceByName("org/smaptest/server/jetty/jetty.xml"));
            Server server = (Server) config.configure();
            server.start();
            server.join();

        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
	public static URL findResourceByName( String name )
    {
        return Thread.currentThread().getContextClassLoader().getResource( name );
    }
}
