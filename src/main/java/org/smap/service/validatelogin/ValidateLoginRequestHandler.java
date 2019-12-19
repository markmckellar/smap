package org.smap.service.validatelogin;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.service.SqlServiceHandler;
import org.smap.service.StatusMessageResponse;
import org.smap.serviceresource.SqlServcieHandlerFactory;
import org.smap.serviceroute.ServiceRoute;
import org.smap.session.SessionFactoryInterface;
import org.smap.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public abstract class ValidateLoginRequestHandler extends SqlServiceHandler
{
	
	public static final String SESSION_VALIDATED = "validated";

	public ValidateLoginRequestHandler(ServiceTypeEnum serviceType,SqlServcieHandlerFactory sqlServcieHandlerFactory,ServiceRoute serviceRoute,SessionFactoryInterface sessionFactory) { 
		super(serviceType,sqlServcieHandlerFactory,serviceRoute,sessionFactory);
	}
	
	public abstract void setValidatedSessionInfo(LoginCredentials loginCredentials,HttpServletRequest req,HttpServletResponse res) throws ServletException;
	
	public abstract StatusMessageResponse validateLoginCredentialValid(LoginCredentials loginCredentials,
			HttpServletRequest req,HttpServletResponse res) throws ServletException;

	public void handleRequestType(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		Gson gson = new Gson();
		Type loginCredentialsType = new TypeToken<LoginCredentials>() {}.getType();
		Type statusMessageResponseType = new TypeToken<StatusMessageResponse>() {}.getType();
		
		String jsonString = getStringParmaterFromPostRequest(req);
		LoginCredentials loginCredentials = gson.fromJson(jsonString,loginCredentialsType);
		//LoginCredentials loginCredentials = gson.fromJson(jsonString, LoginCredentials.class);
		Log.info(loginCredentials.toString());
		
		StatusMessageResponse statusMessageResponse = validateLoginCredentialValid(loginCredentials,req,res);
		if(statusMessageResponse.isSuccess())
		{
			req.getSession().setAttribute(SESSION_VALIDATED,true);
			setValidatedSessionInfo(loginCredentials,req,res);
		}
		String json = gson.toJson(statusMessageResponse,statusMessageResponseType);		
		writeStringResponseJson(res,json);
		
	}


}