package com.github.wicketoracle.app.user.standard.mgr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.ItemsPerPageChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.form.choice.StringSelectChoice;
import com.github.wicketoracle.html.form.choice.YesNoChoice;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.PaginationPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_STANDARD_APP_USER_MGR )
public final class StandardUserMgrPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( StandardUserMgrPage.class );

    private final Panel      menuPanel  = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final SearchForm searchForm = new SearchForm( "searchUserForm" );
    private final MgrForm    mgrForm    = new MgrForm( "mgrForm" , new PaginationPanel( "paginationPanel" , searchForm.getUserSearchChoices() ) , searchForm.getUserLocaleData() );

    private boolean isFirstRender = true;

    /**
     *
     */
    public StandardUserMgrPage()
    {
        add( menuPanel );
        add( searchForm );
        add( mgrForm );

        mgrForm.setVisible( false );
    }

    /**
     *
     */
    @Override
    public void onBeforeRender()
    {
        if ( ! isFirstRender )
        {
            mgrForm.refreshUsers( searchForm.getUserSearchChoices() );
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

        private TextField<String>  usernameTextField              = new TextField<String>( "username" , new PropertyModel<String>( userSearchChoices , "username" ) );
        private YesNoChoice        isEnabledDropDownChoice        = new YesNoChoice( "isEnabled" , new PropertyModel<StringSelectChoice>( userSearchChoices , "isEnabled" ) );
        private YesNoChoice        isTracingEnabledDropDownChoice = new YesNoChoice( "isTracingEnabled" , new PropertyModel<StringSelectChoice>( userSearchChoices , "isTracingEnabled" ) );
        private ItemsPerPageChoice itemsPerPageDropDownChoice     = new ItemsPerPageChoice
                                                                        (
                                                                            "recordsPerPage"
                                                                        ,   new PropertyModel<IntegerSelectChoice>( userSearchChoices , "recordsPerPage" )
                                                                        ,   RECS_PER_PAGE_START
                                                                        ,   RECS_PER_PAGE_END
                                                                        ,   RECS_PER_PAGE_INCREMENT
                                                                        );

        private final DropDownChoice<IntegerSelectChoice> dbRoleDropDownChoice     = new DropDownChoice<IntegerSelectChoice>( "dbRole" );
        private final DropDownChoice<IntegerSelectChoice> userLocaleDropDownChoice = new DropDownChoice<IntegerSelectChoice>( "userLocale" );

        private SelectChoiceList<IntegerSelectChoice> dbRoleData;
        private SelectChoiceList<IntegerSelectChoice> userLocaleData;

        /**
         *
         * @param pId
         */
        public SearchForm( final String pId )
        {
            super( pId );

            isEnabledDropDownChoice.setNullValid( true ).setRequired( false );
            isTracingEnabledDropDownChoice.setNullValid( true ).setRequired( false );
            itemsPerPageDropDownChoice.setNullValid( true ).setRequired( false );

            add( usernameTextField );
            add( isEnabledDropDownChoice );
            add( isTracingEnabledDropDownChoice );
            add( itemsPerPageDropDownChoice );

            /* retrieve the drop down choice data */
            final Session session = ( Session ) getSession();

            StandardUserMgrDAO                                  dataService    = null;
            Map<String , SelectChoiceList<IntegerSelectChoice>> selectListData = null;

            try
            {
                dataService = new StandardUserMgrDAO( session.getUsername() , session.getPassword() );

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

            dbRoleData     = selectListData.get( "DBRL" );
            userLocaleData = selectListData.get( "LNG" );

            if ( dbRoleData != null )
            {
                dbRoleDropDownChoice.setChoices( dbRoleData ).setChoiceRenderer( dbRoleData ).setModel( new PropertyModel <IntegerSelectChoice>( userSearchChoices , "dbRole"  ) );
                dbRoleDropDownChoice.setNullValid( true ).setRequired( false ).setVisible( true );
            }
            else
            {
                dbRoleDropDownChoice.setRequired( false ).setVisible( false );
            }

            if ( userLocaleData != null )
            {
                userLocaleDropDownChoice.setChoices( userLocaleData ).setChoiceRenderer( userLocaleData ).setModel( new PropertyModel <IntegerSelectChoice>( userSearchChoices , "userLocale"  ) );
                userLocaleDropDownChoice.setNullValid( true ).setRequired( false ).setVisible( true );
            }
            else
            {
                userLocaleDropDownChoice.setRequired( false ).setVisible( false );
            }

            add( dbRoleDropDownChoice );
            add( userLocaleDropDownChoice );
        }

        /**
         *
         */
        @Override
        public void onSubmit()
        {
            if ( getIsDebugInfoVisible() )
            {
                info( "Search :: Username :: " + userSearchChoices.getUsername() );
                info( "Search :: Is account enabled :: " + userSearchChoices.getIsEnabled().getKey() );
                info( "Search :: Is tracing enabled :: " + userSearchChoices.getIsTracingEnabled().getKey() );
                info( "Search :: DB Role :: " + userSearchChoices.getDbRole().getKeyAsString() );
                info( "Search :: User Locale :: " + userSearchChoices.getUserLocale().getKeyAsString() );
                info( "Search :: Number of records per page :: " + userSearchChoices.getRecordsPerPage().getKeyAsString() );
                info( "Search :: Pagination :: Lower item boundary :: " + userSearchChoices.getLowerItemBound() );
                info( "Search :: Pagination :: Upper item boundary :: " + userSearchChoices.getUpperItemBound() );
            }

            userSearchChoices.reset();
        }

        public UserSearchChoices getUserSearchChoices()
        {
            return userSearchChoices;
        }

        public SelectChoiceList<IntegerSelectChoice> getUserLocaleData()
        {
            SelectChoiceList<IntegerSelectChoice> temp;
            if ( userLocaleData == null )
            {
                temp = new SelectChoiceList<IntegerSelectChoice>();
            }
            else
            {
                temp = userLocaleData;
            }
            return temp;
        }
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class MgrForm extends StatelessForm<StandardUser>
    {
        private static final long serialVersionUID = 1L;

        private transient Localizer localiser = getLocalizer();

        private PaginationPanel paginationPanel;

        private final Label usernameLabel          = new Label( "LabelUsername"          , localiser.getString( "LabelUsername"         , this ) );
        private final Label profileLabel           = new Label( "LabelProfile"           , localiser.getString( "LabelProfile"          , this ) );
        private final Label isEnabledLabel         = new Label( "LabelIsEnabled"         , localiser.getString( "LabelIsEnabled"        , this ) );
        private final Label isTracingEnabledLabel  = new Label( "LabelIsTracingEnabled"  , localiser.getString( "LabelIsTracingEnabled" , this ) );
        private final Label languageLabel          = new Label( "LabelLanguage"          , localiser.getString( "LabelLanguage"         , this ) );
        private final Label dateCreatedLabel       = new Label( "LabelDateCreated"       , localiser.getString( "LabelDateCreated"      , this ) );
        private final Label changeRolesLabel       = new Label( "LabelChangeRoles"       , localiser.getString( "LabelChangeRoles"      , this ) );
        private final Label changePasswordLabel    = new Label( "LabelChangePassword"    , localiser.getString( "LabelChangePassword"   , this ) );

        private final boolean canChangeRoles     = ( ( Session ) getSession() ).getRoles().hasRole( RequiredRoles.ROLE_STD_APP_USER_ROLE_MGR );
        private final boolean canChangePasswords = ( ( Session ) getSession() ).getRoles().hasRole( RequiredRoles.ROLE_STD_USER_PASSWORD_MGR );

        private transient List<StandardUser> userList = new ArrayList<StandardUser>();

        /**
         *
         * @param pSearchCriteria
         */
        private void refreshUsers( final UserSearchChoices pSearchCriteria )
        {
            /* retrieve the users matching the search criteria */
            final Session session = ( Session ) getSession();

            StandardUserMgrDAO dataService = null;

            try
            {
                dataService = new StandardUserMgrDAO( session.getUsername() , session.getPassword() );
                userList = dataService.getUsers
                           (
                               pSearchCriteria.getUsername()
                           ,   pSearchCriteria.getIsEnabled().getKey()
                           ,   pSearchCriteria.getIsTracingEnabled().getKey()
                           ,   pSearchCriteria.getDbRole().getKey()
                           ,   pSearchCriteria.getUserLocale().getKey()
                           ,   pSearchCriteria.getLowerItemBound()
                           ,   pSearchCriteria.getUpperItemBound()
                           );

                if ( userList.size() > 0 )
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
                    "SQL Exception when retrieving standard users -> {}; error code -> {}; sql state -> {}"
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
         * @param pUserLocaleData
         */
        public MgrForm( final String pId , final PaginationPanel pPaginationPanel , final SelectChoiceList<IntegerSelectChoice> pUserLocaleData )
        {
            super( pId );
            paginationPanel = pPaginationPanel;

            add( pPaginationPanel );

            add( usernameLabel );
            add( profileLabel );
            add( isEnabledLabel );
            add( isTracingEnabledLabel );
            add( languageLabel );
            add( dateCreatedLabel );
            add( changeRolesLabel.setVisible( canChangeRoles ) );
            add( changePasswordLabel.setVisible( canChangePasswords ) );

            add
            (
                new RefreshingView<StandardUser>( "UserList" )
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected Iterator<IModel<StandardUser>> getItemModels()
                    {
                        return new ModelIteratorAdapter<StandardUser>( userList.iterator() )
                        {
                            @Override
                            protected IModel<StandardUser> model( final StandardUser pObject )
                            {
                                return new CompoundPropertyModel<StandardUser>( pObject );
                            }
                        };
                    }

                    @Override
                    protected void populateItem( final Item<StandardUser> pItem )
                    {
                        final boolean isUserTheCurrentUser = pItem.getModelObject().getUsername().equals( ( ( Session ) getSession() ).getUsername() );

                        pItem.add( new Label( "username" ) );
                        pItem.add( new Label( "profile" ) );
                        pItem.add( new YesNoChoice( "isEnabled" ).setEnabled( ! isUserTheCurrentUser ) );
                        pItem.add( new YesNoChoice( "isTracingEnabled" ) );
                        pItem.add( new DropDownChoice<IntegerSelectChoice>( "language", pUserLocaleData , pUserLocaleData ) );
                        pItem.add( new DateLabel( "dateCreated" , new PatternDateConverter( "dd-MM-yyyy", false ) ) );
                        pItem.add
                        (
                            new Link<StandardUser>( "LinkChangeRoles" )
                            {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClick()
                                {
                                    setResponsePage( new StandardUserRoleMgrPage( pItem.getModelObject() ) );
                                }
                            }
                            .setVisible( canChangeRoles )
                        );
                        pItem.add
                        (
                            new Link<Void>( "LinkChangePassword" )
                            {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClick()
                                {
                                    setResponsePage( new StandardUserPasswordMgrPage( pItem.getModelObject() ) );
                                }
                            }
                            .setEnabled( ! isUserTheCurrentUser )
                            .setVisible( canChangePasswords )
                        );

                    }
                }
            );
        }

        /**
         *
         */
        @Override
        public void onSubmit()
        {
            /* retrieve the users matching the search criteria */
            final Session session = ( Session ) getSession();

            StandardUserMgrDAO dataService = null;

            try
            {
                dataService = new StandardUserMgrDAO( session.getUsername() , session.getPassword() );
                dataService.updateUsers( userList );
                dataService.doCommit();

                info( getLocalizer().getString( "MessageSuccess" , this ) );
            }
            catch ( NothingToDoException ntde )
            {
                error( getLocalizer().getString( "NoWorkToDo" , this ) );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving standard users -> {}; error code -> {}; sql state -> {}"
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
    }
}
