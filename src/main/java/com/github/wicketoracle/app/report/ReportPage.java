package com.github.wicketoracle.app.report;

import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.TreeControlPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_REPORT_USER )
public final class ReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ReportPage.class );

    private final Panel          menuPanel      = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final ReportTreeView reportTreeView = new ReportTreeView( "ViewReportTree" );

    /**
     * Constructor that is invoked when page is invoked without a session.
     */
    public ReportPage()
    {
        add( menuPanel );
        add( reportTreeView );
    }

    /**
    *
    * @author Andrew Hall
    *
    */
    private class ReportTreeView extends WebMarkupContainer
    {
        private static final long serialVersionUID = 1L;

        private final LinkTree         reportTree       = getReportTree();
        private final TreeControlPanel treeControlPanel = new TreeControlPanel( "PanelTreeControl" , reportTree );

        /**
        *
        * @return
        */
        private LinkTree getReportTree()
        {
            final Session session = ( Session ) getSession();

            ReportDAO dataService = null;

            TreeModel reportTreeModel = null;

            try
            {
                dataService = new ReportDAO( session.getUsername() , session.getPassword() );

                reportTreeModel = dataService.getReportTree( new DefaultMutableTreeNode( getLocalizer().getString( "LabelReportTreeRootNode" , this ) ) );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when constructing reports link-tree -> {}; error code -> {}; sql state -> {}"
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

            return new LinkTree( "TreeReport" , reportTreeModel )
                       {
                           private static final long serialVersionUID = 1L;

                           protected void onNodeLinkClicked( final Object pNode, final BaseTree pTree, final AjaxRequestTarget pTarget )
                           {
                               Object reportPage = ( ( DefaultMutableTreeNode ) pNode ).getUserObject();

                               if ( reportPage instanceof Report )
                               {
                                   if ( ( ( Session ) getSession() ).getRoles().hasRole( ( ( Report ) reportPage ).getDbrlName() ) )
                                   {
                                       try
                                       {
                                           setResponsePage( Class.forName( ( ( Report ) reportPage ).getTarget() ).asSubclass( StandardPage.class ) );
                                       }
                                       catch ( ClassNotFoundException cnfe )
                                       {
                                           LOGGER.error( "ClassNotFoundException when accessing report page -> {}", cnfe.getMessage() );
                                           error( getLocalizer().getString( "MessageInaccessibleReport" , this ) );
                                       }
                                   }
                                   else
                                   {
                                       LOGGER.error( "Permission denied when attempting to access -> {}", ( ( Report ) reportPage ).getTarget() );
                                       error( getLocalizer().getString( "MessagePermissionDenied" , this ) );
                                   }
                               }
                           }
                       };
        }

        /**
         *
         * @param pId
         */
        public ReportTreeView( final String pId )
        {
            super( pId );

            reportTree.setRootLess( false );
            reportTree.getTreeState().expandAll();
            add( reportTree );
            add( treeControlPanel );
        }

        /**
         *
         */
        @Override
        public void onBeforeRender()
        {
            final TreeModel treeModel = reportTree.getModelObject();

            final boolean isData = ( treeModel.getChildCount( treeModel.getRoot() ) >= 1 );

            if ( ! isData )
            {
                info( getLocalizer().getString( "MessageNoDataFound" , this ) );
                setVisible( false );
            }
            super.onBeforeRender();
        }
    }
}
