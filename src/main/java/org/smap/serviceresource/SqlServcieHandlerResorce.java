package org.smap.serviceresource;

import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.RequestTypeHandler;
import org.smap.util.Log;

public abstract class SqlServcieHandlerResorce implements ServiceHandlerResource {
	private Connection connection;
	
	abstract public Connection getNewConnection(HttpServletRequest req) throws Exception;
	
	public String getLogTail(HttpServletRequest req,RequestTypeHandler requestTypeHandler) {
		return(":"+req.getRequestURI()+":this="+this+":req="+req.hashCode()+":requestTypeHandler="+requestTypeHandler.getClass());
	}

	public void initResource(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException {
		try	
		{
			Log.debug("SqlServcieHandlerResorce:initResource:starting for"+getLogTail(req,requestTypeHandler));				
			this.setConnection(getNewConnection(req));
			Log.debug("SqlServcieHandlerResorce:initResource:uri:got connection="+getConnection()+getLogTail(req,requestTypeHandler));				
			
			Log.debug("SqlServcieHandlerResorce:initResource:setConnection done, starting setAutoCommit for"+getLogTail(req,requestTypeHandler));				

	        getConnection().setAutoCommit( false );
	        Log.debug("SqlServcieHandlerResorce:initResource:done for"+getLogTail(req,requestTypeHandler));

		}
		catch(Exception e)
		{
			Log.debug("***** ERROR SqlServcieHandlerResorce:initResource:done for"+getLogTail(req,requestTypeHandler));
			throw new ServletException(e.getMessage(),e);
		}		
	}

	public void processInitResourceException(HttpServletRequest req, HttpServletResponse res,Exception exception,RequestTypeHandler requestTypeHandler) throws ServletException {
		throw new ServletException("Error opening sql connection : "+exception.getMessage(),exception);				
	}
	
	public void closeResourceOnError(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException 
	{ 
		try
		{
			if(this.getConnection()!=null) {
				Log.debug("SqlServcieHandlerResorce:closeResourceOnError:starting for"+getLogTail(req,requestTypeHandler));				
				getConnection().rollback();
				getConnection().setAutoCommit( true );
				closeConnection(req,requestTypeHandler);
		        Log.debug("SqlServcieHandlerResorce:closeResourceOnError:done for"+getLogTail(req,requestTypeHandler));
			}
		}
		catch(Exception e)
		{
			Log.debug("***** ERROR SqlServcieHandlerResorce:closeResourceOnError:done for"+getLogTail(req,requestTypeHandler));
			closeConnection(req,requestTypeHandler);
			throw new ServletException(e);
		}
	}

	public void closeResource(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException {
		try	
		{
			Log.debug("SqlServcieHandlerResorce:closeResource:connection="+getConnection()+getLogTail(req,requestTypeHandler));				
			
			if(getConnection()!=null)
			{
				if(!getConnection().isClosed()) 
				{
					Log.debug("SqlServcieHandlerResorce:closeResource:connection is not closed, closing for"+getLogTail(req,requestTypeHandler));
					getConnection().commit();
					getConnection().setAutoCommit( true );	        			
					closeConnection(req,requestTypeHandler);
				}
			}
	        Log.debug("SqlServcieHandlerResorce:closeResource:done for"+getLogTail(req,requestTypeHandler));
			
		}
		catch(Exception e)
		{
			Log.debug("***** ERROR SqlServcieHandlerResorce:closeResource:done for"+getLogTail(req,requestTypeHandler));
	        e.printStackTrace();	        
			closeConnection(req,requestTypeHandler);
			throw new ServletException(e);
		}
	}
	
	public void closeConnection(HttpServletRequest req,RequestTypeHandler requestTypeHandler) throws ServletException
	{
		try
		{
			Log.debug("SqlServcieHandlerResorce:closeConnection:connection="+getConnection()+getLogTail(req,requestTypeHandler));
			if(this.getConnection()!=null)
			{				
				if(!getConnection().isClosed()) getConnection().close();
			}
			else
			{
				Log.debug("SqlServcieHandlerResorce:closeConnection::connection was null"+getLogTail(req,requestTypeHandler));
			}
		}
		catch(Exception e)
		{
	        Log.debug("***** ERROR SqlServcieHandlerResorce:closeConnection for"+getLogTail(req,requestTypeHandler));
	        e.printStackTrace();
			throw new ServletException(e);
		}

	}

	public void processCloseResourceException(HttpServletRequest req, HttpServletResponse res,Exception exception,RequestTypeHandler requestTypeHandler) throws ServletException {
		throw new ServletException("Error closing sql connection : "+exception.getMessage()+getLogTail(req,requestTypeHandler),exception);						
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}


}
