package org.smap.session;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.db.dao.QueryTimer;
import org.smap.service.RequestTypeHandler;
import org.smap.service.SqlHelloWorld;
import org.smap.service.RequestTypeHandler.ServiceTypeEnum;
import org.smap.serviceresource.ServiceHandlerResource;
import org.smap.serviceresource.SqlServcieHandlerFactory;
import org.smap.serviceresource.SqlServcieHandlerResorce;
import org.smap.session.SessionData;
import org.smap.session.SessionInterface;
import org.smap.util.Log;


public abstract class DataBaseSessionExternal implements SessionInterface {
	public static Charset DATA_ENCODING = StandardCharsets.UTF_8; 
	private HttpServletRequest request;
	private HttpServletResponse response;
	private SessionData sessionData;
	private double expireSessionInDays;
	String sessionCookieName; 
	private boolean sessionShouldBeInvalidated;
	CloseSessionRequestHandler closeSessionRequestHandler;
	InitSessionRequestHandler initSessionRequestHandler;
	ClearSessionStorageRequestHandler clearSessionStorageRequestHandler;
	
	public static void main(String[] args) throws Exception {

    	SessionData sd1 = new SessionData();
    	Map<String,Object> testMap = new HashMap<String,Object>();
    	testMap.put("aaa",new Boolean(false));
    	testMap.put("bbb",new Integer(1));
    	sd1.setSessionAttributeWithClass("map", testMap);
    	System.out.println("sd1.json="+sd1.getJsonString());

    	Map<String,Object> testMap2 = sd1.getSessionAttributeByKey("map", HashMap.class);

    	System.out.println("testMap2.value="+testMap2.toString());
    	
    	
    	sd1.setSessionAttributeWithClass("bool", true);
    	boolean bool = sd1.getSessionAttributeByKey("bool", Boolean.class);
    	System.out.println("bool="+bool);

	}
	
	public DataBaseSessionExternal(HttpServletRequest request, HttpServletResponse response,SqlServcieHandlerFactory sqlServcieHandlerFactory,
			double expireSessionInDays,String sessionCookieName)  {
		 this.request = request;
		 this.response = response;
		 this.expireSessionInDays = expireSessionInDays;	
		 this.sessionCookieName = sessionCookieName;
		 this.sessionShouldBeInvalidated = false;
		 closeSessionRequestHandler = new CloseSessionRequestHandler(sqlServcieHandlerFactory,null);
		 initSessionRequestHandler = new InitSessionRequestHandler(sqlServcieHandlerFactory,null);
		 clearSessionStorageRequestHandler = new ClearSessionStorageRequestHandler(sqlServcieHandlerFactory,null);
	}
	
	@Override
	public void initSession() throws ServletException {
		
		RequestTypeHandler requestTypeHandler = initSessionRequestHandler.getNewInstance(null,request,response);
		InitSessionRequestHandler ish = (InitSessionRequestHandler)requestTypeHandler;
		
		Log.debug(getIdStringBase()+":initSession:processing:1"+":req="+request.hashCode());
		ish.setServiceRoute(null);				
		Log.debug(getIdStringBase()+":initSession:processing:2"+":req="+request.hashCode());
		ish.setSession(null);
		ish.setDataBaseSessionHandler(this);
		Log.debug(getIdStringBase()+":initSession:processing:3"+":req="+request.hashCode());
		try {
			ish.processRequest(request,response);
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}
	
	public void initSession(Connection connection) throws Exception {	
		String sessionKeyFromCookie = getSessionCookie(request,sessionCookieName);	
		 Log.debug(this.getIdString()+":initSession:"+"found cookie via:getSessionCookie:sessionCookieName:"+sessionKeyFromCookie);
		 
		 if(!doesSessionExistsInDB(connection,sessionKeyFromCookie)) {
			 Log.debug(this.getIdString()+":session was not in DB:"+"sessionCookieName:"+sessionKeyFromCookie);

			 SessionData sd = new SessionData();
			 setSessionData(sd);
			
			 if(sessionKeyFromCookie.isEmpty()) {
				 Log.debug(this.getIdString()+":session key NOT was empty, earsing cookie");
				 Cookie eraseSessionCookie = new Cookie(sessionCookieName, "");
				 eraseSessionCookie.setMaxAge(0);
				 //eraseSessionCookie.setPath( "/" );
				 response.addCookie(eraseSessionCookie);
			 }
			 
			 
			 Cookie newSessionCookie = new Cookie(sessionCookieName,getSessionData().getSessionKey());
			 newSessionCookie.setMaxAge(-1);
			 //newSessionCookie.setPath( "/" );
			 response.addCookie(newSessionCookie);
			 
		 }
		 else {
			 Log.debug(getIdString()+":initSession:session was found in DB:"+"sessionCookieName:"+sessionKeyFromCookie);

			 setSessionData(readSessionDataFromDb(connection,sessionKeyFromCookie));
		 }
	}
	
   
		
	public String getSessionCookie(HttpServletRequest request,String sessionCookieName) {
		String sessionCookieValue = "";
		if(request.getCookies()==null) return(sessionCookieValue);
		
		for(Cookie cookie:request.getCookies()) {
			Log.debug(this.getIdString()+":all cookies:cookie info:"+this.getCookieString(cookie));		
		}
		
		for(Cookie cookie:request.getCookies()) {
			Log.debug(this.getIdString()+":searching for:sessionCookieName="+sessionCookieName+":cookies:cookie with a name of:"+cookie.getName()+":value is:"+cookie.getValue());
			if(cookie.getName().equals(sessionCookieName)) { 
				sessionCookieValue = cookie.getValue();
				break;
			}
		}
		return(sessionCookieValue);
	}
	
	private String getCookieString(Cookie cookie) {
		return(
				":getDomain="+cookie.getDomain()+
				":getName="+cookie.getName()+
				":getPath="+cookie.getPath()+
				":getSecure="+cookie.getSecure()+
				":getMaxAge="+cookie.getMaxAge()+
				":getValue="+cookie.getValue()+
				":getVersion="+cookie.getVersion()+
				""
				);
	}
	
	public abstract SessionData readSessionDataFromDb(Connection connection,String sessionKey) throws Exception;
    public abstract void updateSessionInDB(Connection connection) throws Exception;
    public abstract void insertSessionToDB(Connection connection) throws Exception;
    public abstract void clearSessionStorage(Connection connection) throws Exception;

	public boolean doesSessionExistsInDB(Connection connection,String sessionKey) throws Exception
    {
		return(readSessionDataFromDb(connection,sessionKey)!=null);
    }
	 

    
    
    @Override
	public void closeSession() throws ServletException {
    	Log.debug(getIdStringBase()+":closeSession:processing:1"+":req="+request.hashCode());
    	closeSessionObject();		
    	closeSessionClearSessionStorage();
		Log.debug(getIdStringBase()+":closeSession:processing:2"+":req="+request.hashCode());
		
	}
    
    protected void closeSessionClearSessionStorage() throws ServletException {
    	RequestTypeHandler requestTypeHandler = clearSessionStorageRequestHandler.getNewInstance(null,request,response);
    	ClearSessionStorageRequestHandler css = (ClearSessionStorageRequestHandler)requestTypeHandler;
		
		Log.debug(getIdStringBase()+":closeSessionClearSessionStorage:processing:1"+":req="+request.hashCode());
		css.setServiceRoute(null);				
		Log.debug(getIdStringBase()+":closeSessionClearSessionStorage:processing:2"+":req="+request.hashCode());
		css.setSession(null);
		css.setDataBaseSessionHandler(this);
		Log.debug(getIdStringBase()+":closeSessionClearSessionStorage:processing:3"+":req="+request.hashCode());
		try {
			css.processRequest(request,response);
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
    }
    
    protected void closeSessionObject() throws ServletException {
    	RequestTypeHandler requestTypeHandler = closeSessionRequestHandler.getNewInstance(null,request,response);
    	CloseSessionRequestHandler csh = (CloseSessionRequestHandler)requestTypeHandler;
		
		Log.debug(getIdStringBase()+":closeSessionObject:processing:1"+":req="+request.hashCode());
		csh.setServiceRoute(null);				
		Log.debug(getIdStringBase()+":closeSessionObject:processing:2"+":req="+request.hashCode());
		csh.setSession(null);
		csh.setDataBaseSessionHandler(this);
		Log.debug(getIdStringBase()+":closeSessionObject:processing:3"+":req="+request.hashCode());
		try {
			csh.processRequest(request,response);
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
    }
    
	public void closeSession(Connection connection) throws ServletException {
		Log.debug(  getIdString()+":closeSession:start");
		try {
			if(this.isSessionShouldBeInvalidated()) this.setExpireSessionInDays(0.0);
			
			if(doesSessionExistsInDB(connection,this.getSessionKey())) {
				Log.debug(  getIdString()+":closeSession:session was in db (doing update)");

				this.updateSessionInDB(connection);
			}
			else {
				Log.debug(  getIdString()+":closeSession:session was new (doing insert)");

				if(this.getSessionData().doesSessionHaveData()) this.insertSessionToDB(connection);
			}
		}
		catch(Exception e){
		}
	}
	
	public String getIdStringBase() {
		return("DataBaseSessionExternal");
	}
		
	 public String getIdString() {
		 return(getIdStringBase()+":"+this.getSessionKey()+":req="+request.hashCode());		 
	 }
	
	public void invalidate() throws ServletException{
		Log.debug(  getIdString()+":invalidate");
		this.sessionShouldBeInvalidated = true;
	}
	

	@Override
	public boolean doesAttributeExist(String attributeKey) {
		boolean doesAttributeExist = this.getSessionData().doesAttributeExist(attributeKey);
		return(doesAttributeExist);
	}

	@Override
    public <T> T  getAttribute(String attributeKey,Class<T> objectClass) {
		return( this.getSessionData().getSessionAttributeByKey(attributeKey, objectClass) );
	}

	@Override
	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public void setAttribute(String attributeKey, Object attributeObject) throws ServletException {
		sessionData.setSessionAttributeWithClass(attributeKey,attributeObject);					
	}
	

	public SessionData getSessionData() {
		return sessionData;
	}

	public void setSessionData(SessionData sessionData) {
		this.sessionData = sessionData;
	}


	public double getExpireSessionInDays() {
		return expireSessionInDays;
	}

	public void setExpireSessionInDays(double expireSessionInDays) {
		this.expireSessionInDays = expireSessionInDays;
	}


	@Override
	public String getSessionKey() {
		String sessionKey = "";
		if(this.getSessionData()!=null) {
			sessionKey = this.getSessionData().getSessionKey();
		}
		return(sessionKey);
	}

	public boolean isSessionShouldBeInvalidated() {
		return sessionShouldBeInvalidated;
	}

	public void setSessionShouldBeInvalidated(boolean sessionShouldBeInvalidated) {
		this.sessionShouldBeInvalidated = sessionShouldBeInvalidated;
	}



}
