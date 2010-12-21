package com.github.wicketoracle.app.report.userprivileges;

import java.sql.SQLException;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.TreeControlPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_USER_PRIVILEGES_REPORT )
public class UserPrivilegesReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( UserPrivilegesReportPage.class );

    private final Panel          menuPanel      = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final SearchForm     searchForm     = new SearchForm( "searchForm" );
    private final ReportTreeView reportTreeView = new ReportTreeView( "ViewReportTree" );

    private boolean isFirstRender = true;

    /**
     *
     */
    public UserPrivilegesReportPage()
    {
        add( menuPanel );
        add( searchForm );
        add( reportTreeView );

        reportTreeView.setVisible( false );
    }

    /**
      *
      */
    @Override
    public final void onBeforeRender()
    {
        if ( ! isFirstRender )
        {
            reportTreeView.refreshUserPrivileges( searchForm.getUserSearchChoices() );
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
        private SelectChoiceList<IntegerSelectChoice> userIdData;

        /**
         *
         * @param pId
         */
        public SearchForm( final String pId )
        {
            super( pId );

            /* retrieve the drop down choice data */
            final Session session = ( Session ) getSession();

            UserPrivilegesReportDAO dataService = null;

            Map<String , SelectChoiceList<IntegerSelectChoice>> selectListData = null;

            try
            {
                dataService = new UserPrivilegesReportDAO( session.getUsername() , session.getPassword() );

                selectListData = dataService.getKeyValueRefData();
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving ref. data for user privileges report -> {}; error code -> {}; sql state -> {}"
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
                userIdDropDownChoice.setNullValid( false ).setRequired( true ).setVisible( true );
            }
            else
            {
                userIdDropDownChoice.setRequired( false ).setVisible( false );
            }

            add( userIdDropDownChoice );
        }

        /**
         *
         */
        @Override
        public void onSubmit()
        {
            if ( getIsDebugInfoVisible() )
            {
                info( "Search :: user id :: " + userSearchChoices.getUserId().getKeyAsString() );
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
    private class ReportTreeView extends WebMarkupContainer
    {
        private static final long serialVersionUID = 1L;

        private final LinkTree         reportTree       = new LinkTree( "TreeReport" , new DefaultTreeModel( new DefaultMutableTreeNode( getLocalizer().getString( "LabelReportTreeRootNode" , this ) ) ) );
        private final TreeControlPanel treeControlPanel = new TreeControlPanel( "PanelTreeControl" , reportTree );

        /**
         *
         * @param pId
         */
        public ReportTreeView( final String pId )
        {
            super( pId );
            reportTree.setRootLess( true );
            reportTree.getTreeState().collapseAll();
            add( reportTree );
            add( treeControlPanel );
        }

        /**
         *
         * @param pSearchCriteria
         */
        public void refreshUserPrivileges( final UserSearchChoices pSearchCriteria )
        {
            final Session session = ( Session ) getSession();

            UserPrivilegesReportDAO dataService = null;

            TreeModel reportTreeModel = null;

            try
            {
                dataService = new UserPrivilegesReportDAO( session.getUsername() , session.getPassword() );

                reportTreeModel = dataService.getSecurityTree( new DefaultMutableTreeNode( getLocalizer().getString( "LabelReportTreeRootNode" , this ) ) , pSearchCriteria.getUserId().getKey() );

                if ( reportTreeModel.getChildCount( reportTreeModel.getRoot() ) > 0 )
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
                    "SQL Exception when constructing user privilege tree -> {}; error code -> {}; sql state -> {}"
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
                    final String errmsg = getLocalizer().getString( "MessageUnexpectedError" , this );
                    error( errmsg );
                }
            }

            reportTree.setModelObject( reportTreeModel );
        }
    }
}
