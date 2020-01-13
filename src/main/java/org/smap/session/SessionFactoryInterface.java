package org.smap.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.RequestTypeHandler;
import org.smap.serviceresource.ServiceHandlerResource;

public interface SessionFactoryInterface  {
	SessionInterface getNewSession(HttpServletRequest req, HttpServletResponse res) throws Exception;
}
