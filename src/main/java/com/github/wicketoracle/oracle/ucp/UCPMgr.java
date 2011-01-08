package com.github.wicketoracle.oracle.ucp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.exception.NotInstantiableException;
import com.github.wicketoracle.oracle.util.CloseResource;

import oracle.ucp.UniversalConnectionPoolAdapter;
import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.admin.UniversalConnectionPoolManagerMBean;
import oracle.ucp.admin.UniversalConnectionPoolManagerMBeanImpl;
import oracle.ucp.jdbc.LabelableConnection;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

public final class UCPMgr
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( UCPMgr.class );

    private static UniversalConnectionPoolManagerMBean ucpPoolMgr = null;

    private static PoolDataSource ucpDataSource = null;

    /**
      * This is a non-instantiable utility class.
      */
    protected UCPMgr() throws NotInstantiableException
    {
        throw new NotInstantiableException();
    }

    public static PoolDataSource getUCPDataSource()
    {
        return ucpDataSource;
    }

    public static UniversalConnectionPoolManagerMBean getUCPPoolMgr()
    {
        return ucpPoolMgr;
    }

    public static void setupUCP
    (
        final int    pAbandonedConnectionTimeOut
    ,   final String pConnectionFactoryClassName
    ,   final String pConnectionPoolName
    ,   final String pUrl
    ,   final int    pInitialPoolSize
    ,   final int    pMaxCachedStatements
    )
    throws UniversalConnectionPoolException , SQLException
    {
        LOGGER.info( "Setup invoked" );

        ucpPoolMgr = UniversalConnectionPoolManagerMBeanImpl.getUniversalConnectionPoolManagerMBean();

        LOGGER.info( "UniversalConnectionPoolManagerMBeanImpl instance retrieved" );

        ucpDataSource = PoolDataSourceFactory.getPoolDataSource();

        LOGGER.info( "UCP pool datasource retrieved" );

        ucpDataSource.setConnectionFactoryClassName( pConnectionFactoryClassName );

        LOGGER.info( "UCP Connection factory class name set -> {}" , pConnectionFactoryClassName );

        ucpDataSource.setConnectionPoolName( pConnectionPoolName );

        LOGGER.info( "UCP pool name set -> {}" , pConnectionPoolName );

        ucpDataSource.setURL( pUrl );

        LOGGER.info( "UCP Url set -> {}" , pUrl );

        if ( pInitialPoolSize > 0 )
        {
            ucpDataSource.setInitialPoolSize( pInitialPoolSize );

            LOGGER.info( "UCP Initial Pool Size set -> {}" , pInitialPoolSize );
        }

        ucpDataSource.setMaxStatements( pMaxCachedStatements );

        LOGGER.info( "UCP Maximum cached statements set -> {}" , pMaxCachedStatements );

        if ( pAbandonedConnectionTimeOut >= 0 )
        {
            ucpDataSource.setAbandonedConnectionTimeout( pAbandonedConnectionTimeOut );

            LOGGER.info( "UCP abandoned connection timeout set -> {}" , pAbandonedConnectionTimeOut );
        }

        ucpPoolMgr.createConnectionPool( ( UniversalConnectionPoolAdapter ) ucpDataSource );

        LOGGER.info( "UCP pool created" );

        ucpDataSource.registerConnectionLabelingCallback( new UCPConnectionLabellingCallback() );

        LOGGER.info( "Connection Labelling Callback handler registered" );
    }

    public static Connection getLabelledConnection( final String pUsername , final String pPassword ) throws SQLException
    {
        final String singleQuote = "\"";
        final String username = singleQuote + pUsername + singleQuote;
        final String password = singleQuote + pPassword + singleQuote;

        /* if an existing connection has these labels, then we'll probably use it */
        final Properties requestLabels = new Properties();

        requestLabels.put( UCPConnectionLabelKey.USERNAME.toString() , pUsername );

        LOGGER.debug( "Request oracle connection -> username -> {}" , pUsername );

        final Connection dbConn = ucpDataSource.getConnection( username , password , requestLabels );

        LOGGER.debug( "Successfully acquired oracle connection -> username -> {}" , pUsername );

        final LabelableConnection lDbConn = ( LabelableConnection ) dbConn;

        if ( lDbConn.getConnectionLabels() == null )
        {
            LOGGER.debug( "Connection does not have any labels applied" );

            final String dbStatement =   " BEGIN "
                                       + "   SYS.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                       + "   app_utility.pk_session_utility.pr_configure_session( p_client_info => ? ); "
                                       + " END;  ";

            CallableStatement dbCstmt = null;

            try
            {
                dbConn.setAutoCommit( false );

                LOGGER.debug( "Autocommit set to false" );

                dbCstmt = dbConn.prepareCall( dbStatement );
                dbCstmt.setString( 1 , "PK_SESSION_UTILITY" );
                dbCstmt.setString( 2 , "PR_CONFIGURE_SESSION" );
                dbCstmt.setString( 3 , "Oracle/Wicket webapp" );
                dbCstmt.execute();

                LOGGER.debug( "Configuration stored procedure executed" );

                lDbConn.applyConnectionLabel( UCPConnectionLabelKey.USERNAME.toString() , pUsername );

                LOGGER.debug( "Connection labels applied" );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception whilst configuring connection -> {}; error code -> {}; sql state -> {}; username ->{}"
                ,   new Object [ ]
                    {
                        sqle.getMessage()
                    ,   sqle.getErrorCode()
                    ,   sqle.getSQLState()
                    ,   pUsername
                    }
                );

                throw sqle;
            }
            finally
            {
                CloseResource.close( dbCstmt );
            }
        }
        return dbConn;
    }

    /**
     * Do a run time configuration of the application's Universal Connection Pool
     *
     * @param pMinPoolSize
     * @param pMaxPoolSize
     * @throws SQLException
     */
    public static void configure
    (
        final int pMinPoolSize
    ,   final int pMaxPoolSize
    ,   final int pMaxCachedStatements
    )
    throws SQLException
    {
        ucpDataSource.setMinPoolSize( pMinPoolSize );
        ucpDataSource.setMaxPoolSize( pMaxPoolSize );
        ucpDataSource.setMaxStatements( pMaxCachedStatements );
    }
}
