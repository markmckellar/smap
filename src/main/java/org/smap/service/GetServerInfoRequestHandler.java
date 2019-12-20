package org.smap.service;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.interceptor.Interceptor;
import org.smap.serviceroute.ServiceRoute;
import org.smap.session.SessionFactoryInterface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GetServerInfoRequestHandler extends RequestTypeHandler
{	
	public GetServerInfoRequestHandler(ServiceTypeEnum serviceType,ServiceRoute serviceRoute,SessionFactoryInterface sessionFactory) {
		super(serviceType,serviceRoute,new ArrayList<Interceptor>());
	}	
	
	public void populateInfoMap(HttpServletRequest req, HttpServletResponse res,Map<String,Object> infoMap)
	{
		infoMap.put("tomcat:servletApiVersionMajorVersion",req.getSession().getServletContext().getMajorVersion());
		infoMap.put("tomcat:servletApiVersionMinorVersion",req.getSession().getServletContext().getMinorVersion());
		infoMap.put("tomcat:serverTime",new Date());
	}

	public void handleRequestType(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
 		StatusMessageResponse statusMessageResponse = new StatusMessageResponse("Error",false,"");
 		
		Type statusMessageResponseType = new TypeToken<StatusMessageResponse>() {}.getType();
		Type infoMapType = new TypeToken<Map<String,Object>>() {}.getType();
		
		Map<String,Object> infoMap = new HashMap<String,Object>();
		populateInfoMap(req,res,infoMap);

		String infoMapJson = gson.toJson(infoMap,infoMapType);		
		statusMessageResponse.setMessage(infoMapJson);
		String json = gson.toJson(statusMessageResponse,statusMessageResponseType);	
		
		writeStringResponseJson(res,json);		
		
	}

}
