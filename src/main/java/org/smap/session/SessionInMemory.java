package org.smap.session;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.serviceresource.EmptyServiceHandlerResource;
import org.smap.serviceresource.ServiceHandlerResource;
import org.smap.util.Log;




public class SessionInMemory implements SessionInterface {

	private HttpServletRequest request;
	private HttpServletResponse response;
	EmptyServiceHandlerResource emptyServiceHandlerResource;
	
	 public SessionInMemory(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.emptyServiceHandlerResource = new EmptyServiceHandlerResource();
	 }
	 
	 public String getIdString() {
		 return("SessionInMemory:"+request.getSession().getId());
	 }
	
	public void invalidate() throws ServletException {
		request.getSession().invalidate();
	}


	@Override
	public void closeSession() throws ServletException {
		Log.info(  getIdString()+":closeSession");
	}

	@Override
	public boolean doesAttributeExist(String attributeKey) {		
		boolean doesAttributeExist = ! ( request.getSession().getAttribute(attributeKey)==null);

		return(doesAttributeExist);
	}

	@SuppressWarnings("unchecked")
	@Override
    public <T> T  getAttribute(String attributeKey,Class<T> objectClass) {
		return((T)request.getSession().getAttribute(attributeKey) );
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
		Log.info(getIdString()+":setAttribute:Setting atribute:"+attributeKey+":value="+attributeObject.toString());
		
		request.getSession().setAttribute(attributeKey,attributeObject);	
		
		Log.info(getIdString()+":setAttribute:Setting atribute:"+attributeKey+":value="+attributeObject.toString());
	}

	//@Override
	//public ServiceHandlerResource getServiceHandlerResource() {
	//	return this.emptyServiceHandlerResource;
	//}

	@Override
	public void initSession() throws ServletException {		
	}

	@Override
	public String getSessionKey() {
		return(this.getRequest().getSession().getId());
	}

}
