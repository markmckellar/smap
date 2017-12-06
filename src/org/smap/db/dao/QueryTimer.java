package org.smap.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.smap.util.Log;


public class QueryTimer
{
	public static ResultSet ExecuteStatment(Statement stmt,String sql) throws Exception
	{
		return(ExecuteStatment(stmt,sql,true));
	}

	public static ResultSet ExecuteStatment(Statement stmt,String sql,boolean addNewLine) throws Exception
	{		
		Date startTime = new Date();
		ResultSet res = null;
		try
		{
			res = stmt.executeQuery(sql);	    
	    	long elapsedTime = new java.util.Date().getTime() - startTime.getTime();	    
	    	Log.debug("QueryTimer:ExecuteStatment:"+getCallerInfo()+":sql elapsedTime:"+elapsedTime+":sql:"+sql);
	    	if(addNewLine) Log.debug("");
		}
		catch(Exception e)
		{
			System.out.println("QueryTimer:ExecuteStatment:"+getCallerInfo()+":Error:sql="+sql);
			throw(e);
		}
	    return(res);
	}
	
	public static ResultSet executePreparedStatment(PreparedStatement stmt,String sql) throws Exception
	{
		Date startTime = new Date();
	    ResultSet res = stmt.executeQuery();	    
	    long elapsedTime = new java.util.Date().getTime() - startTime.getTime();
	    Log.debug("QueryTimer:ExecuteStatment:"+getCallerInfo()+":elapsedTime:"+elapsedTime+":sql:"+sql);
	    return(res);
	}
	
	public static ResultSet executePreparedStatment(PreparedStatement stmt) throws Exception
	{
	    return(executePreparedStatment(stmt,"?"));
	}
	
	
	public static void ExecuteUpdate(PreparedStatement stmt,String sql) throws Exception
	{
		Date startTime = new Date();
		try
		{
			stmt.executeUpdate();
		}
		catch(Exception e)
		{
			Log.debug("Error on insert:sql="+sql);
			throw(e);
		}
	    long elapsedTime = new java.util.Date().getTime() - startTime.getTime();
	    Log.debug("QueryTimer:ExecuteUpdate:"+getCallerInfo()+":elapsedTime:"+elapsedTime);
	}
	
	public static String getCallerInfo() {

	      String callerInfo = "DefaultName-callerUnknown";
	      // use class name instead of filename since filename is null for Sun/Java
	      // classes when running in a JRE-only context
	      String thisClassName = "myriad.mmm.reporting.db.QueryTimer";
	      Thread thread = Thread.currentThread();
	      StackTraceElement[] framesArray = thread.getStackTrace();

	      // look for the last stack frame from this class and then whatever is next
	      // is the caller we want to know about
	      for (StackTraceElement stackFrame : framesArray) {

	         // filter out Thread because we just created a couple frames using
	         // Thread
	         if (!stackFrame.getClassName().equals("java.lang.Thread")
	             && !stackFrame.getClassName().equals(thisClassName)) {

	            // handle case for file name when debug info is missing from
	            // classfile
	            String fileName = stackFrame.getFileName() != null ? stackFrame.getFileName()
	                                                              : "Unknown";
	            StringBuilder sb = new StringBuilder(stackFrame.getMethodName());
	            sb.append('(');	            
	            sb.append(fileName);
	            sb.append(':');
	            // if no debug info, returns a negative number
	            sb.append(stackFrame.getLineNumber());
	            sb.append(')');
	            callerInfo = sb.toString();
	            break;
	         }
	      }
	      return callerInfo;
	   }
}
