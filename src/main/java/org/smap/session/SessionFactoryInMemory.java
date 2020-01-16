package org.smap.session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SessionFactoryInMemory  implements SessionFactoryInterface {

	@Override
	public SessionInterface getNewSession(HttpServletRequest request, HttpServletResponse response) throws ServletException {
			return(new SessionInMemory(request,response));		
	}


}
