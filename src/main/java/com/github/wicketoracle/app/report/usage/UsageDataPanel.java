package com.github.wicketoracle.app.report.usage;

import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.panel.dojo.chart.pie.Dojo2DPieChartPanel;
import com.github.wicketoracle.html.panel.dojo.chart.pie.DojoChartTheme;
import com.github.wicketoracle.oracle.dao.charting.ChartDatum;
import com.github.wicketoracle.session.Session;


/**
 * Reusable dashboard demonstrating some of the dojo charting tools
 *
 * @author Andrew Hall
 *
 */

@AuthorizeInstantiation( RequiredRoles.ROLE_USAGE_DATA_REPORT )
public final class UsageDataPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger( UsageDataPanel.class );

    private Dojo2DPieChartPanel top10LoginPieChartPanel    = null;
    private Dojo2DPieChartPanel loginsPerHourPieChartPanel = null;

    private final int defaultPanelWidth  = 250;
    private final int defaultPanelHeight = 250;
    private final int defaultRadius      = 80;
    private final int defaultLabelOffset = -30;

    /**
     *
     * @param pId
     */
    public UsageDataPanel( final String pId )
    {
        super( pId );

        /* retrieve the drop down choice data */
        final Session session = ( Session ) getSession();

        UsageReportDAO                 dataService = null;
        Map<String , List<ChartDatum>> chartData   = null;

        try
        {
            dataService = new UsageReportDAO( session.getUsername() , session.getPassword() );

            chartData = dataService.getChartData();
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception when retrieving chart data for the usage data report panel -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );
            error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
        }
        finally
        {
            if ( ! dataService.closeConnection() )
            {
                error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
            }
        }

        top10LoginPieChartPanel = new Dojo2DPieChartPanel( "PanelTop10LoginPieChart"  , defaultPanelWidth , defaultPanelHeight , DojoChartTheme.PlotKitRed , true  , true  , defaultRadius , defaultLabelOffset , chartData.get( "TOP_10_USER_LOGINS" ) );
        add( top10LoginPieChartPanel );

        loginsPerHourPieChartPanel = new Dojo2DPieChartPanel( "PanelLoginsPerHourPieChart"  , defaultPanelWidth , defaultPanelHeight , DojoChartTheme.PlotKitRed , true  , true  , defaultRadius , defaultLabelOffset , chartData.get( "LOGINS_PER_HOUR" ) );
        add( loginsPerHourPieChartPanel );
    }
}
