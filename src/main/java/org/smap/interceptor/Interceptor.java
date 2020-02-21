package org.smap.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.RequestTypeHandler;
import org.smap.session.SessionInterface;

public interface Interceptor
{
	public RequestTypeHandler getRequestTypeHandler();
	
	public boolean wasIntercepted() throws ServletException,IOException;

	default boolean processInterceptor(SessionInterface session,HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException
	{
		RequestTypeHandler requestTypeHandler = getRequestTypeHandler().getNewInstance(session,req, res);
		requestTypeHandler.processRequest(req, res);
		Interceptor interceptor = (Interceptor) requestTypeHandler;
		return(interceptor.wasIntercepted());
	}
}
