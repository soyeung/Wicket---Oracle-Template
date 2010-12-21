package com.github.wicketoracle.app.data.list;


import java.io.Serializable;

import com.github.wicketoracle.html.form.choice.StringSelectChoice;

import oracle.sql.TIMESTAMP;

public class ListDatum implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int                id;
    private String             code;
    private String             itemName;
    private StringSelectChoice itemIsVisible = new StringSelectChoice( "Y" );
    private int                itemOrder;
    private TIMESTAMP          updatedDate;
    private boolean            isModified;

    /**
     * No-args constructor
     */
    public ListDatum()
    {
        super();
    }

    /**
     * Constructor used when data is retrieved from db
     */
    public ListDatum
   (
        final int                pId
    ,   final String             pItemName
    ,   final StringSelectChoice pItemIsVisible
    ,   final int                pItemOrder
    ,   final TIMESTAMP          pUpdatedDate
    ,   final boolean            pIsModified
    )
    {
        super();
        setId( pId );
        setItemName( pItemName );
        setItemIsVisible( pItemIsVisible );
        setItemOrder( pItemOrder );
        setUpdatedDate( pUpdatedDate );
        setModified( pIsModified );
    }

    /**
     * Constructor used when data is retrieved from db
     */
    public ListDatum
   (
        final int                pId
    ,   final String             pCode
    ,   final String             pItemName
    ,   final StringSelectChoice pItemIsVisible
    ,   final int                pItemOrder
    ,   final TIMESTAMP          pUpdatedDate
    ,   final boolean            pIsModified
    )
    {
        super();
        setId( pId );
        setCode( pCode );
        setItemName( pItemName );
        setItemIsVisible( pItemIsVisible );
        setItemOrder( pItemOrder );
        setUpdatedDate( pUpdatedDate );
        setModified( pIsModified );
    }

    public final int getId()
    {
        return id;
    }

    public final void setId( final int pId )
    {
        id = pId;
    }

    public final String getCode()
    {
        return code;
    }

    public final void setCode( final String pCode )
    {
        if ( pCode != null )
        {
            if ( code == null )
            {
                code = pCode;
                setModified( true );
            }
            else
            {
                if ( ! pCode.equals( itemName ) )
                {
                    code = pCode;
                    setModified( true );
                }
            }
        }
    }

    public final String getItemName()
    {
        return itemName;
    }

    public final void setItemName( final String pItemName )
    {
        if ( pItemName != null )
        {
            if ( itemName == null )
            {
                itemName = pItemName;
                setModified( true );
            }
            else
            {
                if ( ! pItemName.equals( itemName ) )
                {
                    itemName = pItemName;
                    setModified( true );
                }
            }
        }
    }

    public final StringSelectChoice getItemIsVisible()
    {
        return itemIsVisible;
    }

    public final void setItemIsVisible( final StringSelectChoice pItemIsVisible )
    {
        if ( pItemIsVisible != null )
        {
            if ( itemIsVisible == null )
            {
                itemIsVisible = pItemIsVisible;
                setModified( true );
            }
            else
            {
                if ( ! pItemIsVisible.getKey().equals( itemIsVisible.getKey() ) )
                {
                    itemIsVisible = pItemIsVisible;
                    setModified( true );
                }
            }
        }
    }

    public final int getItemOrder()
    {
        return itemOrder;
    }

    public final void setItemOrder( final int pItemOrder )
    {
        if ( itemOrder != pItemOrder )
        {
            itemOrder = pItemOrder;
            setModified( true );
        }
    }

    public final TIMESTAMP getUpdatedDate()
    {
        return updatedDate;
    }

    public final void setUpdatedDate( final TIMESTAMP pUpdatedDate )
    {
        updatedDate = pUpdatedDate;
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
