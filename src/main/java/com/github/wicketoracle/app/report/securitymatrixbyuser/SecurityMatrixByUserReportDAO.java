package com.github.wicketoracle.app.report.securitymatrixbyuser;

import java.sql.SQLException;
import java.util.Map;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.panel.DynamicResult;
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class SecurityMatrixByUserReportDAO extends AbstractOracleDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SecurityMatrixByUserReportDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public SecurityMatrixByUserReportDAO( final String pUsername, final String pPassword ) throws SQLException
    {
        super( pUsername, pPassword );
    }

    /**
     * @return The reference data lists required by the standard user management functionality
     * @throws SQLException
     */
    public Map<String, SelectChoiceList<IntegerSelectChoice>> getKeyValueRefData() throws SQLException
    {
        setRole( RequiredRoles.ROLE_SECURITY_MATRIX_BY_USER_REPORT );

        return getKeyValueRefData( "app_report", "pk_role_matrix_by_user_report", "fn_get_list_ref_data" );
    }

    /**
     * Return a disconnected view of the eport dataset to the caller
     */
    public DynamicResult getReport( final int pUserId, final int pDbRoleId ) throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   ? := app_report.pk_role_matrix_by_user_report.fn_get_report ( p_aur_id => ? , p_dbrl_id => ? ); "
                                 + " end;  ";

        OracleCallableStatement dbCstmt  = null;
        OracleResultSet         dbRs     = null;

        try
        {
            LOGGER.debug( "Run security matrix by role report" );

            setRole( RequiredRoles.ROLE_SECURITY_MATRIX_BY_USER_REPORT );

            LOGGER.debug( "Role set" );

            /* retrieve data */

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1, "PK_ROLE_MATRIX_BY_USER_REPORT" );
            dbCstmt.setString( 2, "FN_GET_REPORT" );
            dbCstmt.registerOutParameter( 3, OracleTypes.CURSOR );

            if ( pUserId  == 0 )
            {
                dbCstmt.setNull( 4, OracleTypes.NULL );
            }
            else
            {
                dbCstmt.setInt( 4, pUserId );
            }

            if ( pDbRoleId == 0 )
            {
                dbCstmt.setNull( 5, OracleTypes.NULL );
            }
            else
            {
                dbCstmt.setInt( 5, pDbRoleId );
            }

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            return new DynamicResult( dbRs );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst running security matrix by user report -> {}; error code -> {}; sql state -> {}; aur id -> {}; dbrl id -> {};"
            ,   new Object []
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pUserId
                ,   pDbRoleId
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
}
