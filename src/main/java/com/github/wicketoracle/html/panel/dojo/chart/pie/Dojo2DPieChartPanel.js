function renderDojo2DPieChart( pDiv , pJson , pTheme , pMoveSlice , pUseToolTip , pRadius , pLabelOffset )
{
    //create the chart
    var pieChart = new dojox.charting.Chart2D( pDiv );

    //set the theme
    pieChart.setTheme( pTheme );

    //add plot 
    pieChart.addPlot
    (
        'default'
    ,   {
            type: 'Pie'
        ,   radius: pRadius
        ,   fontColor: 'black'
        ,   labelOffset: pLabelOffset
        }
    );

    //add the data series
    pieChart.addSeries( 'Pie Chart Series' , pJson );

    //slice animation!
    if( pMoveSlice == true )
    {
        new dojox.charting.action2d.MoveSlice( pieChart , 'default' );
    }

    //tooltips!
    if( pUseToolTip == true)
    {
        new dojox.charting.action2d.Tooltip( pieChart , 'default' );
    }

    //render the chart
    pieChart.render();
}