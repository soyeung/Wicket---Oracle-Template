package com.github.wicketoracle.app.report.useractivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Localizer;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_CURRENT_USER_ACTIVITY_REPORT )
public class UserActivityReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( UserActivityReportPage.class );

    private final Panel            menuPanel        = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final UserActivityView userActivityView = new UserActivityView( "ViewUserActivity" );

    /**
     * Constructor
     */
    public UserActivityReportPage()
    {
        add( menuPanel );
        add( userActivityView );
    }

    @Override
    public final void onBeforeRender()
    {
        userActivityView.refreshUserActivityData();

        super.onBeforeRender();
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private class UserActivityView extends WebMarkupContainer
    {
        private static final long serialVersionUID = 1L;

        private List<ReportRecord> userActivityData = new ArrayList<ReportRecord>();

        private transient Localizer localiser = getLocalizer();

        private final Label usernameLabel         = new Label( "LabelUsername"         , localiser.getString( "LabelUsername"         , this ) );
        private final Label sessionIdLabel        = new Label( "LabelSessionId"        , localiser.getString( "LabelSessionId"        , this ) );
        private final Label sessionSerialLabel    = new Label( "LabelSessionSerial"    , localiser.getString( "LabelSessionSerial"    , this ) );
        private final Label clientInfoLabel       = new Label( "LabelClientInfo"       , localiser.getString( "LabelClientInfo"       , this ) );
        private final Label clientIdentifierLabel = new Label( "LabelClientIdentifier" , localiser.getString( "LabelClientIdentifier" , this ) );
        private final Label logonTimeLabel        = new Label( "LabelLogonTime"        , localiser.getString( "LabelLogonTime"        , this ) );
        private final Label sessionStatusLabel    = new Label( "LabelSessionStatus"    , localiser.getString( "LabelSessionStatus"    , this ) );
        private final Label programLabel          = new Label( "LabelProgram"          , localiser.getString( "LabelProgram"          , this ) );
        private final Label moduleLabel           = new Label( "LabelModule"           , localiser.getString( "LabelModule"           , this ) );
        private final Label actionLabel           = new Label( "LabelAction"           , localiser.getString( "LabelAction"           , this ) );
        private final Label traceFileNameLabel    = new Label( "LabelTraceFileName"    , localiser.getString( "LabelTraceFileName"    , this ) );

        /**
         * Retrieve the report data
         */
        private void refreshUserActivityData()
        {
            final Session session = ( Session ) getSession();

            UserActivityReportDAO dataService = null;

            try
            {
                dataService = new UserActivityReportDAO( session.getUsername() , session.getPassword() );

                userActivityData = dataService.getReport();
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when running the current user activity report -> {}; error code -> {}; sql state -> {}"
                ,   new Object [ ]
                    {
                        sqle.getMessage()
                    ,   sqle.getErrorCode()
                    ,   sqle.getSQLState()
                    }
                );
                error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
            }
            finally
            {
                if ( ! dataService.closeConnection() )
                {
                    error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
                }
            }

            if ( userActivityData.size() > 0 )
            {
                setVisible( true );
            }
            else
            {
                info( getLocalizer().getString( "MessageNoDataFound" , this ) );
                setVisible( false );
            }
        }

        /**
         *
         * @param pId
         */
        public UserActivityView( final String pId )
        {
            super( pId );
            add( usernameLabel );
            add( sessionIdLabel );
            add( sessionSerialLabel );
            add( clientInfoLabel );
            add( clientIdentifierLabel );
            add( logonTimeLabel );
            add( sessionStatusLabel );
            add( programLabel );
            add( moduleLabel );
            add( actionLabel );
            add( traceFileNameLabel );

            add
            (
                new RefreshingView <ReportRecord>( "UserActivityList" )
                {
                    private static final long serialVersionUID = 1L;

                    /**
                     * Retrieve data
                     */
                    @Override
                    protected Iterator <IModel<ReportRecord>> getItemModels()
                    {
                        return new ModelIteratorAdapter<ReportRecord>( userActivityData.iterator() )
                        {
                            @Override
                            protected IModel <ReportRecord> model( final ReportRecord pObject )
                            {
                                return new CompoundPropertyModel<ReportRecord>( pObject );
                            }
                        };
                    }

                    /**
                     * Populate the report
                     */
                    @Override
                    protected void populateItem( final Item<ReportRecord> pItem )
                    {
                        pItem.add( new Label( "username" ) );
                        pItem.add( new Label( "sessionId" ) );
                        pItem.add( new Label( "sessionSerialNum" ) );
                        pItem.add( new Label( "clientInfo" ) );
                        pItem.add( new Label( "clientIdentifier" ) );
                        pItem.add( new DateLabel( "logonTime" , new PatternDateConverter( "dd-MMM-yyyy", false ) ) );
                        pItem.add( new Label( "sessionStatus" ) );
                        pItem.add( new Label( "program" ) );
                        pItem.add( new Label( "module" ) );
                        pItem.add( new Label( "action" ) );
                        pItem.add( new Label( "traceFileName" ) );
                    }
                }
            );
        }
    }
}
