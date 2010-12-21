package com.github.wicketoracle.app.report.usage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.github.wicketoracle.oracle.dao.charting.AbstractOracleChartingDAO;
import com.github.wicketoracle.oracle.dao.charting.ChartDatum;


/**
 *
 * @author Andrew Hall
 *
 */
final class UsageReportDAO extends AbstractOracleChartingDAO
{
    /**
     *
     * @param pUsername
     * @param pPassword
     * @throws SQLException
     */
    public UsageReportDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Map<String , List<ChartDatum>> getChartData() throws SQLException
    {
        super.setRole( RequiredRoles.ROLE_USAGE_DATA_REPORT );

        return getChartData( "app_report" , "pk_usage_data_report" , "fn_get_data" );
    }

}
