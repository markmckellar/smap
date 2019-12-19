package org.smap.session;

import javax.servlet.http.HttpServletRequest;


public interface SessionInterface {
	
    public Object getAttribute(HttpServletRequest req,String attributeKey);
    public void setAttribute(HttpServletRequest req,String attributeKey,Object attributeObject);
    public boolean doesAttributeExist(HttpServletRequest req,String attributeKey);

}
