package org.smap.session;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.SqlServiceHandler;
import org.smap.serviceresource.SqlServcieHandlerFactory;
import org.smap.serviceroute.ServiceRoute;

public class ClearSessionStorageRequestHandler extends SqlServiceHandler {
	private DataBaseSessionExternal dataBaseSessionHandler;

	
	public ClearSessionStorageRequestHandler(SqlServcieHandlerFactory sqlServcieHandlerFactory,ServiceRoute serviceRoute) {
		super(ServiceTypeEnum.GET, sqlServcieHandlerFactory,serviceRoute);
	}

	@Override
	public void handleRequestType(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
	    
		    try
		    {
		    	this.getDataBaseSessionHandler().clearSessionStorage(getSqlServcieHandlerResorce().getConnection());
		    }
		    catch(Exception e)
		    {
		    	throw new ServletException(e);
		    }				
	}

	public DataBaseSessionExternal getDataBaseSessionHandler() {
		return dataBaseSessionHandler;
	}

	public void setDataBaseSessionHandler(DataBaseSessionExternal dataBaseSessionHandler) {
		this.dataBaseSessionHandler = dataBaseSessionHandler;
	}
	 
	
}
