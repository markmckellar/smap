package org.smap.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface SessionInterface {
	
	public HttpServletRequest getRequest();
	public HttpServletResponse getResponse();
    public Object getAttribute(String attributeKey);
    public void setAttribute(String attributeKey,Object attributeObject);
    public boolean doesAttributeExist(String attributeKey);
    public void closeSession() throws Exception;

}
