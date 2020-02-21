package org.smap.serviceresource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.RequestTypeHandler;

public interface ServiceHandlerResource {
	//public void initResource(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException;
	//public void processInitResourceException(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler,Exception exception) throws ServletException;
	//public void closeResource(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException;
	//public void processCloseResourceException(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler,Exception exception) throws ServletException;
	//public void closeResourceOnError(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException;
	
	public void initResource(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException;
	public void processInitResourceException(HttpServletRequest req, HttpServletResponse res,Exception exception,RequestTypeHandler requestTypeHandler) throws ServletException;
	public void closeResource(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException;
	public void processCloseResourceException(HttpServletRequest req, HttpServletResponse res,Exception exception,RequestTypeHandler requestTypeHandler) throws ServletException;
	public void closeResourceOnError(HttpServletRequest req, HttpServletResponse res,RequestTypeHandler requestTypeHandler) throws ServletException;

}
