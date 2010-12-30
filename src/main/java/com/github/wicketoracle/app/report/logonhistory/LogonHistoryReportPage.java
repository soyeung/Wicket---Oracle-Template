package com.github.wicketoracle.app.report.logonhistory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.ItemsPerPageChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.PaginationPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_AUTHENTICATION_REPORT )
public final class LogonHistoryReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( LogonHistoryReportPage.class );

    private final Panel            menuPanel        = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final SearchForm       searchForm       = new SearchForm( "searchForm" );
    private final LogonHistoryView logonHistoryView = new LogonHistoryView( "ViewLogonHistory" , new PaginationPanel( "paginationPanel" , searchForm.getUserSearchChoices() ) );

    private boolean isFirstRender = true;

    /**
     * Constructor
     */
    public LogonHistoryReportPage()
    {
        add( menuPanel );
        add( searchForm );
        add( logonHistoryView );

        logonHistoryView.setVisible( false );
    }

    /**
    *
    */
   @Override
   public void onBeforeRender()
   {
       if ( ! isFirstRender )
       {
           logonHistoryView.refreshLogonHistory( searchForm.getUserSearchChoices() );
       }

       isFirstRender = false;

       super.onBeforeRender();
   }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class SearchForm extends Form<UserSearchChoices>
    {
        private static final long serialVersionUID = 1L;

        private static final int RECS_PER_PAGE_START     = 5;
        private static final int RECS_PER_PAGE_END       = 30;
        private static final int RECS_PER_PAGE_INCREMENT = 5;

        private UserSearchChoices userSearchChoices = new UserSearchChoices();

        private final DropDownChoice<IntegerSelectChoice> userIdDropDownChoice = new DropDownChoice<IntegerSelectChoice>( "userId" );
        private SelectChoiceList<IntegerSelectChoice> userIdData;

        private final DateTextField startDateTextField = new DateTextField( "startDate" , new PropertyModel<Date>( userSearchChoices , "startDate" ) , "dd-MM-yyyy" );
        private final DateTextField endDateTextField   = new DateTextField( "endDate"   , new PropertyModel<Date>( userSearchChoices , "endDate"   ) , "dd-MM-yyyy" );

        private final ItemsPerPageChoice itemsPerPageDropDownChoice = new ItemsPerPageChoice
                                                                          (
                                                                              "recordsPerPage"
                                                                          ,   new PropertyModel<IntegerSelectChoice>( userSearchChoices , "recordsPerPage" )
                                                                          ,   RECS_PER_PAGE_START
                                                                          ,   RECS_PER_PAGE_END
                                                                          ,   RECS_PER_PAGE_INCREMENT
                                                                          );

        /**
         *
         * @param pId
         */
        public SearchForm( final String pId )
        {
            super( pId );
            add( startDateTextField.add( new DatePicker().setShowOnFieldClick( true ) ) );
            add( endDateTextField.add( new DatePicker().setShowOnFieldClick( true ) ) );
            add( itemsPerPageDropDownChoice.setNullValid( true ).setRequired( false ) );

            /* retrieve the drop down choice data */
            final Session session = ( Session ) getSession();

            LogonHistoryReportDAO dataService = null;

            Map<String , SelectChoiceList<IntegerSelectChoice>> selectListData = null;

            try
            {
                dataService = new LogonHistoryReportDAO( session.getUsername() , session.getPassword() );

                selectListData = dataService.getKeyValueRefData();
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving ref. data for user management search form -> {}; error code -> {}; sql state -> {}"
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

            userIdData = selectListData.get( "AUR" );

            if ( userIdData != null )
            {
                userIdDropDownChoice.setChoices( userIdData ).setChoiceRenderer( userIdData ).setModel( new PropertyModel <IntegerSelectChoice>( userSearchChoices , "userId"  ) );
                userIdDropDownChoice.setNullValid( true ).setRequired( false ).setVisible( true );
            }
            else
            {
                userIdDropDownChoice.setRequired( false ).setVisible( false );
            }

            add( userIdDropDownChoice );
        }

        public void onSubmit()
        {
            userSearchChoices.reset();

            if ( getIsDebugInfoVisible() )
            {
                info( "Search :: User id :: " + userSearchChoices.getUserId() );
                info( "Search :: start date :: " + userSearchChoices.getStartDate() );
                info( "Search :: end date :: " + userSearchChoices.getEndDate() );
                info( "Search :: Number of records per page :: " + userSearchChoices.getRecordsPerPage().getKeyAsString() );
                info( "Search :: Pagination :: Lower item boundary :: " + userSearchChoices.getLowerItemBound() );
                info( "Search :: Pagination :: Upper item boundary :: " + userSearchChoices.getUpperItemBound() );
            }
        }

        public UserSearchChoices getUserSearchChoices()
        {
            return userSearchChoices;
        }
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private class LogonHistoryView extends WebMarkupContainer
    {
        private static final long serialVersionUID = 1L;

        private transient Localizer localiser = getLocalizer();

        private PaginationPanel paginationPanel;

        private final Label usernameLabel    = new Label( "LabelUsername"    , localiser.getString( "LabelUsername"    , this ) );
        private final Label logonDateLabel   = new Label( "LabelLogonDate"   , localiser.getString( "LabelLogonDate"   , this ) );
        private final Label ipAddressLabel   = new Label( "LabelIPAddress"   , localiser.getString( "LabelIPAddress"   , this ) );
        private final Label httpSessionLabel = new Label( "LabelHTTPSession" , localiser.getString( "LabelHTTPSession" , this ) );

        private transient List<ReportRecord> logonHistoryData = new ArrayList<ReportRecord>();

        /**
         *
         * @param pSearchCriteria
         */
        public void refreshLogonHistory( final UserSearchChoices pSearchCriteria )
        {
            /* retrieve the drop down choice data */
            final Session session = ( Session ) getSession();

            LogonHistoryReportDAO dataService = null;

            try
            {
                dataService = new LogonHistoryReportDAO( session.getUsername() , session.getPassword() );

                logonHistoryData = dataService.getReport
                                   (
                                       pSearchCriteria.getUserId().getKey()
                                   ,   pSearchCriteria.getStartDate()
                                   ,   pSearchCriteria.getEndDate()
                                   ,   pSearchCriteria.getLowerItemBound()
                                   ,   pSearchCriteria.getUpperItemBound()
                                   );

                if ( logonHistoryData.size() > 0 )
                {
                    setVisible( true );
                    if ( pSearchCriteria.getUpperItemBound() > 1 )
                    {
                        paginationPanel.setVisible( true );
                    }
                    else
                    {
                        paginationPanel.setVisible( false );
                    }
                }
                else
                {
                    info( getLocalizer().getString( "MessageNoDataFound" , this ) );
                    setVisible( false );
                    paginationPanel.setVisible( false );
                }
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving ref. data for logon history search form -> {}; error code -> {}; sql state -> {}"
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
        }

        /**
         *
         * @param pId
         * @param pPaginationPanel
         */
        public LogonHistoryView( final String pId , final PaginationPanel pPaginationPanel )
        {
            super( pId );
            paginationPanel = pPaginationPanel;

            add( pPaginationPanel );
            add( usernameLabel );
            add( logonDateLabel );
            add( ipAddressLabel );
            add( httpSessionLabel );

            add
            (
                new RefreshingView<ReportRecord>( "LogonHistoryList" )
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected Iterator<IModel<ReportRecord>> getItemModels()
                    {
                        return new ModelIteratorAdapter<ReportRecord>( logonHistoryData.iterator() )
                        {
                            @Override
                            protected IModel<ReportRecord> model( final ReportRecord pObject )
                            {
                                return new CompoundPropertyModel<ReportRecord>( pObject );
                            }
                        };
                    }

                    @Override
                    protected void populateItem( final Item<ReportRecord> pItem )
                    {
                        pItem.add( new Label( "username" ) );
                        pItem.add( new DateLabel( "logonDate" , new PatternDateConverter( "dd-MM-yyyy HH:mm:ss", false ) ) );
                        pItem.add( new Label( "ipAddress" ) );
                        pItem.add( new Label( "httpSession" ) );
                    }
                }
            );
        }
    }
}
