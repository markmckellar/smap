package org.smap.service.validatelogin;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.interceptor.Interceptor;
import org.smap.service.RequestTypeHandler;
import org.smap.service.StatusMessageResponse;
import org.smap.serviceroute.ServiceRoute;
import org.smap.session.SessionFactoryInterface;
import org.smap.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class IsSessionValidGetRequestHandler extends RequestTypeHandler {
	
	public IsSessionValidGetRequestHandler(ServiceRoute serviceRoute,SessionFactoryInterface sessionFactory) {
		super(ServiceTypeEnum.GET, serviceRoute,new ArrayList<Interceptor>(),sessionFactory);
	}

	public void handleRequestType(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Gson gson = new Gson();
		Type statusMessageResponseType = new TypeToken<StatusMessageResponse>() {}.getType();
	    Log.debug( "CheckForValidSession: " + req.getSession().getId() );
	    
	    boolean isValidated = false;
	    if(req.getSession().getAttribute(ValidateLoginRequestHandler.SESSION_VALIDATED)!=null)
	    {
	    	isValidated = (boolean)req.getSession().getAttribute(ValidateLoginRequestHandler.SESSION_VALIDATED);
	    }
	    	    
 		StatusMessageResponse statusMessageResponse = new StatusMessageResponse("is valid session",isValidated,"");			
		String json = gson.toJson(statusMessageResponse,statusMessageResponseType);		
		writeStringResponseJson(res,json);   
	}

}
