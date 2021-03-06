package org.smap.interceptor;


import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.RequestTypeHandler;
import org.smap.service.validatelogin.ValidateLoginRequestHandler;
import org.smap.util.Log;

public class InterceptorValidSession extends RequestTypeHandler implements Interceptor
{
	private boolean wasIntercepted;

	public InterceptorValidSession(RequestTypeHandler requestTypeHandler) {
		super(requestTypeHandler.getServiceType(),
				requestTypeHandler.getServiceRoute(),
				new ArrayList<Interceptor>());
	}
	
	public RequestTypeHandler getRequestTypeHandler()
	{
		return(this);
	}

	public boolean isInterceptable(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		boolean isInterceptable = true;
		boolean doesAttributeExist = false;
		if(this.getRequestTypeHandler().getSession().doesAttributeExist(ValidateLoginRequestHandler.SESSION_VALIDATED))
	    {
			doesAttributeExist = true;
			isInterceptable = ! (getSession().getAttribute(ValidateLoginRequestHandler.SESSION_VALIDATED,boolean.class) );
	    }
		Log.debug("InterceptorValidSession:does SESSION_VALIDATED exist?="+doesAttributeExist+":isInterceptable="+isInterceptable);

		return(isInterceptable);
	}
	
	public void handleRequestType(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.setWasIntercepted(false);
		if(isInterceptable(req,res))
		{
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			setWasIntercepted(true);
		}
	}

	public boolean wasIntercepted() {
		return wasIntercepted;
	}

	public void setWasIntercepted(boolean wasIntercepted) {
		this.wasIntercepted = wasIntercepted;
	}
}
