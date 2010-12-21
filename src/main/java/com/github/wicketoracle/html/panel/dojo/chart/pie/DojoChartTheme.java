package com.github.wicketoracle.html.panel.dojo.chart.pie;

/**
 * List the Dojo themes available to the application along with the actual
 * JavaScript class.
 *
 * @author Andrew Hall
 *
 */
public enum DojoChartTheme
{
    PlotKitRed( "dojox.charting.themes.PlotKit.red" );

    private String dojoClass;

    /**
     *
     * @param pDojoClass
     */
    private DojoChartTheme( final String pDojoClass )
    {
        dojoClass = pDojoClass;
    }

    /**
     *
     * @return
     */
    public String getDojoClass()
    {
        return dojoClass;
    }
}
