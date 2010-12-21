package com.github.wicketoracle.app.ucp.panel;

import oracle.ucp.jdbc.JDBCConnectionPoolStatistics;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.github.wicketoracle.oracle.ucp.UCPMgr;


public class UCPStatisticsPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private transient JDBCConnectionPoolStatistics ucpStatistics = UCPMgr.getUCPDataSource().getStatistics();

    private final Label abandonedConnectionsLabel                   = new Label( "LabelAbandonedConnectionsValue"               , Integer.toString( ucpStatistics.getAbandonedConnectionsCount() ) );
    private final Label availableConnectionsLabel                   = new Label( "LabelAvailableConnectionsValue"               , Integer.toString( ucpStatistics.getAvailableConnectionsCount() ) );
    private final Label averageBorrowedConnectionsLabel             = new Label( "LabelAverageBorrowedConnectionsValue"         , Integer.toString( ucpStatistics.getAverageBorrowedConnectionsCount() ) );
    private final Label averageConnectionWaitTimeLabel              = new Label( "LabelAverageConnectionWaitTimeValue"          , Long.toString( ucpStatistics.getAverageConnectionWaitTime() ) );
    private final Label borrowedConnectionsLabel                    = new Label( "LabelBorrowedConnectionsValue"                , Integer.toString( ucpStatistics.getBorrowedConnectionsCount() ) );
    private final Label connectionsClosedLabel                      = new Label( "LabelConnectionsClosedValue"                  , Integer.toString( ucpStatistics.getConnectionsClosedCount() ) );
    private final Label connectionsCreatedLabel                     = new Label( "LabelConnectionsCreatedValue"                 , Integer.toString( ucpStatistics.getConnectionsCreatedCount() ) );
    private final Label cumulativeConnectionsBorrowedLabel          = new Label( "LabelCumulativeConnectionsBorrowedValue"      , Long.toString( ucpStatistics.getCumulativeConnectionBorrowedCount() ) );
    private final Label cumulativeConnectionsReturnedLabel          = new Label( "LabelCumulativeConnectionsReturnedValue"      , Long.toString( ucpStatistics.getCumulativeConnectionReturnedCount() ) );
    private final Label cumulativeConnectionsUseTimeLabel           = new Label( "LabelCumulativeConnectionsUseTimeValue"       , Long.toString( ucpStatistics.getCumulativeConnectionUseTime() ) );
    private final Label cumulativeConnectionsWaitTimeLabel          = new Label( "LabelCumulativeConnectionsWaitTimeValue"      , Long.toString( ucpStatistics.getCumulativeConnectionWaitTime() ) );
    private final Label numberLabelledConnectionsLabel              = new Label( "LabelNumberLabelledConnectionsValue"          , Integer.toString( ucpStatistics.getLabeledConnectionsCount() ) );
    private final Label peakConnectionsLabel                        = new Label( "LabelPeakConnectionsValue"                    , Integer.toString( ucpStatistics.getPeakConnectionsCount() ) );

    public UCPStatisticsPanel( final String pId )
    {
        super( pId );
        add( abandonedConnectionsLabel );
        add( availableConnectionsLabel );
        add( averageBorrowedConnectionsLabel );
        add( averageConnectionWaitTimeLabel );
        add( borrowedConnectionsLabel );
        add( connectionsClosedLabel );
        add( connectionsCreatedLabel );
        add( cumulativeConnectionsBorrowedLabel );
        add( cumulativeConnectionsReturnedLabel );
        add( cumulativeConnectionsUseTimeLabel );
        add( cumulativeConnectionsWaitTimeLabel );
        add( numberLabelledConnectionsLabel );
        add( peakConnectionsLabel );
    }
}
