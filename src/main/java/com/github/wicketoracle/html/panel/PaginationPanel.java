package com.github.wicketoracle.html.panel;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.Panel;

public class PaginationPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    public PaginationPanel( final String pId , final IPageable pIPageable )
    {
        super( pId );
        add( new PagingNavigation( "pageLinks" , pIPageable ) );
    }
}
