package org.smap.session;

import javax.servlet.http.HttpServletRequest;


public class SessionInMemory implements SessionInterface {

	@Override
	public Object getAttribute(HttpServletRequest req, String attributeKey) {
		return(req.getSession().getAttribute(attributeKey) );
	}

	@Override
	public void setAttribute(HttpServletRequest req, String attributeKey, Object attributeObject) {
		req.setAttribute(attributeKey,attributeObject);
		
	}

	@Override
	public boolean doesAttributeExist(HttpServletRequest req, String attributeKey) {
		return( !((req.getSession().getAttribute(attributeKey)) == null) );
	}

}
