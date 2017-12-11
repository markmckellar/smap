package org.smaptest.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.smap.ServiceMapperServlet;
import org.smap.interceptor.InterceptorValidSession;
import org.smap.service.GetServerInfoRequestHandler;
import org.smap.service.HelloRequestHandler;
import org.smap.service.RequestTypeHandler;
import org.smap.service.RequestTypeHandler.ServiceTypeEnum;
import org.smap.serviceroute.ServiceRoute;


@SuppressWarnings("serial")
public class SMapTest extends ServiceMapperServlet
{
	
    public void init( ServletConfig config ) throws ServletException
    {
        super.init(config);
    }

	public List<ServiceRoute> getInitServiceRouteList() throws Exception
	{		
		List<ServiceRoute> serviceRouteList = new ArrayList<ServiceRoute>(); 
		
		String baseServletPath = getServletContext().getContextPath();
		
		serviceRouteList.add(createNewRoute(baseServletPath+"/service/serverinfo",
				new GetServerInfoRequestHandler(ServiceTypeEnum.GET,null)));
				
		serviceRouteList.add(createNewRoute(baseServletPath+"/service/hello",
				new HelloRequestHandler(ServiceTypeEnum.GET,null)));
						
		return(serviceRouteList);
	}
	
	public ServiceRoute createNewRouteValidateSession(String path,RequestTypeHandler requestTypeHandler) throws Exception
	{
		ServiceRoute serviceRoute = new ServiceRoute(path,this);
		
		requestTypeHandler.setServiceRoute(serviceRoute);
		serviceRoute.getRequestTypeHandlerList().add(requestTypeHandler);
		requestTypeHandler.getInterceptorList().add(new InterceptorValidSession(requestTypeHandler));
		
		return(serviceRoute);
	}
		
}
