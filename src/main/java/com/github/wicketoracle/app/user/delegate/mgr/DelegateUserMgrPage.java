package com.github.wicketoracle.app.user.delegate.mgr;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.form.choice.StringSelectChoice;
import com.github.wicketoracle.html.form.choice.YesNoChoice;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;

@AuthorizeInstantiation( RequiredRoles.ROLE_DELEGATE_APP_USER_MGR )
public final class DelegateUserMgrPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DelegateUserMgrPage.class );

    private final Panel      menuPanel  = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final SearchForm searchForm = new SearchForm( "searchUserForm" );
    private final MgrForm    mgrForm    = new MgrForm( "mgrForm" );

    private boolean isFirstRender = true;

    /**
     *
     */
    public DelegateUserMgrPage()
    {
        add( menuPanel );
        add( searchForm );
        add( mgrForm );

        mgrForm.setVisible( false );
    }

    /**
     *
     */
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

        private UserSearchChoices userSearchChoices = new UserSearchChoices( );

        private final TextField<String>  usernameTextField              = new TextField<String>( "username" , new PropertyModel<String>( userSearchChoices , "username" ) );
        private final YesNoChoice        isEnabledDropDownChoice        = new YesNoChoice( "isEnabled" , new PropertyModel<StringSelectChoice>( userSearchChoices , "isEnabled" ) );
        private final YesNoChoice        isTracingEnabledDropDownChoice = new YesNoChoice( "isTracingEnabled" , new PropertyModel<StringSelectChoice>( userSearchChoices , "isTracingEnabled" ) );

        private final DropDownChoice<IntegerSelectChoice> dbRoleDropDownChoice = new DropDownChoice<IntegerSelectChoice>( "dbRole" );

        private SelectChoiceList<IntegerSelectChoice> dbRoleData;

        /**
         *
         * @param pId
         */
        public SearchForm( final String pId )
        {
            super( pId );

            isEnabledDropDownChoice.setNullValid( true ).setRequired( false );
            isTracingEnabledDropDownChoice.setNullValid( true ).setRequired( false );

            add( usernameTextField );
            add( isEnabledDropDownChoice );
            add( isTracingEnabledDropDownChoice );

            /* retrieve the */
            final Session session = ( Session ) getSession();

            OracleDelegateUserMgrDAO                            dataService    = null;
            Map<String , SelectChoiceList<IntegerSelectChoice>> selectListData = null;

            try
            {
                dataService = new OracleDelegateUserMgrDAO( session.getUsername() , session.getPassword() );

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

            dbRoleData = selectListData.get( "DBRL" );

            if ( dbRoleData != null )
            {
                dbRoleDropDownChoice.setChoices( dbRoleData ).setChoiceRenderer( dbRoleData ).setModel( new PropertyModel <IntegerSelectChoice>( userSearchChoices , "dbRole"  ) );
                dbRoleDropDownChoice.setNullValid( true ).setRequired( false ).setVisible( true );
            }
            else
            {
                dbRoleDropDownChoice.setRequired( false ).setVisible( false );
            }

            add( dbRoleDropDownChoice );
        }

        public void onSubmit()
        {
            if ( getIsDebugInfoVisible() )
            {
                info( "Search :: Username :: " + userSearchChoices.getUsername() );
                info( "Search :: Is account enabled :: " + userSearchChoices.getIsEnabled().getKey() );
                info( "Search :: Is tracing enabled :: " + userSearchChoices.getIsTracingEnabled().getKey() );
                info( "Search :: DB Role :: " + userSearchChoices.getDbRole().getKeyAsString() );
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
    private final class MgrForm extends StatelessForm<DelegateUser>
    {
        private static final long serialVersionUID = 1L;

        private transient Localizer localiser = getLocalizer();

        private final Label usernameLabel          = new Label( "LabelUsername"          , localiser.getString( "LabelUsername"         , this ) );
        private final Label profileLabel           = new Label( "LabelProfile"           , localiser.getString( "LabelProfile"          , this ) );
        private final Label isEnabledLabel         = new Label( "LabelIsEnabled"         , localiser.getString( "LabelIsEnabled"        , this ) );
        private final Label isTracingEnabledLabel  = new Label( "LabelIsTracingEnabled"  , localiser.getString( "LabelIsTracingEnabled" , this ) );
        private final Label dateCreatedLabel       = new Label( "LabelDateCreated"       , localiser.getString( "LabelDateCreated"      , this ) );
        private final Label changeRolesLabel       = new Label( "LabelChangeRoles"       , localiser.getString( "LabelChangeRoles"      , this ) );

        private final boolean canChangeRoles     = ( ( Session ) getSession() ).getRoles().hasRole( RequiredRoles.ROLE_DGT_APP_USER_ROLE_MGR );

        private transient List<DelegateUser> userList = new ArrayList<DelegateUser>();

        public MgrForm( final String pId )
        {
            super( pId );

            add( usernameLabel );
            add( profileLabel );
            add( isEnabledLabel );
            add( isTracingEnabledLabel );
            add( dateCreatedLabel );
            add( changeRolesLabel.setVisible( canChangeRoles ) );

            add
            (
                new RefreshingView<DelegateUser>( "UserList" )
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected Iterator<IModel<DelegateUser>> getItemModels()
                    {
                        return new ModelIteratorAdapter<DelegateUser>( userList.iterator() )
                        {
                            @Override
                            protected IModel<DelegateUser> model( final DelegateUser pObject )
                            {
                                return new CompoundPropertyModel<DelegateUser>( pObject );
                            }
                        };
                    }

                    @Override
                    protected void populateItem( final Item<DelegateUser> pItem )
                    {
                        final boolean isUserTheCurrentUser = pItem.getModelObject().getUsername().equals( ( ( Session ) getSession() ).getUsername() );

                        pItem.add( new Label( "username" ) );
                        pItem.add( new Label( "profile" ) );
                        pItem.add( new YesNoChoice( "isEnabled" ).setEnabled( ! isUserTheCurrentUser ) );
                        pItem.add( new YesNoChoice( "isTracingEnabled" ) );
                        pItem.add( new DateLabel( "dateCreated" , new PatternDateConverter( "dd-MM-yyyy", false ) ) );
                        pItem.add
                        (
                            new WebMarkupContainer
                            (
                                "LabelLinkChangeRoles"
                            )
                            .add
                            (
                                new Link<DelegateUser>( "LinkChangeRoles" )
                                {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void onClick()
                                    {
                                        setResponsePage( new DelegateUserRoleMgrPage( pItem.getModelObject() ) );
                                    }
                                }
                            )
                            .setVisible( canChangeRoles )
                        );
                    }
                }
            );
        }

        public void onSubmit()
        {
            /* retrieve the users matching the search criteria */
            final Session session = ( Session ) getSession();

            OracleDelegateUserMgrDAO dataService = null;

            try
            {
                dataService = new OracleDelegateUserMgrDAO( session.getUsername() , session.getPassword() );
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

        public void refreshUsers( final UserSearchChoices pSearchCriteria )
        {
            /* retrieve the users matching the search criteria */
            final Session session = ( Session ) getSession();

            OracleDelegateUserMgrDAO dataService = null;

            try
            {
                dataService = new OracleDelegateUserMgrDAO( session.getUsername() , session.getPassword() );
                userList = dataService.getUsers
                           (
                               pSearchCriteria.getUsername()
                           ,   pSearchCriteria.getIsEnabled().getKey()
                           ,   pSearchCriteria.getIsTracingEnabled().getKey()
                           ,   pSearchCriteria.getDbRole().getKey()
                           );

                if ( userList.size() > 0 )
                {
                    setVisible( true );
                }
                else
                {
                    info( getLocalizer().getString( "MessageNoDataFound" , this ) );
                    setVisible( false );
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
    }
}
