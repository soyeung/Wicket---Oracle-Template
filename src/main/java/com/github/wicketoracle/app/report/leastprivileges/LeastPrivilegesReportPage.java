package com.github.wicketoracle.app.report.leastprivileges;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Localizer;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
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


@AuthorizeInstantiation( RequiredRoles.ROLE_LEAST_PRIVILEGES_REPORT )
public final class LeastPrivilegesReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( LeastPrivilegesReportPage.class );

    private final Panel               menuPanel           = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final LeastPrivilegesView leastPrivilegesView = new LeastPrivilegesView( "ViewLeastPrivileges" );

    /**
     * Constructor
     */
    public LeastPrivilegesReportPage()
    {
        add( menuPanel );
        add( leastPrivilegesView );
    }

    /**
     *
     */
    @Override
    public void onBeforeRender()
    {
        leastPrivilegesView.refreshUserActivityData();

        super.onBeforeRender();
    }

    /**
      *
      * @author Andrew Hall
      *
      */
    private class LeastPrivilegesView extends WebMarkupContainer
    {
        private static final long serialVersionUID = 1L;

        private List<ReportRecord> leastPrivilegesData = new ArrayList<ReportRecord>();

        private transient Localizer localiser = getLocalizer();

        private final Label privilegeHolderLabel  = new Label( "LabelPrivilegeHolder"  , localiser.getString( "LabelPrivilegeHolder"  , this ) );
        private final Label objectOwnerLabel      = new Label( "LabelObjectOwner"      , localiser.getString( "LabelObjectOwner"      , this ) );
        private final Label objectNameLabel       = new Label( "LabelObjectName"       , localiser.getString( "LabelObjectName"       , this ) );
        private final Label grantedPrivilegeLabel = new Label( "LabelGrantedPrivilege" , localiser.getString( "LabelGrantedPrivilege" , this ) );

        /**
         * Retrieve the report data
         */
        private void refreshUserActivityData()
        {
            final Session session = ( Session ) getSession();

            LeastPrivilegesReportDAO dataService = null;

            try
            {
                dataService = new LeastPrivilegesReportDAO( session.getUsername() , session.getPassword() );

                leastPrivilegesData = dataService.getReport();
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when running the least privileges report -> {}; error code -> {}; sql state -> {}"
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

            if ( leastPrivilegesData.size() > 0 )
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
        public LeastPrivilegesView( final String pId )
        {
            super( pId );
            add( privilegeHolderLabel );
            add( objectOwnerLabel );
            add( objectNameLabel );
            add( grantedPrivilegeLabel );

            add
            (
                new RefreshingView <ReportRecord>( "LeastPrivilegesList" )
                {
                    private static final long serialVersionUID = 1L;

                    /**
                     * Retrieve data
                     */
                    @Override
                    protected Iterator <IModel<ReportRecord>> getItemModels()
                    {
                        return new ModelIteratorAdapter<ReportRecord>( leastPrivilegesData.iterator() )
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
                        pItem.add( new Label( "privilegeHolder" ) );
                        pItem.add( new Label( "objectOwner" ) );
                        pItem.add( new Label( "objectName" ) );
                        pItem.add( new Label( "grantedPrivilege" ) );
                    }
                }
            );
        }
    }
}
