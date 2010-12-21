package com.github.wicketoracle.app.report.sessionlock;

import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.wicket.Localizer;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyRenderableColumn;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.TreeControlPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


public class SessionLockReportPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SessionLockReportPage.class );

    private final Panel           menuPanel       = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final SessionLockView sessionLockView = new SessionLockView( "ViewSessionLocks" );

    /**
     *
     */
    public SessionLockReportPage()
    {
        add( menuPanel );
        add( sessionLockView );
    }

    /**
    *
    * @author Andrew Hall
    *
    */
    private class SessionLockView extends WebMarkupContainer
    {
        private static final long serialVersionUID = 1L;

        private final TreeTable        reportTree       = new TreeTable( "TreeReport" , getTreeModel() , getColumns() );
        private final TreeControlPanel treeControlPanel = new TreeControlPanel( "PanelTreeControl" , reportTree );

        /**
         *
         * @return
         */
        private IColumn[] getColumns()
        {
            Localizer localizer = getLocalizer();

            return new IColumn[ ] {
                                      new PropertyTreeColumn( new ColumnLocation( Alignment.LEFT , 15 , Unit.EM ) , localizer.getString( "HeaderSessionId" , this ) , "userObject.sessionId" )
                                  ,   new PropertyRenderableColumn( new ColumnLocation( Alignment.LEFT , 8  , Unit.EM ) , localizer.getString( "HeaderUsername" , this )  , "userObject.username" )
                                  ,   new PropertyRenderableColumn( new ColumnLocation( Alignment.LEFT , 8  , Unit.EM ) , localizer.getString( "HeaderLockType" , this ) , "userObject.lockType" )
                                  ,   new PropertyRenderableColumn( new ColumnLocation( Alignment.LEFT , 8  , Unit.EM ) , localizer.getString( "HeaderModeHeld" , this ) , "userObject.modeHeld" )
                                  ,   new PropertyRenderableColumn( new ColumnLocation( Alignment.LEFT , 10 , Unit.EM ) , localizer.getString( "HeaderModeRequested" , this ) , "userObject.modeRequested" )
                                  ,   new PropertyRenderableColumn( new ColumnLocation( Alignment.LEFT , 8  , Unit.EM ) , localizer.getString( "HeaderLockId1" , this ) , "userObject.lockId1" )
                                  ,   new PropertyRenderableColumn( new ColumnLocation( Alignment.LEFT , 8  , Unit.EM ) , localizer.getString( "HeaderLockId2" , this ) , "userObject.lockId2" )
                                  ,   new PropertyRenderableColumn( new ColumnLocation( Alignment.LEFT , 10 , Unit.EM ) , localizer.getString( "HeaderPrevSQL" , this ) , "userObject.prevSql" )
                                  ,   new PropertyRenderableColumn( new ColumnLocation( Alignment.LEFT , 10 , Unit.EM ) , localizer.getString( "HeaderCurrentSQL" , this ) , "userObject.currSql" )
                                  };
        }

        /**
         *
         * @return
         */
        private TreeModel getTreeModel()
        {
            final Session session = ( Session ) getSession();

            SessionLockReportDAO dataService = null;
            TreeModel            treeModel   = null;

            try
            {
                dataService = new SessionLockReportDAO( session.getUsername() , session.getPassword() );

                treeModel = dataService.getReport( new DefaultMutableTreeNode( getLocalizer().getString( "LabelRootTreeNode" , this ) ) );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving session lock report link-tree data -> {}; error code -> {}; sql state -> {}"
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

            if ( treeModel == null )
            {
                treeModel = new DefaultTreeModel( new DefaultMutableTreeNode( getLocalizer().getString( "LabelRootTreeNodeError" , this ) ) );
            }

            return treeModel;
        }

        /**
         *
         * @param pId
         */
        public SessionLockView( final String pId )
        {
            super( pId );

            reportTree.setRootLess( true );
            reportTree.getTreeState().expandAll();

            add( treeControlPanel );
            add( reportTree );
        }

        /**
         *
         */
        @Override
        public void onBeforeRender()
        {
            final TreeModel treeModel = reportTree.getModelObject();

            final boolean isLocks = ( treeModel.getChildCount( treeModel.getRoot() ) >= 1 );

            reportTree.setModelObject( treeModel );

            if ( isLocks )
            {
                reportTree.setModelObject( treeModel );
                setVisible( true );
            }
            else
            {
                info( getLocalizer().getString( "MessageNoDataFound" , this ) );
                setVisible( false );
            }

            super.onBeforeRender();
        }
    }
}
