package com.github.wicketoracle.app.user.delegate.mgr;

import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

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
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class OracleDelegateUserRoleMgrDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( OracleDelegateUserRoleMgrDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public OracleDelegateUserRoleMgrDAO( final String pUsername, final String pPassword ) throws SQLException
    {
        super( pUsername, pPassword );
    }

    /**
     *
     * @param pUserId
     *               User id of the application user whose roles we are trying to discover
     * @return
     */
    public List<DelegateUserRole> getUserRoles( final int pUserId ) throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   ? := app_user.pk_delegate_user_role_mgr.fn_get_delegate_user_roles( p_aur_id => ? );"
                                 + " end;  ";

        OracleCallableStatement dbCstmt = null;
        OracleResultSet         dbRs    = null;

        List<DelegateUserRole> userRoleList = new ArrayList<DelegateUserRole>();

        try
        {
            LOGGER.debug( "Get user roles : pUserId -> {}", pUserId );

            setRole( RequiredRoles.ROLE_DGT_APP_USER_ROLE_MGR );

            LOGGER.debug( "Role set" );

            /* retrieve data */

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1, "PK_DELEGATE_USER_ROLE_MGR" );
            dbCstmt.setString( 2, "FN_GET_DELEGATE_USER_ROLES" );
            dbCstmt.registerOutParameter( 3, OracleTypes.CURSOR );
            dbCstmt.setInt( 4, pUserId );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            /* wrap the result up in our list */

            while ( dbRs.next() )
            {
                boolean tempUserHasRole = false;
                if ( dbRs.getString( "DBRL_IS_ASSIGNED_TO_USER" ).equals( "Y" ) )
                {
                    tempUserHasRole = true;
                }

                userRoleList.add
                (
                    new DelegateUserRole
                    (
                        dbRs.getInt( "ID" )
                    ,   dbRs.getString( "DESCR" )
                    ,   tempUserHasRole
                    ,   dbRs.getString( "NAME" ), false
                    )
                );
            }

            return userRoleList;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst retrieving delegate user roles -> {}; error code -> {}; sql state -> {}; pAurId -> {}"
            ,   new Object []
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pUserId
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
     * Apply changes to roles specified via the application
     */
    public void setUserRoles( final int pUserId , final List<DelegateUserRole> pRoles ) throws SQLException , NothingToDoException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   app_user.pk_delegate_user_role_mgr.pr_update_delegate_user_roles( p_aur_id => ? , p_dataset => ? );"
                                 + " end;  ";

        OracleCallableStatement dbCstmt = null;

        try
        {
            LOGGER.info( "Set user roles : pAurId-> {}", pUserId );

            setRole( RequiredRoles.ROLE_DGT_APP_USER_ROLE_MGR );

            LOGGER.info( "Role set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            /* check that some modifications have taken place */
            List<Object[]> moddedRoles = new ArrayList<Object[]>();

            for ( DelegateUserRole role : pRoles )
            {
                if ( role.isModified() )
                {
                    String tempGetAssigned = "N";
                    if ( role.getAssigned() )
                    {
                        tempGetAssigned = "Y";
                    }

                    moddedRoles.add
                    (
                        new Object[]
                        {
                            role.getDbRole()
                        ,   tempGetAssigned
                        }
                    );
                }
            }

            /* if moddedUsers is empty then nothing needs to be done */
            if ( moddedRoles.size() == 0 )
            {
                throw new NothingToDoException();
            }

            LOGGER.debug( "{} user mod(s) specified", moddedRoles.size() );

            STRUCT[] moddedDbRoles = new STRUCT[moddedRoles.size()];

            LOGGER.debug( "Role STRUCT[] built : size -> {}", moddedRoles.size() );

            StructDescriptor tyDbRole = StructDescriptor.createDescriptor( "APP_USER.TY_DB_ROLE", getConnection() );

            LOGGER.debug( "ty_db_role structure retrieved" );

            ArrayDescriptor ttyDbRole = ArrayDescriptor.createDescriptor( "APP_USER.TTY_DB_ROLE", getConnection() );

            LOGGER.debug( "tty_db_role structure retrieved" );

            for ( int i = 0 ; i < moddedDbRoles.length ; i++ )
            {
                moddedDbRoles[i] = new STRUCT( tyDbRole , getConnection(), moddedRoles.get( i ) );
            }

            /* if we have reached this stage then changes have been specified, and now an ARRAY can be built, and our sp. executed */

            dbCstmt.setString( 1, "PK_DELEGATE_USER_ROLE_MGR" );
            dbCstmt.setString( 2, "PR_UPDATE_DELEGATE_USER_ROLES" );
            dbCstmt.setInt( 3, pUserId );
            dbCstmt.setARRAY( 4, new ARRAY( ttyDbRole , getConnection() , moddedDbRoles ) );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst setting user roles -> {}; error code -> {}; sql state -> {}; pAurId -> {}"
            ,   new Object []
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pUserId
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
