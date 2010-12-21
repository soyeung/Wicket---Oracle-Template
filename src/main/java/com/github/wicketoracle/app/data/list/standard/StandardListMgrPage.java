package com.github.wicketoracle.app.data.list.standard;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Localizer;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import com.github.wicketoracle.app.data.DataStructure;
import com.github.wicketoracle.app.data.list.AbstractListMgrPage;
import com.github.wicketoracle.app.data.list.standard.panel.InsertFormPanel;
import com.github.wicketoracle.app.data.list.standard.panel.UpdateFormPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;


public class StandardListMgrPage extends AbstractListMgrPage
{
    private final Panel menuPanel = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final Label headerLabel;

    public StandardListMgrPage( final DataStructure pDataStructure )
    {
        add( menuPanel );

        ValueMap headerLabelParams = new ValueMap();
        headerLabelParams.put( "dataStructure" , pDataStructure.getRefdataDescr() );
        headerLabel = new Label( "HeaderStandardListMgr" , new StringResourceModel( "HeaderStandardListMgr" , this , new Model<ValueMap>( headerLabelParams ) ) );
        add( headerLabel );

        Localizer localiser = getLocalizer();

        List<ITab> tabList = new ArrayList<ITab>();
        tabList.add
        (
            new AbstractTab( new Model<String>( localiser.getString( "LabelInsertFormTab" , this ) ) )
            {
                private static final long serialVersionUID = 1L;

                public Panel getPanel( final String pPanelId )
                {
                    return new InsertFormPanel( pPanelId , pDataStructure , getListMetaData( pDataStructure.getRdsId() ) );
                }
            }
        );

        tabList.add
        (
            new AbstractTab( new Model<String>( localiser.getString( "LabelUpdateFormTab" , this ) ) )
            {
                private static final long serialVersionUID = 1L;

                public Panel getPanel( final String pPanelId )
                {
                    return new UpdateFormPanel( pPanelId , pDataStructure , getListMetaData( pDataStructure.getRdsId() ) );
                }
            }
        );

        add( new TabbedPanel( "tabPanel" , tabList ) );
    }
}
