package com.github.wicketoracle.app.changepassword;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class ChangePasswordDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( ChangePasswordDAO.class );

    public ChangePasswordDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     *
     * @param pCurrPassword
     *                      the user's current password
     * @param pNewPassword
     *                      what the user's password will be post-change
     * @throws SQLException
     */
    public void changePassword( final String pCurrPassword , final String pNewPassword ) throws SQLException
    {
        final String dbStatement =   " begin  "
                                   + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                   + "   app_user.pk_app_user_password_mgr.pr_change_password ( p_current_password => ? , p_new_password => ? );"
                                   + " end;   ";

        CallableStatement dbCstmt = null;

        try
        {
            dbCstmt = getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_APP_USER_PASSWORD_MGR" );
            dbCstmt.setString( 2 , "PR_CHANGE_PASSWORD" );
            dbCstmt.setString( 3 , pCurrPassword );
            dbCstmt.setString( 4 , pNewPassword );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst changing password -> {}; error code -> {}; sql state -> {}"
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
            CloseResource.close( dbCstmt );
        }
    }
}
