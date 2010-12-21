package com.github.wicketoracle.session;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;
import com.github.wicketoracle.session.user.PersonalDetails;

import java.sql.CallableStatement;
import java.sql.SQLException;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.STRUCT;


/**
 * Data Access Object providing facilities required by a user when they successfully authenticate.
 *
 * @author Andrew Hall
 *
 */
final class SessionDAO extends AbstractOracleDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SessionDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public SessionDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     *
     * @return
     *         Personal details of authenticated user, including :
     *         <li>Granted roles</li>
     * @throws SQLException
     */
    public PersonalDetails getAppUserDetails() throws SQLException
    {
        final String dbStatement =   " begin "
                                   + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                   + "   ? := app_utility.pk_session_utility.fn_get_user_details; "
                                   + " end;  ";

        OracleCallableStatement dbCstmt  = null;
        OracleResultSet         dbRs     = null;
        OracleResultSet         dbRoleRs = null;

        PersonalDetails appUserDetails = new PersonalDetails();
        String [ ]     roleList = null;

        try
        {
            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_SESSION_UTILITY" );
            dbCstmt.setString( 2 , "FN_GET_USER_DETAILS" );
            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            LOGGER.debug( "Result set retrieved" );

            dbRs.next();

            LOGGER.debug( "Moved to the one expected record in this result set" );

            appUserDetails.setLanguageCode( dbRs.getString( "LNG_CODE" ) );

            LOGGER.debug( "Retrieved locale code -> {}" , appUserDetails.getLanguageCode() );

            appUserDetails.setPasswordExpiryDate( dbRs.getDate( "AUR_PASSWORD_EXPIRY_DATE" ) );

            LOGGER.debug( "Retrieved password expiry date -> {}" , appUserDetails.getPasswordExpiryDate() );

            int numRoles = dbRs.getInt( "AUR_NUMBER_OF_ROLES" );

            LOGGER.debug( "Number of roles retrieved -> {}" , numRoles );

            roleList = new String [ numRoles ];

            if ( numRoles > 0 )
            {
                dbRoleRs = ( OracleResultSet ) dbRs.getARRAY( "AUR_GRANTED_ROLES" ).getResultSet();

                LOGGER.debug( "Role list retrieved" );

                for ( int i = 0 ; i < numRoles ; i++ )
                {
                    dbRoleRs.next();

                    STRUCT dbRole  = dbRoleRs.getSTRUCT( 2 );
                    roleList [ i ] = ( dbRole.getAttributes() ) [ 0 ].toString();
                }

                LOGGER.debug( "Role list converted to string array" );

                appUserDetails.setRoles( new Roles( roleList ) );
            }
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst retrieving user details during logon -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            throw sqle;
        }
        finally
        {
            CloseResource.close( dbRoleRs );
            CloseResource.close( dbRs );
            CloseResource.close( dbCstmt );
        }

        return appUserDetails;
    }

    /**
     * Records successful logons to the application in the database
     *
     * @param pIPAddress
     * @param pHTTPSession
     */
    public void recordLogon( final String pIPAddress , final String pHTTPSession ) throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   SYS.dbms_application_info.set_module( module_name => ? , action_name => ? ); "
                                 + "   app_log.pk_log.pr_log_app_authentication( p_ip_address => ? , p_http_session => ? );"
                                 + " end;  ";

        CallableStatement dbCstmt = null;

        try
        {
            LOGGER.debug( "Record successful logon" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_LOG" );
            dbCstmt.setString( 2 , "PR_LOG_APP_AUTHENTICATION" );
            dbCstmt.setString( 3 , pIPAddress );
            dbCstmt.setString( 4 , pHTTPSession );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
           (
                "SQL Exception whilst recording successful logon -> {}; error code -> {}; sql state -> {}; IP address -> {}; HTTP Session -> {}"
            ,   new Object []
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pIPAddress
                ,   pHTTPSession
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
