package com.github.wicketoracle.app.report.securitymatrixbyuser;

import java.sql.SQLException;
import java.util.Map;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.report.useractivity.UserActivityReportPage;
import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.DynamicResult;
import com.github.wicketoracle.html.panel.DynamicResultPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


public class SecurityMatrixByUserReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( UserActivityReportPage.class );

    private final Panel          menuPanel      = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final SearchForm     searchForm     = new SearchForm( "searchForm" );
    private final RoleByUserView roleByUserView = new RoleByUserView( "ViewRoleByUser" );

    private boolean isFirstRender = true;

    /**
     *
     */
    public SecurityMatrixByUserReportPage()
    {
        add( menuPanel );
        add( searchForm );
        add( roleByUserView );

        roleByUserView.setVisible( false );
    }

    /**
     *
     */
    @Override
    public final void onBeforeRender()
    {
        if ( ! isFirstRender )
        {
            roleByUserView.refreshRoleByUserData( searchForm.getUserSearchChoices() );
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

        private UserSearchChoices userSearchChoices = new UserSearchChoices();

        private final DropDownChoice<IntegerSelectChoice> userIdDropDownChoice = new DropDownChoice<IntegerSelectChoice>( "userId" );
        private SelectChoiceList<IntegerSelectChoice>     userIdData;

        private final DropDownChoice<IntegerSelectChoice> dbRoleDropDownChoice = new DropDownChoice<IntegerSelectChoice>( "dbRoleId" );
        private SelectChoiceList<IntegerSelectChoice>     dbRoleData;

        /**
         *
         * @param pId
         */
        public SearchForm( final String pId )
        {
            super( pId );

            /* retrieve the drop down choice data */
            final Session session = ( Session ) getSession();

            SecurityMatrixByUserReportDAO dataService = null;

            Map<String , SelectChoiceList<IntegerSelectChoice>> selectListData = null;

            try
            {
                dataService = new SecurityMatrixByUserReportDAO( session.getUsername() , session.getPassword() );

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
            dbRoleData = selectListData.get( "DBRL" );

            if ( userIdData != null )
            {
                userIdDropDownChoice.setChoices( userIdData ).setChoiceRenderer( userIdData ).setModel( new PropertyModel <IntegerSelectChoice>( userSearchChoices , "userId"  ) );
                userIdDropDownChoice.setNullValid( true ).setRequired( false ).setVisible( true );
            }
            else
            {
                userIdDropDownChoice.setRequired( false ).setVisible( false );
            }

            if ( dbRoleData != null )
            {
                dbRoleDropDownChoice.setChoices( dbRoleData ).setChoiceRenderer( dbRoleData ).setModel( new PropertyModel <IntegerSelectChoice>( userSearchChoices , "dbRoleId"  ) );
                dbRoleDropDownChoice.setNullValid( true ).setRequired( false ).setVisible( true );
            }
            else
            {
                dbRoleDropDownChoice.setRequired( false ).setVisible( false );
            }

            add( userIdDropDownChoice );
            add( dbRoleDropDownChoice );
        }

        /**
         *
         */
        public void onSubmit()
        {
            if ( getIsDebugInfoVisible() )
            {
                info( "Search :: User id :: " + userSearchChoices.getUserId() );
                info( "Search :: DB Role id :: " + userSearchChoices.getDbRoleId().getKeyAsString() );
            }
        }

        /**
         *
         * @return
         */
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
    private class RoleByUserView extends WebMarkupContainer
    {
        private static final long serialVersionUID = 1L;

        private DynamicResultPanel roleByUserPanel = new DynamicResultPanel( "PanelRoleByUser" );

        /**
         *
         * @param pUserSearchChoices
         */
        public void refreshRoleByUserData( final UserSearchChoices pUserSearchChoices )
        {
            /* retrieve the drop down choice data */
            final Session session = ( Session ) getSession();

            SecurityMatrixByUserReportDAO dataService = null;

            DynamicResult dynamicResult = null;

            try
            {
                dataService = new SecurityMatrixByUserReportDAO( session.getUsername() , session.getPassword() );

                dynamicResult = dataService.getReport( pUserSearchChoices.getUserId().getKey() , pUserSearchChoices.getDbRoleId().getKey() );

                roleByUserPanel.setDynamicResult( dynamicResult );
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

            if ( dynamicResult != null && dynamicResult.getResults().size() > 0 )
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
        public RoleByUserView( final String pId )
        {
            super( pId );
            add( roleByUserPanel );
        }
    }
}
