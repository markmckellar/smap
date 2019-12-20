package org.smap.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionFactoryInterface {
	SessionInterface getNewSession(HttpServletRequest req, HttpServletResponse res) throws Exception;
}
