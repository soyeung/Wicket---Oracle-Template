package com.github.wicketoracle.app.data.list.subdivision;

import java.io.Serializable;

import com.github.wicketoracle.html.form.choice.StringSelectChoice;


final class SubdivisionDatum implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int                id;
    private String             itemName;
    private StringSelectChoice includeItem = new StringSelectChoice( "Y" );
    private boolean            isModified;

    public SubdivisionDatum
    (
        final int                pId
    ,   final String             pItemName
    ,   final StringSelectChoice pIncludeItem
    ,   final boolean            pIsModified
    )
    {
        super();
        setId( pId );
        setItemName( pItemName );
        setIncludeItem( pIncludeItem );
        setModified( pIsModified );
    }

    public int getId()
    {
        return id;
    }

    public void setId( final int pId )
    {
        id = pId;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName( final String pItemName )
    {
        itemName = pItemName;
    }

    public StringSelectChoice getIncludeItem()
    {
        return includeItem;
    }

    public void setIncludeItem( final StringSelectChoice pIncludeItem )
    {
        if ( pIncludeItem != null )
        {
            if ( includeItem == null )
            {
                includeItem = pIncludeItem;
                setModified( true );
            }
            else
            {
                if ( ! pIncludeItem.getKey().equals( includeItem.getKey() ) )
                {
                    includeItem = pIncludeItem;
                    setModified( true );
                }
            }
        }
    }

    public boolean isModified()
    {
        return isModified;
    }

    public void setModified( final boolean pIsModified )
    {
        isModified = pIsModified;
    }
}
