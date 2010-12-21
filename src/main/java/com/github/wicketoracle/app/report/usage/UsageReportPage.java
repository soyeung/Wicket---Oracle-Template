package com.github.wicketoracle.app.report.usage;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;

import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;


@AuthorizeInstantiation( RequiredRoles.ROLE_USAGE_DATA_REPORT )
public class UsageReportPage extends StandardPage
{
    private final Panel          menuPanel      = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final UsageDataPanel usageDataPanel = new UsageDataPanel( "PanelUsageDashBoard" );

    /**
     *
     */
    public UsageReportPage()
    {
        add( menuPanel );
        add( usageDataPanel );
    }
}
