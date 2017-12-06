package org.smap.serviceresource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JndiSqlServcieHandlerFactory extends SqlServcieHandlerFactory
{
	private String contextName;

	public SqlServcieHandlerResorce getSqlServiceHandlerResourceInstance(HttpServletRequest req,
			HttpServletResponse res) throws ServletException {
		return(getJndiSqlServcieHandlerResorceInstance());
	}

	public JndiSqlServcieHandlerFactory(String contextName)
	{
		this.setContextName(contextName);
	}
	
	public JndiSqlServcieHandlerResorce getJndiSqlServcieHandlerResorceInstance()
	{
		return(new JndiSqlServcieHandlerResorce(getContextName()));
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	@Override
	public ServiceHandlerResource getServiceHandlerResourceInstance(HttpServletRequest req, HttpServletResponse res)
			throws ServletException {
		return(getJndiSqlServcieHandlerResorceInstance());
	}
}
