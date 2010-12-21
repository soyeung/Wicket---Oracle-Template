package com.github.wicketoracle.app.user.standard.mgr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.form.choice.StringSelectChoice;
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class StandardUserMgrDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( StandardUserMgrDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public StandardUserMgrDAO( final String pUsername, final String pPassword ) throws SQLException
    {
        super( pUsername, pPassword );
    }

    /**
     * @return The reference data lists required by the standard user management functionality
     * @throws SQLException
     */
    public Map<String, SelectChoiceList<IntegerSelectChoice>> getKeyValueRefData() throws SQLException
    {
        setRole( RequiredRoles.ROLE_STANDARD_APP_USER_MGR );

        return getKeyValueRefData( "app_user", "pk_standard_app_user_mgr", "fn_get_list_ref_data" );
    }

    /**
     *
     * @param pUsername
     * @param pIsAccountEnabled
     * @param pIsTracingEnabled
     * @param pDbrlId
     * @param pLowerRecordLimit
     * @param pUpperRecordLimit
     * @return
     *         A list of standard users which can be managed through the application
     */
    public List<StandardUser> getUsers( final String pUsername, final String pIsAccountEnabled, final String pIsTracingEnabled, final int pDbrlId, final int pLngId, final int pLowerRecordLimit, final int pUpperRecordLimit ) throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   ? := app_user.pk_standard_app_user_mgr.fn_get_standard_users"
                                 + "        ( "
                                 + "          p_aur_username       => ? "
                                 + "        , p_is_account_enabled => ? "
                                 + "        , p_is_tracing_enabled => ? "
                                 + "        , p_dbrl_id            => ? "
                                 + "        , p_lng_id             => ? "
                                 + "        , p_lower_record_limit => ? "
                                 + "        , p_upper_record_limit => ? "
                                 + "        );"
                                 + " end;  ";

        OracleCallableStatement dbCstmt  = null;
        OracleResultSet         dbRs     = null;

        List<StandardUser> userMgrBeanList = new ArrayList<StandardUser>();

        try
        {
            LOGGER.debug( "Search standard users" );

            setRole( RequiredRoles.ROLE_STANDARD_APP_USER_MGR );

            LOGGER.debug( "Role set" );

            /* retrieve data */

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1, "PK_STANDARD_APP_USER_MGR" );
            dbCstmt.setString( 2, "FN_GET_STANDARD_USERS" );
            dbCstmt.registerOutParameter( 3, OracleTypes.CURSOR );

            if ( pUsername == null )
            {
                dbCstmt.setNull( 4, OracleTypes.VARCHAR );
            }
            else
            {
                dbCstmt.setString( 4 , pUsername );
            }

            if ( pIsAccountEnabled == null )
            {
                dbCstmt.setNull( 5 , OracleTypes.VARCHAR );
            }
            else
            {
                dbCstmt.setString( 5 , pIsAccountEnabled );
            }

            if ( pIsAccountEnabled == null )
            {
                dbCstmt.setNull( 6 , OracleTypes.VARCHAR );
            }
            else
            {
                dbCstmt.setString( 6 , pIsTracingEnabled );
            }

            if ( pDbrlId == 0 )
            {
                dbCstmt.setNull( 7 , OracleTypes.INTEGER );
            }
            else
            {
                dbCstmt.setInt( 7 , pDbrlId );
            }

            if ( pLngId == 0 )
            {
                dbCstmt.setNull( 8 , OracleTypes.INTEGER );
            }
            else
            {
                dbCstmt.setInt( 8 , pLngId );
            }

            if ( pLowerRecordLimit == 0   )
            {
                dbCstmt.setNull( 9 , OracleTypes.INTEGER );
            }
            else
            {
                dbCstmt.setInt( 9 , pLowerRecordLimit );
            }

            if ( pUpperRecordLimit == 0 )
            {
                dbCstmt.setNull( 10 , OracleTypes.INTEGER );
            }
            else
            {
                dbCstmt.setInt( 10 , pUpperRecordLimit );
            }

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            /* build user list */
            while ( dbRs.next() )
            {
                userMgrBeanList.add
                (
                    new StandardUser
                        (
                            dbRs.getInt( "ID" )
                        ,   dbRs.getString( "AUR_USERNAME" )
                        ,   dbRs.getString( "AUR_PROFILE" )
                        ,   new StringSelectChoice( dbRs.getString( "AUR_IS_ACCOUNT_ENABLED" ) )
                        ,   new StringSelectChoice( dbRs.getString( "AUR_IS_TRACING_ENABLED" ) )
                        ,   new IntegerSelectChoice( dbRs.getInt( "LNG_ID" ) )
                        ,   new java.util.Date( dbRs.getTIMESTAMP( "CREATED_DATE" ).timestampValue().getTime() )
                        ,   dbRs.getTIMESTAMP( "UPDATED_DATE" )
                        ,   false
                        )
                );
            }

            return userMgrBeanList;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                  "SQL Exception whilst searching standard users -> {}; error code -> {}; sql state -> {}; username -> {}; "
                + " is account enabled -> {}; is tracing enabled -> {}; pDbrlId -> {}; pLngId -> {}; pLowerRecordLimit -> {}; pUpperRecordLimit -> {}"
            ,   new Object[]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pUsername
                ,   pIsAccountEnabled
                ,   pIsTracingEnabled
                ,   pDbrlId
                ,   pLngId
                ,   pLowerRecordLimit
                ,   pUpperRecordLimit
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
     * @param pUsers
     *               a list of users to whom changes may be applied
     */
    public void updateUsers( final List<StandardUser> pUsers ) throws SQLException, NothingToDoException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   app_user.pk_standard_app_user_mgr.pr_update_standard_users( p_dataset => ? );"
                                 + " end;  ";

        OracleCallableStatement dbCstmt = null;

        try
        {
            LOGGER.debug( "Update standard users" );

            setRole( RequiredRoles.ROLE_STANDARD_APP_USER_MGR );

            LOGGER.debug( "Role set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            /* check that some modifications have taken place */
            List<Object[]> moddedUsers = new ArrayList<Object[]>();

            for ( StandardUser user : pUsers )
            {
                if ( user.isModified() )
                {
                    moddedUsers.add
                    (
                        new Object[]
                        {
                            user.getUserId()
                        ,   user.getUsername()
                        ,   user.getIsEnabled().getKey()
                        ,   user.getIsTracingEnabled().getKey()
                        ,   user.getLanguage().getKey() // language
                        ,   null // created date
                        ,   user.getAurUpdatedDate()
                        }
                    );
                }
            }

            /* if moddedUsers is empty then nothing needs to be done */
            if ( moddedUsers.size() == 0 )
            {
                throw new NothingToDoException();
            }

            LOGGER.debug( "{} user mod(s) specified", moddedUsers.size() );

            STRUCT[] moddedDbUsers = new STRUCT[moddedUsers.size()];

            LOGGER.debug( "User STRUCT[] built : size -> {}", moddedUsers.size() );

            StructDescriptor tyAppUser = StructDescriptor.createDescriptor( "APP_USER.TY_STANDARD_APP_USER", getConnection() );

            LOGGER.debug( "ty_app_user structure retrieved" );

            ArrayDescriptor ttyAppUser = ArrayDescriptor.createDescriptor( "APP_USER.TTY_STANDARD_APP_USER", getConnection() );

            LOGGER.debug( "tty_app_user structure retrieved" );

            for ( int i = 0 ; i < moddedDbUsers.length ; i++ )
            {
                moddedDbUsers[i] = new STRUCT( tyAppUser , getConnection(), moddedUsers.get( i ) );
            }

            /* if we have reached this stage then changes have been specified, and now an ARRAY can be built, and our sp. executed */

            dbCstmt.setString( 1, "PK_STANDARD_APP_USER_MGR" );
            dbCstmt.setString( 2, "PR_UPDATE_STANDARD_USERS" );
            dbCstmt.setARRAY( 3, new ARRAY( ttyAppUser , getConnection() , moddedDbUsers ) );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst updating standard users -> {}; error code -> {}; sql state -> {}"
            ,   new Object []
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
            CloseResource.close( dbCstmt );
        }
    }
}
