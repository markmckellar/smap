package org.smap.serviceresource;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.smap.util.Log;

public class JndiSqlServcieHandlerResorce extends SqlServcieHandlerResorce {
	
	private String contextName;
	
	public JndiSqlServcieHandlerResorce(String contextName)
	{
		this.contextName = contextName;
	}

	public Connection getNewConnection(HttpServletRequest req) throws Exception {
		return getDatabaseConnection(req,this.getContextName());
	}

    public  Connection getDatabaseConnection(HttpServletRequest req,String contextName ) throws Exception
    {
		Log.debug("JndiSqlServcieHandlerResorce:initResource:starting:this="+this+":req="+req.hashCode());				

        Context initCtx = new InitialContext();
        // Data source is required to be set up and named as below.
        Context envCtx = (Context)initCtx.lookup( "java:comp/env" );
        DataSource ds = (DataSource)envCtx.lookup( contextName );
        Connection connection = ds.getConnection();
        Log.debug("JndiSqlServcieHandlerResorce:initResource:ending:this="+this+":connection="+connection+":req="+req.hashCode());				
        
        return(connection);
    }
	public String getContextName() {
		return contextName;
	}
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}


}
