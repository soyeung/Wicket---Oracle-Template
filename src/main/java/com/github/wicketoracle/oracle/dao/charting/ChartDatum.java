package com.github.wicketoracle.oracle.dao.charting;

import org.apache.wicket.IClusterable;

public class ChartDatum implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private int    y    = 0;
    private String text = "Pie Slice Text";

    public ChartDatum( final int pY , final String pText )
    {
        setY( pY );
        setText( pText );
    }

    public final int getY()
    {
        return y;
    }

    public final void setY( final int pY )
    {
        y = pY;
    }

    public final String getText()
    {
        return text;
    }

    public final void setText( final String pText )
    {
        text = pText;
    }
}
