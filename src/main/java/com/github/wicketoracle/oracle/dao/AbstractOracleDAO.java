package com.github.wicketoracle.oracle.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.oracle.ucp.UCPMgr;
import com.github.wicketoracle.oracle.util.CloseResource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import oracle.ucp.jdbc.ValidConnection;


/**
 * Base class which all Oracle Data Access Objects( DAOs ) should extend in order
 * that data access facilities operate in a uniform manner
 *
 * @author Andrew Hall
 *
 */
public abstract class AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractOracleDAO.class );

    private transient OracleConnection conn;

    private String username;

    /**
     * Constructor initialising a db connection
     *
     * @param pUsername
     *                  DB username
     * @param pPassword
     *                  DB password
     * @throws SQLException
     */

    protected AbstractOracleDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        conn = ( OracleConnection ) UCPMgr.getLabelledConnection( pUsername , pPassword );
    }

    /**
     *
     * @return
     *         application username
     */
    public final String getUsername()
    {
        return username;
    }

    /**
     * @return
     *         reference to db connection
     */
    protected final OracleConnection getConnection() throws SQLException
    {
        return conn;
    }

    /**
     * Commit uncommitted work to the db
     *
     * @throws SQLException
     */

    public final void doCommit() throws SQLException
    {
        conn.commit();
    }

    /**
     * Rollback uncommitted work in the db
     *
     * @throws SQLException
     */

    public final void doRollback() throws SQLException
    {
        conn.rollback();
    }

    /**
     * Return the connection to the Universal Connection Pool, making it accessible
     * to other threads. This method makes provision for Oracle Proxy user connections
     *
     * @throws SQLException
     */
    public final boolean closeConnection()
    {
        try
        {
            if ( !conn.isClosed() )
            {
                conn.close();
            }

            return true;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst closing a connection -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            return false;
        }
    }

    /**
     * mark the connection to be removed from the UCP when it is closed
     */
    public final void setConnectionInvalid() throws SQLException
    {
       ( ( ValidConnection ) getConnection() ).setInvalid();
    }

    /**
     * Manufacture a map of lists from a result set. Each list represents id/code value sets
     * @param pDbRs
     * @return
     * @throws SQLException
     */
    protected final Map <String , SelectChoiceList <IntegerSelectChoice>> getKeyValueRefData( final OracleResultSet pDbRs ) throws SQLException
    {
        Map <String , SelectChoiceList <IntegerSelectChoice>> mapKeyValueRefData  = new HashMap <String , SelectChoiceList <IntegerSelectChoice>>();
        SelectChoiceList <IntegerSelectChoice>                listKeyValueRefData = new SelectChoiceList <IntegerSelectChoice>();

        int count = 0;

        String prevKey = "";
        String currKey = "";

        while ( pDbRs.next() )
        {
            currKey = pDbRs.getString( "DATASETKEY" );

            if ( count == 0 )
            {
                prevKey = currKey;
            }

            if ( ! currKey.equals( prevKey ) )
            {
                if ( count > 0 )
                {
                    mapKeyValueRefData.put( prevKey , listKeyValueRefData );
                }

                listKeyValueRefData = new SelectChoiceList < IntegerSelectChoice >();

                prevKey = currKey;
            }

            listKeyValueRefData.add( new IntegerSelectChoice( pDbRs.getInt( "ID" ) , pDbRs.getString( "NAME" ) ) );

            count++;
        }

        if ( listKeyValueRefData.size() > 0 )
        {
            mapKeyValueRefData.put( currKey , listKeyValueRefData );
        }

        LOGGER.debug( "Reference data built : #sets of ref data -> {}" , mapKeyValueRefData.size() );

        /* finished building reference data map */

        return mapKeyValueRefData;
    }

    /**
     * @param pDbUser
     *                 The name of an oracle user
     * @param pPackage
     *                 The name of a pl/sql package owned by pDbUser
     * @param pFunction
     *                 The name of a function within pPackage which returns a ref. cursor containing ref. data
     * @return A map containing any number of lists of reference data
     */
    protected final Map <String , SelectChoiceList <IntegerSelectChoice>> getKeyValueRefData( final String pDbUser , final String pPackage , final String pFunction ) throws SQLException
    {
        final String dbStatement =   " begin  "
                                   + "   sys.dbms_application_info.set_module( module_name => ? , action_name => ? ); "
                                   + "   ? := " + pDbUser + "." + pPackage + "." + pFunction + ";"
                                   + " end;   ";

        OracleCallableStatement dbCstmt  = null;
        OracleResultSet         dbRs     = null;

        try
        {
            /* retrieve data */

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1 , pPackage.toUpperCase() );
            dbCstmt.setString( 2 , pFunction.toUpperCase() );
            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            /* build reference data map [a map of lists] */

            return getKeyValueRefData( dbRs );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst retrieving reference data -> {}; db user -> {}; package -> {}; function -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    pDbUser
                ,   pPackage
                ,   pFunction
                ,   sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            throw sqle;
        }
        finally
        {
            CloseResource.close( dbRs );
            CloseResource.close( dbCstmt );
        }
    }

    /**
     * Make a role granted to the authenticated user active
     *
     * @param pRole
     *              the role to activate
     */
    protected final void setRole( final String pRole ) throws SQLException
    {
        final String dbStatement =   " begin  "
                                   + "   sys.dbms_application_info.set_module( module_name => ? , action_name => ? ); "
                                   + "   app_user.pk_set_app_user_roles.pr_enable_role( p_role => ? );"
                                   + " end;   ";

        CallableStatement dbCstmt = null;

        try
        {
            LOGGER.debug( "Set db role" );

            dbCstmt = getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_SET_APP_USER_ROLES" );
            dbCstmt.setString( 2 , "PR_ENABLE_ROLE" );
            dbCstmt.setString( 3 , pRole );

            LOGGER.debug( "DB params registered : Role -> {}" , pRole );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst setting role -> {}; role -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   pRole
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            throw sqle;
        }
        finally
        {
            CloseResource.close( dbCstmt );
        }
    }

    /**
     * Enable several Oracle roles at once
     */
    protected final void setRoles( final String [ ] pRoles ) throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module( module_name => ? , action_name => ? ); "
                                 + "   app_user.pk_set_app_user_roles.pr_enable_roles( p_role_list => ? );"
                                 + " end;  ";

        OracleCallableStatement dbCstmt = null;

        try
        {
            LOGGER.debug( "Set db role" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            /* transform the pRoles string array into a table of Oracle objects */

            STRUCT [ ] dbRoles = new STRUCT [ pRoles.length ];

            LOGGER.debug( "Role STRUCT[] built : size -> {}" , pRoles.length );

            StructDescriptor tyString = StructDescriptor.createDescriptor( "APP_UTILITY.TY_STRING" , getConnection() );

            LOGGER.debug( "ty_string structure retrieved" );

            ArrayDescriptor ttyString = ArrayDescriptor.createDescriptor( "APP_UTILITY.TTY_STRING" , getConnection() );

            LOGGER.debug( "tty_string structure retrieved" );

            for ( int i = 0 ; i < dbRoles.length ; i++ )
            {
                String  [   ] role = { pRoles [ i ] };
                dbRoles [ i ]      = new STRUCT( tyString , getConnection() , role );
            }

            /* if we've got this far our ARRAY is almost prepared */

            dbCstmt.setString( 1 , "PK_SET_APP_USER_ROLES" );
            dbCstmt.setString( 2 , "PR_ENABLE_ROLES" );
            dbCstmt.setARRAY( 3 , new ARRAY( ttyString , getConnection() , dbRoles ) );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst setting role -> {}; role -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   pRoles
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            throw sqle;
        }
        finally
        {
            CloseResource.close( dbCstmt );
        }
    }
}
