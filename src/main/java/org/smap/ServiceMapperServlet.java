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
			try
			{				
				session = getSessionFactory().getNewSession(req, res);
				session.initSession();
				
				RequestTypeHandler requestTypeHandler = firstServiceRoute.getMathcingServiceTypeEnum(serviceTypeEnum).getNewInstance(session,req, res);
				requestTypeHandler.setServiceRoute(firstServiceRoute);				
				requestTypeHandler.setSession(session);
				requestTypeHandler.processRequest(req,res);
				session.closeSession();
			}
			catch(Exception e) {
				Log.error("ServcieMapperServlet:routeService:had error:"+e.getMessage());

				if(session!=null)
				try {
					Log.error("ServcieMapperServlet:routeService:trying to close session resource:had error:"+e.getMessage());
					e.printStackTrace();
					session.getServiceHandlerResource().closeResourceOnError(req, res);;
					session = null;
				}
				catch(Exception eclose) {
					Log.error("Error closing a session! : "+eclose.getMessage()+" initial error : "+e.getMessage());
				}
			}
			finally {
				if(session!=null)
					try {
						session.getServiceHandlerResource().closeResource(req, res);;
						session = null;
					}
					catch(Exception eclose) {
						Log.error("Error closing a session at final! : "+eclose.getMessage());
					}
			}
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
        //doPost(req, res);
    }

    public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
    {       
    	Log.info("doGet:uri="+req.getRequestURI());
    	routeService(req,res,ServiceTypeEnum.GET);
        //doPost(req, res);
    }
    
    public void doHead( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
    {       
    	Log.info("doHead:uri="+req.getRequestURI());
    	routeService(req,res,ServiceTypeEnum.HEAD);
        //doPost(req, res);
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
        //doPost(req, res);
    }

	public List<ServiceRoute> getServiceRouteList() {
		return serviceRouteList;
	}

	public void setServiceRouteList(List<ServiceRoute> serviceRouteList) {
		this.serviceRouteList = serviceRouteList;
	}

}
