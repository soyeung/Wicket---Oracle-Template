package com.github.wicketoracle.oracle.ucp;

import java.sql.SQLException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.ucp.UniversalConnectionPoolException;

public final class UCPConfigServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger( UCPConfigServlet.class );

    public void init()
    {
        final int    abandonedConnectionTimeOut = Integer.parseInt( getInitParameter( "ucp-abandoned-connection-timeout" ) );
        final String connectionFactoryClassName = getInitParameter( "ucp-connection-factory-class-name" );
        final String connectionPoolName         = getInitParameter( "ucp-connection-pool-name" );
        final String connectionPoolURL          = getInitParameter( "ucp-connection-pool-url" );
        final int    initialPoolSize            = Integer.parseInt( getInitParameter( "ucp-initial-pool-size" ) );
        final int    maxCachedStatements        = Integer.parseInt( getInitParameter( "ucp-max-cached-statements" ) );

        LOGGER.info( "Connection Factory Class Name -> {}" , connectionFactoryClassName );
        LOGGER.info( "Connection Pool Name -> {}"          , connectionPoolName );
        LOGGER.info( "Connection Pool URL -> {}"           , connectionPoolURL );
        LOGGER.info( "Initial Pool Size -> {}"             , initialPoolSize );
        LOGGER.info( "Max Cached Statements -> {}"         , maxCachedStatements );

        try
        {
            UCPMgr.setupUCP
            (
                abandonedConnectionTimeOut
            ,   connectionFactoryClassName
            ,   connectionPoolName
            ,   connectionPoolURL
            ,   initialPoolSize
            ,   maxCachedStatements
            );
        }
        catch ( UniversalConnectionPoolException ucpe )
        {
            LOGGER.error( "Configuring Universal Connection Pool -> {} -> {}" , ucpe.getClass().toString() , ucpe.getLocalizedMessage() );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error( "Configuring Universal Connection Pool -> {} -> {}" , sqle.getClass().toString() , sqle.getLocalizedMessage() );
        }
    }
}
