package com.github.wicketoracle.app.report.tableswithoutpk;

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


final class TablesWithoutPKReportDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( TablesWithoutPKReportDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public TablesWithoutPKReportDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     *
     * @return A list of unindexed foreign keys
     */
    public List<ReportRecord> getReport() throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   ? := app_report.pk_tables_without_pk_report.fn_get_report; "
                                 + " end;  ";

        OracleCallableStatement dbCstmt  = null;
        OracleResultSet         dbRs     = null;

        List<ReportRecord> reportData = new ArrayList < ReportRecord >();

        try
        {
            LOGGER.debug( "Run unindexed foreign key report" );

            setRole( RequiredRoles.ROLE_TABLES_WITHOUT_PK_REPORT );

            LOGGER.debug( "Role set" );

            /* retrieve data */

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_TABLES_WITHOUT_PK_REPORT" );
            dbCstmt.setString( 2 , "FN_GET_REPORT" );
            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            /* build report data */
            while ( dbRs.next() )
            {
                reportData.add
               (
                    new ReportRecord
                   (
                        dbRs.getString( "TABLE_OWNER" )
                    ,   dbRs.getString( "TABLE_NAME" )
                    )
                );
            }

            return reportData;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst running the tables with primary keys report -> {}; error code -> {}; sql state -> {}"
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
