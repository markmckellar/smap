package org.smap.session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.serviceresource.ServiceHandlerResource;


public interface SessionInterface {
	public String getSessionKey();
	public void initSession() throws ServletException;
	public HttpServletRequest getRequest();
	public HttpServletResponse getResponse();
    public <T> T  getAttribute(String attributeKey,Class<T> objectClass);
    public void setAttribute(String attributeKey,Object attributeObject) throws ServletException;
    public boolean doesAttributeExist(String attributeKey);
    public void closeSession() throws ServletException;
    public void invalidate() throws ServletException;
}
