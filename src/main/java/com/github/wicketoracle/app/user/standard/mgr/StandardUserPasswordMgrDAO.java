package com.github.wicketoracle.app.user.standard.mgr;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class StandardUserPasswordMgrDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( StandardUserPasswordMgrDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public StandardUserPasswordMgrDAO( final String pUsername, final String pPassword ) throws SQLException
    {
        super( pUsername, pPassword );
    }

    /**
     *
     * @param pAurid
     *               id of the user whose password is to be changed
     * @param pPassword
     *                  new password
     */
    public void changePassword( final int pUserId, final String pPassword ) throws SQLException
    {
        final String dbStatement = " begin  "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   app_user.pk_standard_user_password_mgr.pr_change_password( p_aur_id => ?, p_password => ? );"
                                 + " end;   ";

        CallableStatement dbCstmt = null;

        try
        {
            setRole( RequiredRoles.ROLE_STD_USER_PASSWORD_MGR );

            LOGGER.debug( "Role set" );

            dbCstmt = getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1, "PK_STANDARD_USER_PASSWORD_MGR" );
            dbCstmt.setString( 2, "PR_CHANGE_PASSWORD" );
            dbCstmt.setInt( 3, pUserId );
            dbCstmt.setString( 4, pPassword );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst changing a user password -> {}; error code -> {}; sql state -> {};"
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
