package com.github.wicketoracle.app.data;

import java.io.Serializable;

public class DataStructure implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int     rdsId;
    private String  dbrlCode;
    private String  refdataDescr;
    private boolean editable;
    private String  rdtCode;
    private boolean isModified;

    /**
     * Constructor
     */
    public DataStructure
    (
        final int     pRdsId
    ,   final String  pDbrlCode
    ,   final String  pRefdataDescr
    ,   final boolean pEditable
    ,   final String  pRdtCode
    ,   final boolean pIsModified
    )
    {
        super();
        rdsId        = pRdsId;
        dbrlCode     = pDbrlCode;
        refdataDescr = pRefdataDescr;
        editable     = pEditable;
        rdtCode      = pRdtCode;
        isModified   = pIsModified;
    }

    public final int getRdsId()
    {
        return rdsId;
    }

    public final void setRdsId( final int pRdsId )
    {
        rdsId = pRdsId;
    }

    public final String getDbrlCode()
    {
        return dbrlCode;
    }

    public final void setDbrlCode( final String pDbrlCode )
    {
        dbrlCode = pDbrlCode;
    }

    public final String getRefdataDescr()
    {
        return refdataDescr;
    }

    public final void setRefdataDescr( final String pRefdataDescr )
    {
        refdataDescr = pRefdataDescr;
    }

    public final boolean getEditable()
    {
        return editable;
    }

    public final void setEditable( final boolean pEditable )
    {
        if ( editable != pEditable )
        {
            editable = pEditable;
            setModified( true );
        }
    }

    public final String getRdtCode()
    {
        return rdtCode;
    }

    public final void setRdtCode( final String pRdtCode )
    {
        rdtCode = pRdtCode;
    }

    public final boolean isModified()
    {
        return isModified;
    }

    public final void setModified( final boolean pIsModified )
    {
        isModified = pIsModified;
    }
}
