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

	public void initResource(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		try	
		{
			Log.debug("SqlServcieHandlerResorce:initResource:starting for :"+req.getRequestURI()+":this="+this+":req="+req.hashCode());				
			this.setConnection(getNewConnection(req));
			Log.debug("SqlServcieHandlerResorce:initResource:uri:"+req.getRequestURI()+":got connection="+getConnection()+":this="+this+":req="+req.hashCode());				
			
			Log.debug("SqlServcieHandlerResorce:initResource:setConnection done, starting setAutoCommit for :"+req.getRequestURI()+":this="+this+":req="+req.hashCode());				

	        getConnection().setAutoCommit( false );
	        Log.debug("SqlServcieHandlerResorce:initResource:done for :"+req.getRequestURI()+":this="+this+":req="+req.hashCode());

		}
		catch(Exception e)
		{
			Log.debug("***** ERROR SqlServcieHandlerResorce:initResource:done for :"+req.getRequestURI()+":"+e.getMessage()+":this="+this+":req="+req.hashCode());
			throw new ServletException(e.getMessage(),e);
		}		
	}

	public void processInitResourceException(HttpServletRequest req, HttpServletResponse res,Exception exception) throws ServletException {
		throw new ServletException("Error opening sql connection : "+exception.getMessage(),exception);				
	}
	
	public void closeResourceOnError(HttpServletRequest req, HttpServletResponse res) throws ServletException 
	{ 
		try
		{
			Log.debug("SqlServcieHandlerResorce:closeResourceOnError:starting for :"+req.getRequestURI()+":this="+this+":req="+req.hashCode());				
			
			getConnection().rollback();
			getConnection().setAutoCommit( true );
			closeConnection(req);
	        Log.debug("SqlServcieHandlerResorce:closeResourceOnError:done for :"+req.getRequestURI()+":this="+this+":req="+req.hashCode());
			
		}
		catch(Exception e)
		{
			Log.debug("***** ERROR SqlServcieHandlerResorce:closeResourceOnError:done for :"+req.getRequestURI()+":"+e.getMessage()+":this="+this+":req="+req.hashCode());
			
			throw new ServletException(e);
		}
	}

	public void closeResource(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		try	
		{
			Log.debug("SqlServcieHandlerResorce:closeResource:starting for :"+req.getRequestURI()+":connection="+getConnection()+":this="+this+":req="+req.hashCode());				
			
			if(getConnection()!=null)
			{
				if(!getConnection().isClosed()) 
				{
					Log.debug("SqlServcieHandlerResorce:closeResource:connection is not closed, closing for :"+req.getRequestURI()+":this="+this+":req="+req.hashCode());
					getConnection().commit();
					getConnection().setAutoCommit( true );	        			
					closeConnection(req);
				}
			}
	        Log.debug("SqlServcieHandlerResorce:closeResource:done for :"+req.getRequestURI()+":this="+this+":req="+req.hashCode());
			
		}
		catch(Exception e)
		{
			Log.debug("***** ERROR SqlServcieHandlerResorce:closeResource:done for :"+req.getRequestURI()+":"+e.getMessage()+":this="+this+":req="+req.hashCode());
	        e.printStackTrace();	        
			closeConnection(req);
			throw new ServletException(e);
		}
	}
	
	public void closeConnection(HttpServletRequest req) throws ServletException
	{
		try
		{
			Log.debug("SqlServcieHandlerResorce:closeConnection:"+req.getRequestURI()+":connection="+getConnection()+":this="+this+":req="+req.hashCode());
			if(this.getConnection()!=null)
			{				
				if(!getConnection().isClosed()) getConnection().close();
			}
			else
			{
				Log.debug("SqlServcieHandlerResorce:closeConnection:"+req.getRequestURI()+":connection was null:this="+this+":req="+req.hashCode());
			}
		}
		catch(Exception e)
		{
	        Log.debug("***** ERROR SqlServcieHandlerResorce:closeConnection for :"+req.getRequestURI()+":"+e.getMessage()+":this="+this+":req="+req.hashCode());
	        e.printStackTrace();
			throw new ServletException(e);
		}

	}

	public void processCloseResourceException(HttpServletRequest req, HttpServletResponse res,Exception exception) throws ServletException {
		throw new ServletException("Error closing sql connection : "+exception.getMessage(),exception);						
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}


}
