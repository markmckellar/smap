package org.smap.session;

import javax.servlet.http.HttpServletRequest;

public interface SessionFactoryInterface {
	SessionInterface getSession(HttpServletRequest req);
}
