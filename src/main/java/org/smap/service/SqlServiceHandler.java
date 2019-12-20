package org.smap.service;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.interceptor.Interceptor;
import org.smap.serviceresource.ServiceHandlerResource;
import org.smap.serviceresource.SqlServcieHandlerFactory;
import org.smap.serviceresource.SqlServcieHandlerResorce;
import org.smap.serviceroute.ServiceRoute;
import org.smap.session.SessionFactoryInterface;

public abstract class SqlServiceHandler extends RequestTypeHandler 
{
	private SqlServcieHandlerResorce sqlServcieHandlerResorce;
	private SqlServcieHandlerFactory sqlServcieHandlerFactory;

	public SqlServiceHandler(SqlServiceHandler sqlServiceHandler) {
		super(sqlServiceHandler.getServiceType(),
				sqlServiceHandler.getServiceRoute(),sqlServiceHandler.getInterceptorList());
		this.setSqlServcieHandlerFactory(sqlServiceHandler.sqlServcieHandlerFactory);
	}
	
	public SqlServiceHandler(ServiceTypeEnum serviceType,
			SqlServcieHandlerFactory sqlServcieHandlerFactory,
			ServiceRoute serviceRoute) {
		super(serviceType,serviceRoute,new ArrayList<Interceptor>());
		this.setSqlServcieHandlerFactory(sqlServcieHandlerFactory);
	}
	
	public List<ServiceHandlerResource> getInitResourceHandlerList(HttpServletRequest req, HttpServletResponse res) throws ServletException
	{
		List<ServiceHandlerResource> initResourceHandlerList = super.getInitResourceHandlerList(req,res);
		this.setSqlServcieHandlerResorce(this.getSqlServcieHandlerFactory().getSqlServiceHandlerResourceInstance(req, res));
		initResourceHandlerList.add(getSqlServcieHandlerResorce());
		return(initResourceHandlerList);
	}

	public SqlServcieHandlerResorce getSqlServcieHandlerResorce() {
		return sqlServcieHandlerResorce;
	}

	public void setSqlServcieHandlerResorce(SqlServcieHandlerResorce sqlServcieHandlerResorce) {
		this.sqlServcieHandlerResorce = sqlServcieHandlerResorce;
	}

	public SqlServcieHandlerFactory getSqlServcieHandlerFactory() {
		return sqlServcieHandlerFactory;
	}

	public void setSqlServcieHandlerFactory(SqlServcieHandlerFactory sqlServcieHandlerFactory) {
		this.sqlServcieHandlerFactory = sqlServcieHandlerFactory;
	}
	

}
