package com.github.wicketoracle.app.ucp.panel;

import org.apache.wicket.IClusterable;

class UCPStateChoice implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private UCPState ucpState;

    public UCPStateChoice()
    {

    }

    public UCPState getUcpState()
    {
        return ucpState;
    }

    public void setUcpState( final UCPState pUCPState )
    {
        ucpState = pUCPState;
    }
}
