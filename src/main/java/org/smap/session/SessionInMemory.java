package org.smap.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SessionInMemory implements SessionInterface {

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public SessionInMemory(HttpServletRequest request,HttpServletResponse response) {
		this.setRequest(request);
		this.setResponse(response);
	}
	
	@Override
    public void closeSession() throws Exception {
		
	}

	
	@Override
	public Object getAttribute(String attributeKey) {
		return(getRequest().getSession().getAttribute(attributeKey) );
	}

	@Override
	public void setAttribute(String attributeKey, Object attributeObject) {
		getRequest().setAttribute(attributeKey,attributeObject);	
	}

	@Override
	public boolean doesAttributeExist(String attributeKey) {
		return( !((getRequest().getSession().getAttribute(attributeKey)) == null) );
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	
	

}
