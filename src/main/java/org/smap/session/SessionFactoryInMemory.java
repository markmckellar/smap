package org.smap.session;

import javax.servlet.http.HttpServletRequest;

public class SessionFactoryInMemory  implements SessionFactoryInterface {

	@Override
	public SessionInterface getSession(HttpServletRequest req) {
			return(new SessionInMemory());		
	}

}
