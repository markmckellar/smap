package org.smap.serviceresource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JndiSqlServcieHandlerFactory extends SqlServcieHandlerFactory
{
	private String contextName;
	
	
	Context initCtx;


	public SqlServcieHandlerResorce getSqlServiceHandlerResourceInstance(HttpServletRequest req,
			HttpServletResponse res) throws ServletException {
		return(getJndiSqlServcieHandlerResorceInstance());
	}

	public JndiSqlServcieHandlerFactory(String contextName) throws Exception
	{
		this.setContextName(contextName);
        this.initCtx = new InitialContext();
	}
	
	public JndiSqlServcieHandlerResorce getJndiSqlServcieHandlerResorceInstance()
	{
		return(new JndiSqlServcieHandlerResorce(getContextName(),this.initCtx));
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
