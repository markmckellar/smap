package org.smap.service;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.interceptor.Interceptor;
import org.smap.serviceroute.ServiceRoute;

public class HelloRequestHandler extends RequestTypeHandler
{
	public HelloRequestHandler(ServiceTypeEnum serviceType,ServiceRoute serviceRoute) {
		super(serviceType,serviceRoute,new ArrayList<Interceptor>());
	}

	public void handleRequestType(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		writeStringResponse(res,"Hello world",ResponseType.TextPlain);
		
	}

}
