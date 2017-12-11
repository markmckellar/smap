package org.smap.serviceresource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class SqlServcieHandlerFactory implements ServiceHandlerResourceFactory
{

	abstract public SqlServcieHandlerResorce getSqlServiceHandlerResourceInstance(HttpServletRequest req, HttpServletResponse res) throws ServletException;
		
	//abstract public ServiceHandlerResource getServiceHandlerResourceInstance(HttpServletRequest req, HttpServletResponse res) throws ServletException;
	/*
	 *                                       getServiceHandlerResourceInstance
	public ServiceHandlerResource getServiceHandlerResourceInstance(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		Log.debug("***************** HERE I AM **********************");
		return(getServiceHandlerResourceInstance(req,res));
	}
	 */
}
