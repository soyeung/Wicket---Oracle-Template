package com.github.wicketoracle.app.home;


import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import com.github.wicketoracle.app.report.usage.UsageDataPanel;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;

/**
 * A simple splash screen from which users will be guided to the application's functions.
 * Access is granted to this page if and only if a user has successfully authenticated
 * with the application.
 *
 * @author Andrew Hall
 *
 */

@AuthorizeInstantiation( com.github.wicketoracle.app.home.RequiredRoles.ROLE_CREATE_SESSION )
public final class HomePage extends StandardPage
{
    private final Panel menuPanel = PostLoginMenuPanelFactory.getPostLoginMenuPanel();

    /**
     * Constructor
     */
    public HomePage()
    {
        add( menuPanel );

        final Session session = ( Session ) getSession();

        if ( session.getRoles().hasRole( com.github.wicketoracle.app.report.usage.RequiredRoles.ROLE_USAGE_DATA_REPORT ) )
        {
            add( new UsageDataPanel( "PanelUsageDashBoard" ) );
        }
        else
        {
            add( new EmptyPanel( "PanelUsageDashBoard" ) );
        }
    }
}
