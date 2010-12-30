package com.github.wicketoracle.app.report.logonhistory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class LogonHistoryReportDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( LogonHistoryReportDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public LogonHistoryReportDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     * @return The reference data lists required by the standard user management functionality
     * @throws SQLException
     */
    public Map <String , SelectChoiceList<IntegerSelectChoice>> getKeyValueRefData() throws SQLException
    {
        setRole( RequiredRoles.ROLE_AUTHENTICATION_REPORT );

        return getKeyValueRefData( "app_report" , "pk_authentication_report" , "fn_get_list_ref_data" );
    }

    /**
     *
     * @return The report data
     */
    public List<ReportRecord> getReport( final int pAurId , final java.util.Date pStartDateTime , final java.util.Date pEndDateTime , final int pLowerRecordLimit , final int pUpperRecordLimit ) throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   ? := app_report.pk_authentication_report.fn_get_report"
                                 + "        ( "
                                 + "          p_aur_id             => ? "
                                 + "        , p_start_lgn_time     => ? "
                                 + "        , p_end_lgn_time       => ? "
                                 + "        , p_lower_record_limit => ? "
                                 + "        , p_upper_record_limit => ? "
                                 + "        );"
                                 + " end;  ";

        OracleCallableStatement dbCstmt  = null;
        OracleResultSet         dbRs     = null;

        List < ReportRecord > reportData = new ArrayList <ReportRecord>();

        try
        {
            LOGGER.debug( "Run authentication report" );

            setRole( RequiredRoles.ROLE_AUTHENTICATION_REPORT );

            LOGGER.debug( "Role set" );

            /* retrieve data */

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_AUTHENTICATION_REPORT" );
            dbCstmt.setString( 2 , "FN_GET_REPORT" );
            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );

            if ( pAurId == 0 )
            {
                dbCstmt.setNull( 4 , OracleTypes.NULL );
            }
            else
            {
                dbCstmt.setInt( 4, pAurId );
            }

            if ( pStartDateTime == null )
            {
                dbCstmt.setNull( 5 , OracleTypes.NULL );
            }
            else
            {
                dbCstmt.setDate( 5, new java.sql.Date( pStartDateTime.getTime() ) );
            }

            if ( pEndDateTime == null )
            {
                dbCstmt.setNull( 6 , OracleTypes.NULL );
            }
            else
            {
                dbCstmt.setDate( 6, new java.sql.Date( pEndDateTime.getTime() ) );
            }

            if ( pLowerRecordLimit == 0 )
            {
                dbCstmt.setNull( 7 , OracleTypes.NULL );
            }
            else
            {
                dbCstmt.setInt( 7, pLowerRecordLimit );
            }

            if ( pUpperRecordLimit == 0 )
            {
                dbCstmt.setNull( 8 , OracleTypes.NULL );
            }
            else
            {
                dbCstmt.setInt( 8, pUpperRecordLimit );
            }

            LOGGER.debug( "DB params registered" );

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
                            dbRs.getString( "AUR_USERNAME" )
                        ,   dbRs.getTIMESTAMP( "LGN_TIME" ).timestampValue()
                        ,   dbRs.getString( "LGN_IP_ADDRESS" )
                        ,   dbRs.getString( "LGN_HTTP_SESSION" )
                        )
                );
            }

            return reportData;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst running authentication report -> {}; error code -> {}; sql state -> {}; aur id -> {}; start date time -> {}; end date time -> {}; lower record limit -> {}; upper record limit -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pAurId
                ,   pStartDateTime
                ,   pEndDateTime
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
}
