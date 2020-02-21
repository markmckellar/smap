package org.smap.serviceresource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.RequestTypeHandler;

public class EmptyServiceHandlerResource  implements ServiceHandlerResource {

	@Override
	public void initResource(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processInitResourceException(HttpServletRequest req, HttpServletResponse res, Exception exception,RequestTypeHandler requestTypeHandler)
			throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeResource(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processCloseResourceException(HttpServletRequest req, HttpServletResponse res, Exception exception,RequestTypeHandler requestTypeHandler)
			throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeResourceOnError(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
