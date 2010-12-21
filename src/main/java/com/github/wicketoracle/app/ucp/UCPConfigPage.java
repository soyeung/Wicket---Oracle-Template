package com.github.wicketoracle.app.ucp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.Localizer;

import com.github.wicketoracle.app.ucp.panel.UCPConfigurationPanel;
import com.github.wicketoracle.app.ucp.panel.UCPStateMgrPanel;
import com.github.wicketoracle.app.ucp.panel.UCPStatisticsPanel;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;


/**
 * Screen providing a management facility for the Oracle Universal Connection pool.
 * This screen currently provides three points of interest:
 *
 * <ul>
 *     <li>It exposes the connection usage statistics collated by the UCP</li>
 *     <li>It allows UCP managers to configure it online</li>
 *     <li>It allows the UCP state to be set online - i.e. the pool can be flushed, refreshed or recycled</li>
 * </ul>
 *
 * @author Andrew Hall
 *
 */

@AuthorizeInstantiation( RequiredRoles.ROLE_UCP_MGR )
public final class UCPConfigPage extends StandardPage
{
    private Panel menuPanel = PostLoginMenuPanelFactory.getPostLoginMenuPanel();

    /**
     * Constructor
     */
    public UCPConfigPage()
    {
        add( menuPanel );

        Localizer localiser = getLocalizer();

        List<ITab> tabList = new ArrayList<ITab>();
        tabList.add
        (
            new AbstractTab( new Model<String>( localiser.getString( "LabelUCPStatisticsTab" , this ) ) )
            {
                private static final long serialVersionUID = 1L;

                public Panel getPanel( final String pPanelId )
                {
                    return new UCPStatisticsPanel( pPanelId );
                }
            }
        );

        tabList.add
        (
            new AbstractTab( new Model<String>( localiser.getString( "LabelUCPConfigurationTab" , this ) ) )
            {
                private static final long serialVersionUID = 1L;

                public Panel getPanel( final String pPanelId )
                {
                    return new UCPConfigurationPanel( pPanelId );
                }
            }
        );

        tabList.add
        (
            new AbstractTab( new Model<String>( localiser.getString( "LabelUCPStateMgrTab" , this ) ) )
            {
                private static final long serialVersionUID = 1L;

                public Panel getPanel( final String pPanelId )
                {
                    return new UCPStateMgrPanel( pPanelId );
                }
            }
        );

        add( new TabbedPanel( "tabPanel" , tabList ) );
    }
}
