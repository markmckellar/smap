package org.smap.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SessionFactoryInMemory  implements SessionFactoryInterface {

	@Override
	public SessionInterface getNewSession(HttpServletRequest request, HttpServletResponse response) throws Exception {
			return(new SessionInMemory(request,response));		
	}


}
