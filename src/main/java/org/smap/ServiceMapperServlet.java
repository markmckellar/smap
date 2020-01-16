package org.smap;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.RequestTypeHandler;
import org.smap.service.RequestTypeHandler.ServiceTypeEnum;
import org.smap.serviceroute.ServiceRoute;
import org.smap.session.SessionFactoryInterface;
import org.smap.session.SessionInterface;
import org.smap.util.Log;

public abstract class ServiceMapperServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ServiceRoute> serviceRouteList;
	
	
	public ServiceRoute findFirstMatchingService(HttpServletRequest req,ServiceTypeEnum serviceTypeEnum) throws ServletException
	{
		ServiceRoute firstServiceRoute = null;
		String uri = req.getRequestURI();

		for(ServiceRoute serviceRoute:getServiceRouteList())
		{
			if(serviceRoute.matches(uri,serviceTypeEnum))
			{
				firstServiceRoute = serviceRoute;
				Log.info("ServiceMapperServlet:findFirstMatchingService:Found a route for:"+uri+":"+firstServiceRoute.getPassedInPath());
				try {
					
					
					// 2018-06-29 NEW!  posbile issue with concurency
					firstServiceRoute = new ServiceRoute(serviceRoute);
					
					
					firstServiceRoute.getPathSelector().setExtractedValueMap(
							firstServiceRoute.getPathSelector().createTagNameToValueMap(uri));
				} catch (Exception e) {
					throw new ServletException(e.getMessage());
				}
				break;
			}
		}
		return(firstServiceRoute);
	}

	abstract public SessionFactoryInterface getSessionFactory() throws ServletException;
	
	public ServiceRoute createNewRoute(String path,RequestTypeHandler requestTypeHandler) throws Exception
	{
		ServiceRoute serviceRoute = new ServiceRoute(path,this);
		requestTypeHandler.setServiceRoute(serviceRoute);
		serviceRoute.getRequestTypeHandlerList().add(requestTypeHandler);
		return(serviceRoute);
	}
	
	public void routeService(HttpServletRequest req, HttpServletResponse res,ServiceTypeEnum serviceTypeEnum) throws ServletException, IOException
	{
		Date startTime = new Date();
		Log.info("------- starting "+req.getRequestURI()+" ----------------------------------------------"+":req="+req.hashCode());
		Log.info("ServiceMapperServlet:serviceTypeEnum="+serviceTypeEnum.name()+":uri="+req.getRequestURI());
		ServiceRoute firstServiceRoute = findFirstMatchingService(req,serviceTypeEnum);
		if(firstServiceRoute==null)
		{
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else
		{
			Log.info("ServiceMapperServlet:found route"+
					":serviceRoute="+firstServiceRoute.getPathSelector().getFormatString()+
					":serviceTypeEnum="+serviceTypeEnum+
					":req="+req.hashCode()+
					"");
			
			
			//RequestTypeHandler requestTypeHandler = firstServiceRoute.getMathcingServiceTypeEnum(serviceTypeEnum).getNewInstance(req, res);
			// 2018-06-29 NEW!  possible issue with concurrency
			//requestTypeHandler.setServiceRoute(firstServiceRoute);
			SessionInterface session = null;
			
			
				Log.debug("ServcieMapperServlet:routeService:processing:1"+":req="+req.hashCode());
				session = getSessionFactory().getNewSession(req, res);				
				Log.debug("ServcieMapperServlet:routeService:processing:2"+":req="+req.hashCode());
				session.initSession();
				Log.debug("ServcieMapperServlet:routeService:processing:3"+":req="+req.hashCode());
				
				
				
				
				RequestTypeHandler requestTypeHandler = firstServiceRoute.getMathcingServiceTypeEnum(serviceTypeEnum).getNewInstance(session,req, res);
				Log.debug("ServcieMapperServlet:routeService:processing:4"+":req="+req.hashCode());
				requestTypeHandler.setServiceRoute(firstServiceRoute);				
				Log.debug("ServcieMapperServlet:routeService:processing:5"+":req="+req.hashCode());
				requestTypeHandler.setSession(session);
				Log.debug("ServcieMapperServlet:routeService:processing:6"+":req="+req.hashCode());
				requestTypeHandler.processRequest(req,res);
				
				
				
				
				Log.debug("ServcieMapperServlet:routeService:processing:7"+":req="+req.hashCode());
				session.closeSession();
				Log.debug("ServcieMapperServlet:routeService:processing:8"+":req="+req.hashCode());
			

		}		
		long elapsedTime = new java.util.Date().getTime() - startTime.getTime();	    
		Log.info("------- ending "+req.getRequestURI()+":service elapsedTime:"+elapsedTime+" -----------------------------"+":req="+req.hashCode());		
	}	
	
	public abstract List<ServiceRoute> getInitServiceRouteList() throws Exception;	

    public void init( ServletConfig config ) throws ServletException
    {
        super.init(config);

    	Log.info("init");
        Log.info("ServiceMapperServlet:init");  
        try
        {
        	setServiceRouteList(getInitServiceRouteList());
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	throw new ServletException(e);
        }
    }
    
    public void doDelete( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
    {       
    	Log.info("doDelete:uri="+req.getRequestURI());
    	routeService(req,res,ServiceTypeEnum.DELETE);
    }

    public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
    {       
    	Log.info("doGet:uri="+req.getRequestURI());
    	routeService(req,res,ServiceTypeEnum.GET);
    }
    
    public void doHead( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
    {       
    	Log.info("doHead:uri="+req.getRequestURI());
    	routeService(req,res,ServiceTypeEnum.HEAD);
    }

    public void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
    {
    	Log.info("doPost:uri="+req.getRequestURI());
    	routeService(req,res,ServiceTypeEnum.POST);
    }
    
    public void doPut( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
    {       
    	Log.info("doPut:uri="+req.getRequestURI());
    	routeService(req,res,ServiceTypeEnum.PUT);
    }

	public List<ServiceRoute> getServiceRouteList() {
		return serviceRouteList;
	}

	public void setServiceRouteList(List<ServiceRoute> serviceRouteList) {
		this.serviceRouteList = serviceRouteList;
	}

}
