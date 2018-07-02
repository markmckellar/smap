package org.smap.serviceroute;

import java.util.ArrayList;
import java.util.List;

import org.smap.ServiceMapperServlet;
import org.smap.service.RequestTypeHandler;
import org.smap.service.RequestTypeHandler.ServiceTypeEnum;


public class ServiceRoute 
{
	private StringFormatExtractor pathSelector;
	private List<RequestTypeHandler> requestTypeHandlerList;
	private ServiceMapperServlet serviceMapperServlet;
	private String passedInPath;
	
	public ServiceRoute(ServiceRoute serviceRoute) throws Exception
	{
		this(serviceRoute.getPassedInPath(),serviceRoute.getServiceMapperServlet());
		this.getRequestTypeHandlerList().addAll(serviceRoute.getRequestTypeHandlerList());
	}
	
	public ServiceRoute(String path,ServiceMapperServlet serviceMapperServlet) throws Exception
	{
		this.setPassedInPath(path);
		this.setPathSelector(new StringFormatExtractor(path));
		this.setRequestTypeHandlerList(new ArrayList<RequestTypeHandler>());
		this.setServiceMapperServlet(serviceMapperServlet);
	}

	public StringFormatExtractor getPathSelector() {
		return pathSelector;
	}

	public void setPathSelector(StringFormatExtractor pathSelector) {
		this.pathSelector = pathSelector;
	}

	public boolean matches(String uri, ServiceTypeEnum serviceTypeEnum) {	
		/*
		Log.debug("ServiceRoute:matches"+
				":uri="+uri+
				":serviceTypeEnum="+serviceTypeEnum+
				":handlesServiceTypeEnum(serviceTypeEnum)="+handlesServiceTypeEnum(serviceTypeEnum)+
				":getPathSelector().matches(uri)="+getPathSelector().matches(uri)+
				"");
				*/
		return( handlesServiceTypeEnum(serviceTypeEnum) && getPathSelector().matches(uri) ); 
	}
	
	public boolean handlesServiceTypeEnum(ServiceTypeEnum serviceTypeEnum) {
		return( getMathcingServiceTypeEnum(serviceTypeEnum)!=null );
	}

	public RequestTypeHandler getMathcingServiceTypeEnum(ServiceTypeEnum serviceTypeEnum) {
		for(RequestTypeHandler requestTypeHandler:getRequestTypeHandlerList())
		{
			if(requestTypeHandler.matches(serviceTypeEnum)) return(requestTypeHandler);
		}
		return(null);
	}

	public List<RequestTypeHandler> getRequestTypeHandlerList() {
		return requestTypeHandlerList;
	}

	public void setRequestTypeHandlerList(List<RequestTypeHandler> requestTypeHandlerList) {
		this.requestTypeHandlerList = requestTypeHandlerList;
	}

	public ServiceMapperServlet getServiceMapperServlet() {
		return serviceMapperServlet;
	}

	public void setServiceMapperServlet(ServiceMapperServlet serviceMapperServlet) {
		this.serviceMapperServlet = serviceMapperServlet;
	}

	public String getPassedInPath() {
		return passedInPath;
	}

	public void setPassedInPath(String passedInPath) {
		this.passedInPath = passedInPath;
	}
}
