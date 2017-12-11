package org.smap.util;

import java.io.PrintStream;

import org.apache.log4j.Logger;

public class Log
{

    private static boolean configured = false;

    private Log()
    {
    }

    public static void debug( Class<?> loggerClass, Object object )
    {
        getLogLogger( loggerClass ).debug( object );
    }

    public static void info( Class<?> loggerClass, Object object )
    {
        getLogLogger( loggerClass ).info( object );
    }

    public static void warn( Class<?> loggerClass, Object object )
    {
        getLogLogger( loggerClass ).warn( object );
    }

    public static void error( Class<?> loggerClass, Object object )
    {
        getLogLogger( loggerClass ).error( object );
    }

    public static void fatal( Class<?> loggerClass, Object object )
    {
        getLogLogger( loggerClass ).fatal( object );
    }

    public static void debug( Class<?> loggerClass, Object object, Throwable throwable )
    {
        getLogLogger( loggerClass ).debug( object, throwable );
    }

    public static void info( Class<?> loggerClass, Object object, Throwable throwable )
    {
        getLogLogger( loggerClass ).info( object, throwable );
    }

    public static void warn( Class<?> loggerClass, Object object, Throwable throwable )
    {
        getLogLogger( loggerClass ).warn( object, throwable );
    }

    public static void error( Class<?> loggerClass, Object object, Throwable throwable )
    {
        getLogLogger( loggerClass ).error( object, throwable );
    }

    public static void fatal( Class<?> loggerClass, Object object, Throwable throwable )
    {
        getLogLogger( loggerClass ).fatal( object, throwable );
    }

    public static void debug( Object object )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).debug( object );
    }

    public static void info( Object object )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).info( object );
    }

    public static void warn( Object object )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).warn( object );
    }

    public static void error( Object object )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).error( object );
    }

    public static void fatal( Object object )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).fatal( object );
    }

    public static void debug( Object object, Throwable throwable )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).debug( object, throwable );
    }

    public static void info( Object object, Throwable throwable )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).info( object, throwable );
    }

    public static void warn( Object object, Throwable throwable )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).warn( object, throwable );
    }

    public static void error( Object object, Throwable throwable )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).error( object, throwable );
    }

    public static void fatal( Object object, Throwable throwable )
    {
        getLogLogger( LoggedSecurityManager.getFrom() ).fatal( object, throwable );
    }

    public static void installUncaughtExceptionLogger()
    {
        Thread.setDefaultUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler()
        {
            public void uncaughtException( Thread t, Throwable e )
            {
                Log.error( "Uncaught Exception", e );
                e.printStackTrace();
            }
        } );
    }

    public static void tieSystemOutAndErrToLog()
    {
        if ( Boolean.getBoolean( "tieSystemOutAndErrToLog" ) )
        {
            System.setOut( new LogPrintStream( System.out ) );
            System.setErr( new LogPrintStream( System.err ) );
        }
    }

    private static Logger getLogLogger( Class<?> loggerClass )
    {
        configure();
        return Logger.getLogger( loggerClass );
    }

    private static void configure()
    {
        if ( configured == false )
        {
            // Log4JConfigurator.configure();
            configured = true;
        }
    }

    private static class LoggedSecurityManager extends SecurityManager
    {
        private static LoggedSecurityManager _instance;

        public static LoggedSecurityManager getInstance()
        {
            if ( _instance == null )
            {
                _instance = new LoggedSecurityManager();
            }
            return _instance;
        }

        public static Class<?> getFrom()
        {
            // 2 because first is this class, second is logger, third is logging
            // class.
            return getInstance().getClassContext()[2];
        }
    }

    private static class LogPrintStream extends PrintStream
    {
        //private PrintStream realPrintStream;

        private LogPrintStream( PrintStream realPrintStream )
        {
            super( realPrintStream );
            //this.realPrintStream = realPrintStream;
        }

        public void print( String string )
        {
            // realPrintStream.print( string );
            Log.info( string );
        }
    }
}
