package org.smap.serviceresource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServiceHandlerResourceFactory {

	public ServiceHandlerResource getServiceHandlerResourceInstance(HttpServletRequest req, HttpServletResponse res) throws ServletException;

}
