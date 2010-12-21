package com.github.wicketoracle.html.page;

import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

import com.github.wicketoracle.html.panel.ApplicationDebugInfoPanel;
import com.github.wicketoracle.session.Session;


/**
 * HTML page from which all pages in the application should <i>extend</i>, in order to
 * reproduce the standard look and feel of the application
 *
 * @author Andrew Hall
 *
 */
public abstract class StandardPage extends WebPage
{
    private final Label         pageTitleLabel = new Label( "LabelPageTitle" , new Model<String>( getLocalizer().getString( "PageTitle" , this ) ) );
    private final FeedbackPanel feedbackPanel  = new FeedbackPanel( "FeedbackPanel" );

    private DebugBar                  debugPanel;
    private ApplicationDebugInfoPanel applicationDebugInfoPanel;

    /**
     *
     * @return
     */
    public final boolean getIsDebugInfoVisible()
    {
        return ( ( Session ) getSession() ).isDebugInfoVisible();
    }

    /**
     * Constructor
     */
    public StandardPage()
    {
        add( pageTitleLabel );
        add( feedbackPanel );

        final boolean isDebugInfoVisible = getIsDebugInfoVisible();

        if ( isDebugInfoVisible )
        {
            applicationDebugInfoPanel = new ApplicationDebugInfoPanel( "ApplicationDebugInfoPanel" , this.getClass() );
            add( applicationDebugInfoPanel );

            debugPanel = new DebugBar( "PanelDebugBar" );
            add( debugPanel );
        }
        else
        {
            add( new EmptyPanel( "ApplicationDebugInfoPanel" ) );
            add( new EmptyPanel( "PanelDebugBar" ) );
        }

        getApplication().getDebugSettings().setDevelopmentUtilitiesEnabled( isDebugInfoVisible );
    }
}
