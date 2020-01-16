package org.smap.serviceresource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.smap.util.Log;

public class JndiSqlServcieHandlerResorce extends SqlServcieHandlerResorce {
	
	private String contextName;
	Context initCtx;
	
	public JndiSqlServcieHandlerResorce(String contextName,Context initCtx)
	{
		this.contextName = contextName;
        this.initCtx = initCtx;

	}

	public Connection getNewConnection(HttpServletRequest req) throws Exception {
		return getDatabaseConnection(req,this.getContextName());
	}

    public  Connection getDatabaseConnection(HttpServletRequest req,String contextName ) throws Exception
    {
		Log.debug("JndiSqlServcieHandlerResorce:initResource:0:starting:this="+this+":req="+req.hashCode());				

        //Context initCtx = new InitialContext();
        // Data source is required to be set up and named as below.
		Log.debug("JndiSqlServcieHandlerResorce:initResource:1:this="+this+":req="+req.hashCode());				

        Context envCtx = (Context)initCtx.lookup( "java:comp/env" );
		Log.debug("JndiSqlServcieHandlerResorce:initResource:2:this="+this+":req="+req.hashCode());				

        DataSource ds = (DataSource)envCtx.lookup( contextName );       
		Log.debug("JndiSqlServcieHandlerResorce:initResource:3:this="+this+":req="+req.hashCode());				
		
        Connection connection = ds.getConnection();
        Log.debug("JndiSqlServcieHandlerResorce:initResource:ending:this="+this+":connection="+connection+":req="+req.hashCode());	
        
     // Create Oracle DatabaseMetaData object
        DatabaseMetaData meta = connection.getMetaData();

        // gets driver info:
        Log.debug("JndiSqlServcieHandlerResorce:initResource:JDBC driver version is " + meta.getDriverVersion());
        
        return(connection);
    }
	public String getContextName() {
		return contextName;
	}
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}


}
