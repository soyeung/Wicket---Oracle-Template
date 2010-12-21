package com.github.wicketoracle.html.panel.dojo.chart.pie;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.ResourceReference;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.dao.charting.ChartDatum;


/**
 * Wrap up the Dojo Toolkits pie chart tool behind a panel, so that it can easily
 * be reused in any situation.
 *
 * @author Andrew Hall
 *
 */
public class Dojo2DPieChartPanel extends Panel implements IHeaderContributor
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger( Dojo2DPieChartPanel.class );

    private DojoChartTheme dojoChartTheme;
    private boolean moveSlice   = false;
    private boolean useToolTip  = false;
    private int     radius;
    private int     labelOffset;

    private List<ChartDatum> data = new ArrayList<ChartDatum>();

    /**
     *
     * @param pId
     * @param pPanelWidth
     * @param pPanelHeight
     * @param pDojoChartTheme
     * @param pMoveSlice
     * @param pUseToolTip
     * @param pRadius
     * @param pLabelOffset
     * @param pData
     */
    public Dojo2DPieChartPanel( final String pId , final int pPanelWidth , final int pPanelHeight , final DojoChartTheme pDojoChartTheme , final boolean pMoveSlice , final boolean pUseToolTip , final int pRadius , final int pLabelOffset , final List<ChartDatum> pData )
    {
        super( pId );
        dojoChartTheme = pDojoChartTheme;
        moveSlice      = pMoveSlice;
        useToolTip     = pUseToolTip;
        radius         = pRadius;
        labelOffset    = pLabelOffset;
        data           = pData;

        setOutputMarkupId( true );
        add( new AttributeModifier( "style" , true , new Model<String>( "width:" + Integer.toString( pPanelWidth ) + "px;height:" + Integer.toString( pPanelHeight ) + "px;" ) ) );
    }

    /**
     * Build some dynamic javascript in order to tailor this pie chart to our needs
     */
    public final void renderHead( final IHeaderResponse response )
    {

        String json = "[]";

        try
        {
            json = new ObjectMapper().writeValueAsString( data );
            response.renderJavascriptReference( new ResourceReference( Dojo2DPieChartPanel.class , "Dojo2DPieChartPanel.js" ) );
            response.renderJavascript
            (
                "dojo.addOnLoad(function(){"
              + "var json = " + json + ";"
              + "renderDojo2DPieChart( " + this.getMarkupId( true ) + " , json , " + dojoChartTheme.getDojoClass() + " , " + Boolean.toString( moveSlice ) + " , " + useToolTip + " , " + Integer.toString( radius ) + " , " + Integer.toString( labelOffset ) + " )});", "jsDojo2DPieChart"
            );
        }
        catch ( Exception e )
        {
            LOGGER.error( "JSON Exception :: {}" , e.toString() );
            error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
        }
    }
}
