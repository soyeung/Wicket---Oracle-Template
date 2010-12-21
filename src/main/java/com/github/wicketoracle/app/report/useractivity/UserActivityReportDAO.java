package com.github.wicketoracle.app.report.useractivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class UserActivityReportDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( UserActivityReportDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public UserActivityReportDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    public List<ReportRecord> getReport() throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   ? := app_report.pk_current_activity_report.fn_get_report;"
                                 + " end;  ";

        OracleCallableStatement dbCstmt  = null;
        OracleResultSet         dbRs     = null;

        List < ReportRecord > reportData = new ArrayList < ReportRecord >();

        try
        {
            LOGGER.debug( "Run authentication report" );

            setRole( RequiredRoles.ROLE_CURRENT_USER_ACTIVITY_REPORT );

            LOGGER.debug( "Role set" );

            /* retrieve data */

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_CURRENT_ACTIVITY_REPORT" );
            dbCstmt.setString( 2 , "FN_GET_REPORT" );
            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            while ( dbRs.next() )
            {
                reportData.add
               (
                    new ReportRecord
                       (
                            dbRs.getString( "AUR_USERNAME" )
                        ,   dbRs.getInt( "V$SES_SID" )
                        ,   dbRs.getInt( "V$SES_SERIAL" )
                        ,   dbRs.getString( "V$SES_CLIENT_INFO" )
                        ,   dbRs.getString( "V$SES_CLIENT_IDENTIFIER" )
                        ,   dbRs.getString( "V$SES_STATUS" )
                        ,   dbRs.getString( "V$SES_PROGRAM" )
                        ,   dbRs.getString( "V$SES_MODULE" )
                        ,   dbRs.getString( "V$SES_ACTION" )
                        ,   dbRs.getString( "TRACE_FILE_NAME" )
                        )
                );
            }

            return reportData;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
           (
                "SQL Exception whilst running current user activity report -> {}; error code -> {}; sql state -> {}"
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
            CloseResource.close( dbRs );
            CloseResource.close( dbCstmt );
        }
    }
}
