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

	default boolean processInterceptor(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException
	{
		RequestTypeHandler requestTypeHandler = getRequestTypeHandler().getNewInstance(req, res);
		requestTypeHandler.processRequest(req, res);
		return(wasIntercepted());
	}
}
