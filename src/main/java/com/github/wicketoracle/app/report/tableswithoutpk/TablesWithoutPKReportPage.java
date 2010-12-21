package com.github.wicketoracle.app.report.tableswithoutpk;

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


@AuthorizeInstantiation( RequiredRoles.ROLE_TABLES_WITHOUT_PK_REPORT )
public final class TablesWithoutPKReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( TablesWithoutPKReportPage.class );

    private final Panel               menuPanel           = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final TablesWithoutPKView tablesWithoutPKView = new TablesWithoutPKView( "ViewTablesWithoutPK" );

    /**
     * Constructor
     */
    public TablesWithoutPKReportPage()
    {
        add( menuPanel );
        add( tablesWithoutPKView );
    }

    @Override
    public void onBeforeRender()
    {
        tablesWithoutPKView.refreshUserActivityData();

        super.onBeforeRender();
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private class TablesWithoutPKView extends WebMarkupContainer
    {
        private static final long serialVersionUID = 1L;

        private List<ReportRecord> tablesWithoutPKData = new ArrayList<ReportRecord>();

        private transient Localizer localiser = getLocalizer();

        private final Label tableOwnerLabel = new Label( "LabelTableOwner" , localiser.getString( "LabelTableOwner" , this ) );
        private final Label tableNameLabel  = new Label( "LabelTableName"  , localiser.getString( "LabelTableName"  , this ) );

        /**
         * Retrieve the report data
         */
        private void refreshUserActivityData()
        {
            final Session session = ( Session ) getSession();

            TablesWithoutPKReportDAO dataService = null;

            try
            {
                dataService = new TablesWithoutPKReportDAO( session.getUsername() , session.getPassword() );

                tablesWithoutPKData = dataService.getReport();
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when running the unindexed foreign key report -> {}; error code -> {}; sql state -> {}"
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

            if ( tablesWithoutPKData.size() > 0 )
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
        public TablesWithoutPKView( final String pId )
        {
            super( pId );
            add( tableOwnerLabel );
            add( tableNameLabel );

            add
            (
                new RefreshingView <ReportRecord>( "TablesWithoutPKList" )
                {
                    private static final long serialVersionUID = 1L;

                    /**
                     * Retrieve data
                     */
                    @Override
                    protected Iterator <IModel<ReportRecord>> getItemModels()
                    {
                        return new ModelIteratorAdapter<ReportRecord>( tablesWithoutPKData.iterator() )
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
                        pItem.add( new Label( "tableOwner" ) );
                        pItem.add( new Label( "tableName" ) );
                    }
                }
            );
        }
    }
}
