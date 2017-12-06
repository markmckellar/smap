package org.smap;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.smap.service.RequestTypeHandler.ServiceTypeEnum;
import org.smap.serviceroute.ServiceRoute;

public class ServiceMapper
{
	
	public ServiceMapper()
	{
	}
	
	public List<ServiceRoute> getServiceRouteList()
	{
		return(null);
	}
	
	
	
	public ServiceRoute findFirstMatchingService(HttpServletRequest req,ServiceTypeEnum serviceTypeEnum)
	{
		ServiceRoute firstServiceRoute = null;
		for(ServiceRoute serviceRoute:getServiceRouteList())
		{
			String uri = req.getRequestURI();
			if(serviceRoute.matches(uri,serviceTypeEnum))
			{
				firstServiceRoute = serviceRoute;
			}
		}
		return(firstServiceRoute);
	}


}
