package com.github.wicketoracle.app.report.securitymodel;

import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.TreeControlPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_SECURITY_MODEL_REPORT )
public class SecurityModelReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SecurityModelReportPage.class );

    private final Panel          menuPanel      = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final ReportTreeView reportTreeView = new ReportTreeView( "ViewReportTree" );

    /**
     *
     */
    public SecurityModelReportPage()
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

            SecurityModelReportDAO dataService = null;

            TreeModel reportTreeModel = null;

            try
            {
                dataService = new SecurityModelReportDAO( session.getUsername() , session.getPassword() );

                reportTreeModel = dataService.getSecurityTree( new DefaultMutableTreeNode( getLocalizer().getString( "LabelReportTreeRootNode" , this ) ) );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when constructing security model tree -> {}; error code -> {}; sql state -> {}"
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

            return new LinkTree( "TreeReport" , reportTreeModel );
        }

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
    }
}
