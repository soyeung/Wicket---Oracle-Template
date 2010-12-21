package com.github.wicketoracle.html.panel;

import org.apache.wicket.IClusterable;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

public abstract class Paginator implements IClusterable , IPageable
{
    private static final long serialVersionUID = 1L;

    /**
     * No pagination is necessary
     */
    public static final int ALL_RECORDS_ON_PAGE = 0;

    /**
     * Keep track of the current page number
     */
    private int currentPage = 0;

    /**
     * Record how many items are be displayed on one page
     */
    private int itemsPerPage = ALL_RECORDS_ON_PAGE;

    /**
     *
     */
    private int lowerItemBound = ALL_RECORDS_ON_PAGE;

    /**
     *
     */
    private int upperItemBound = ALL_RECORDS_ON_PAGE;

    /**
     *
     */
    private void setBounds()
    {
        if ( itemsPerPage == ALL_RECORDS_ON_PAGE )
        {
            lowerItemBound = ALL_RECORDS_ON_PAGE;
            upperItemBound = ALL_RECORDS_ON_PAGE;
        }
        else
        {
            lowerItemBound = ( ( currentPage ) * itemsPerPage ) + 1;
            upperItemBound = ( currentPage + 1 ) * itemsPerPage;
        }
    }

    public final void reset()
    {
        setCurrentPage( 0 );
    }

    /**
     *
     */
    public final int getCurrentPage()
    {
        return currentPage;
    }

    /**
     *
     */
    public final void setCurrentPage( final int pCurrentPage )
    {
        if ( pCurrentPage < 1 )
        {
            currentPage = 0;
        }
        else
        {
            currentPage = pCurrentPage;
        }
        setBounds();
    }

    public final int getItemsPerPage()
    {
        return itemsPerPage;
    }

    /**
     *
     */
    public final void setItemsPerPage( final int pItemsPerPage )
    {
        itemsPerPage = pItemsPerPage;
        setBounds();
    }

    /**
     *
     */
    public final int getPageCount()
    {
        return getCurrentPage() + 3;
    }

    /**
     *
     * @return
     */
    public final int getLowerItemBound()
    {
        return lowerItemBound;
    }

    /**
     *
     * @return
     */
    public final int getUpperItemBound()
    {
        return upperItemBound;
    }
}
