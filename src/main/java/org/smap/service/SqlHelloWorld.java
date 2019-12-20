package org.smap.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smap.db.dao.QueryTimer;
import org.smap.serviceresource.SqlServcieHandlerFactory;
import org.smap.serviceroute.ServiceRoute;
import org.smap.session.SessionFactoryInterface;
import org.smap.util.Log;


public class SqlHelloWorld extends SqlServiceHandler {

	public SqlHelloWorld(ServiceTypeEnum serviceType,
			SqlServcieHandlerFactory sqlServcieHandlerFactory,
			ServiceRoute serviceRoute) {
		super(serviceType, sqlServcieHandlerFactory,serviceRoute);
	}
	
	@Override
	public void handleRequestType(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try
		{
			writeStringResponse(res,
				"Hello world connection="+this.getSqlServcieHandlerResorce().getConnection()+
				":timestamp="+getTimeStampString(this.getSqlServcieHandlerResorce().getConnection()),
				ResponseType.TextPlain);
		}
		catch(Exception e)
		{
			throw new ServletException(e);
		}
	}
	
	public String getTimeStampString(Connection con) throws Exception	
	{
		String sql = "SELECT SYSTIMESTAMP timestamp FROM DUAL";
		String tsString = "";
		
		Log.debug("TgSessionDataDAO:TgMailedDocument:sql="+sql);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet res = QueryTimer.executePreparedStatment(stmt,sql);
		while ( res.next() )
		{
			Timestamp tsTemp = res.getTimestamp("timestamp");
			tsString = tsTemp.toString();
		}    
		return(tsString);
	}

}
