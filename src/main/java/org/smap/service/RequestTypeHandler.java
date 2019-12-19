package org.smap.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.interceptor.Interceptor;
import org.smap.serviceresource.ServiceHandlerResource;
import org.smap.serviceroute.ServiceRoute;
import org.smap.session.SessionFactoryInterface;
import org.smap.session.SessionInterface;
import org.smap.util.Log;

public abstract class RequestTypeHandler implements Cloneable
{
	public static enum ServiceTypeEnum {DELETE,GET,HEAD,POST,PUT,INTERCEPTOR};
	
	
	private ServiceRoute serviceRoute;
	private ServiceTypeEnum serviceType;
	private List<ServiceHandlerResource> serviceHandlerResourceListPre;
	private List<Interceptor> interceptorList;
	private SessionFactoryInterface sessionFactory;

	
	public RequestTypeHandler(ServiceTypeEnum serviceType,
			ServiceRoute serviceRoute,List<Interceptor> interceptorList,
			SessionFactoryInterface sessionFactory)
	{
		this.setServiceType(serviceType);
		this.setServiceHandlerResourceList(new ArrayList<ServiceHandlerResource>());
		this.setServiceRoute(serviceRoute);
		this.setInterceptorList(interceptorList);
		this.setSessionFactory(sessionFactory);
	}
	
	public RequestTypeHandler getNewInstance(HttpServletRequest req,HttpServletResponse res) throws ServletException
	{
		RequestTypeHandler clone = null;
		try
		{
			clone = (RequestTypeHandler) clone();
			clone.setServiceHandlerResourceList(new ArrayList<ServiceHandlerResource>());
		}
		catch(Exception e)
		{
			throw new ServletException(e);
		}
		return(clone);
	}
	
	public RequestTypeHandler clone() throws CloneNotSupportedException
	{
        return (RequestTypeHandler) super.clone();
	}

	//public abstract RequestTypeHandler getNewInstance(HttpServletRequest req,HttpServletResponse res) throws ServletException;
	public abstract void handleRequestType(HttpServletRequest req,HttpServletResponse res) throws ServletException,java.io.IOException;	
	
	public void processRequest(HttpServletRequest req,HttpServletResponse res,SessionInterface session) throws ServletException,java.io.IOException
	{
		processRequest(this,req,res,session);
	}

	public static void processRequest(RequestTypeHandler requestTypeHandler,HttpServletRequest req,HttpServletResponse res,SessionInterface session) throws ServletException,java.io.IOException
	{
		boolean wasIntercepted = false;
		Log.debug("Checking interceptor List for:"+req.getRequestURI()+":getInterceptorList.size="+requestTypeHandler.getInterceptorList().size());				

		for(Interceptor interceptor:requestTypeHandler.getInterceptorList()) 
		{
			wasIntercepted = interceptor.processInterceptor(req,res,requestTypeHandler.getSessionFactory().getSession(req));
			Log.debug("Checking interceptor List for:"+req.getRequestURI()+
					":interceptor="+interceptor.getClass()+
					":wasIntercepted="+wasIntercepted+
					""
					);				

			if(wasIntercepted) break;
		}
		
		if(!wasIntercepted) processRequestInner(requestTypeHandler,req,res);
	}
	
	public static void processRequestInner(RequestTypeHandler requestTypeHandler,HttpServletRequest req,HttpServletResponse res) throws ServletException,java.io.IOException

	{
		try
		{				
			Log.debug("Starting processing for :"+req.getRequestURI()+":req="+req.hashCode());				
			requestTypeHandler.initResources(req, res);
			Log.debug("After initResources for :"+req.getRequestURI()+":req="+req.hashCode());
			
			requestTypeHandler.handleRequestType(req, res);
			
			Log.debug("After handleRequestType for :"+req.getRequestURI());												
		}
		catch(Exception e)
		{
			Log.debug("Had an exception for :"+req.getRequestURI()+":this="+requestTypeHandler+":req="+req.hashCode());
			Log.debug("**** "+e.getMessage());
			e.printStackTrace();
			
			try
			{
				Log.debug("Starting closeResourcesOnError for :"+req.getRequestURI()+":req="+req.hashCode());
				requestTypeHandler.closeResourcesOnError(req, res);
				Log.debug("Ended closeResourcesOnError for :"+req.getRequestURI()+":req="+req.hashCode());
			}
			catch(Exception ex)
			{
				Log.debug("***** Error on closeResourcesOnError"+":req="+req.hashCode());
				ex.printStackTrace();
				throw new ServletException("Error on closeResourcesOnError");
			}
            Log.debug(e);
        	e.printStackTrace();
            throw new ServletException(e);

		}
		finally
		{
			Log.debug("Starting finally for :"+req.getRequestURI());				

		       try
	            {
		    	   Log.debug("Starting closeResources for :"+req.getRequestURI()+":req="+req.hashCode());
		    	   requestTypeHandler.closeResources(req, res);
		    	   Log.debug("Ended closeResources for :"+req.getRequestURI()+":req="+req.hashCode());
		    	   
	            }
	            catch ( Exception ex )
	            {
	                Log.debug("***** Error on closeResources : "+ex.getMessage()+":req="+req.hashCode());
	                ex.printStackTrace();
	                throw new ServletException("Error on closeResources : "+ex.getMessage());
	            }
				Log.debug("Ended finally for :"+req.getRequestURI());				

		}			
	}		

	public void writeStringResponsePdf(HttpServletResponse res,byte[] pdfData,String fileName) throws IOException
	{
	    Log.debug(this.getClass().getName()+":writeStringResponsePdf:pdfData.length="+pdfData.length);
		res.setContentType(ResponseType.ApplicationPdf.getType());
		res.setHeader("Content-disposition","inline; filename='"+fileName+"'");
		//res.setHeader("Cache-Control", "nocache");
		//res.setCharacterEncoding("utf-8");
		
		ServletOutputStream os = res.getOutputStream();
		os.write(pdfData);
	}
	
	public void writeStringResponseJson(HttpServletResponse res,String json) throws IOException
	{
	    Log.debug(this.getClass().getName()+":writeStringResponseJson:json.length()="+json.length());
		res.setContentType(ResponseType.ApplicationJson.getType());
		res.setHeader("Cache-Control", "nocache");
		res.setCharacterEncoding("utf-8");
		PrintWriter out = res.getWriter();
        out.print(json);
	}
	
	public void writeStringResponse(HttpServletResponse res,CharSequence string,ResponseType responseType) throws IOException
	{
	    Log.debug(this.getClass().getName()+":writeStringResponse:string.length()="+string.length());
		res.setContentType(responseType.getType());
		res.getWriter().append(string);	
	}
	
	/****
	 * ..headers should be :
	????Accept: application/json
	????Connection: keep-alive
	????Content-Type: application/json
	**/
	public String getStringParmaterFromPostRequest(HttpServletRequest req) throws IOException 
	{
		StringBuilder jsonStringBuilder = new StringBuilder();
	    BufferedReader br = req.getReader();
	    String line;
	    while( (line = br.readLine()) != null) {
	    	jsonStringBuilder.append(line);
		}
		return(jsonStringBuilder.toString());
	}

	public String getStringParmaterFromGetRequest(HttpServletRequest req) throws IOException 
	{
		StringBuilder jsonStringBuilder = new StringBuilder();
		if(req.getParameter("loadProds") != null)
		{
		    // 1. get received JSON data from request
		    BufferedReader br = new BufferedReader(new  InputStreamReader(req.getInputStream()));
		    String line;
		    while( (line = br.readLine()) != null) {
		    	jsonStringBuilder.append(line);
		    }
		}
		return(jsonStringBuilder.toString());
	}
	
	public boolean matches(ServiceTypeEnum serviceTypeEnum)
	{
		return(this.getServiceType().compareTo(serviceTypeEnum)==0);
	}
		
	public ServiceTypeEnum getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceTypeEnum serviceType) {
		this.serviceType = serviceType;
	}
	
	private List<ServiceHandlerResource> getServiceHandlerResourceList() {
		return serviceHandlerResourceListPre;
	}

	private void setServiceHandlerResourceList(List<ServiceHandlerResource> serviceHandlerResourceList) {
		this.serviceHandlerResourceListPre = serviceHandlerResourceList;
	}
	
	public List<ServiceHandlerResource> getInitResourceHandlerList(HttpServletRequest req, HttpServletResponse res) throws ServletException
	{
		return(new ArrayList<ServiceHandlerResource>());
	}

	public void initResources(HttpServletRequest req, HttpServletResponse res) throws ServletException {
		getServiceHandlerResourceList().clear();
		getServiceHandlerResourceList().addAll(getInitResourceHandlerList(req,res));
		
		for(ServiceHandlerResource serviceHandlerResource:getServiceHandlerResourceList())
		{
			serviceHandlerResource.initResource(req,res,this);			
		}
			
	}

	public void closeResources(HttpServletRequest req, HttpServletResponse res) throws Exception{
		for(ServiceHandlerResource serviceHandlerResource:this.getServiceHandlerResourceList())
			serviceHandlerResource.closeResource(req,res,this);		
	}

	public ServiceRoute getServiceRoute() {
		return serviceRoute;
	}

	public void setServiceRoute(ServiceRoute serviceRoute) {
		this.serviceRoute = serviceRoute;
	}

	public void closeResourcesOnError(HttpServletRequest req, HttpServletResponse res) throws Exception {
		for(ServiceHandlerResource serviceHandlerResource:this.getServiceHandlerResourceList())
			serviceHandlerResource.closeResourceOnError(req,res,this);			
	}

	public List<Interceptor> getInterceptorList() {
		return interceptorList;
	}
	public void setInterceptorList(List<Interceptor> interceptorList) {
		this.interceptorList = interceptorList;
	}

	public SessionFactoryInterface getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactoryInterface sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
