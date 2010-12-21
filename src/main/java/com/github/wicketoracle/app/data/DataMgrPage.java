package com.github.wicketoracle.app.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.IRenderable;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyRenderableColumn;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.data.list.coded.CodedListMgrPage;
import com.github.wicketoracle.app.data.list.intersection.IntersectionListMgrPage;
import com.github.wicketoracle.app.data.list.standard.StandardListMgrPage;
import com.github.wicketoracle.app.data.list.subdivision.SubdivisionListMgrPage;
import com.github.wicketoracle.app.data.list.updateonly.UpdateOnlyListMgrPage;
import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.CheckboxPanel;
import com.github.wicketoracle.html.panel.TreeControlPanel;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_REF_DATA_MGR )
public final class DataMgrPage extends StandardPage
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( DataMgrPage.class );

    private final Panel       menuPanel   = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final DataMgrForm dataMgrForm = new DataMgrForm( "FormDataTree" );

    /**
     *
     */
    public DataMgrPage()
    {
        add( menuPanel );
        add( dataMgrForm );
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class DataMgrForm extends StatelessForm<DataStructure>
    {
        private static final long serialVersionUID = 1L;

        private boolean canConfigureData = ( ( Session ) getSession() ).getRoles().hasRole( RequiredRoles.ROLE_CONFIGURE_REF_DATA );

        private final Button submitButton = new Button( "ButtonSubmit" );

        private TreeTable dataMgrTreeTable = new TreeTable( "DataTree" , getTreeModel() , getColumns() )
                                             {
                                                 private static final long serialVersionUID = 1L;

                                                 protected void onNodeLinkClicked( final AjaxRequestTarget pTarget , final TreeNode pNode )
                                                 {
                                                     final DataStructure dataStructure     = ( ( DataStructure ) ( ( DefaultMutableTreeNode ) pNode ).getUserObject() );
                                                     final String        dataStructureType = dataStructure.getRdtCode();

                                                     if ( dataStructure.getEditable() )
                                                     {

                                                         if ( dataStructureType.equals( "SPL" ) )
                                                         {
                                                             setResponsePage( new StandardListMgrPage( dataStructure ) );
                                                         }
                                                         else if ( dataStructureType.equals( "SDV" ) )
                                                         {
                                                             setResponsePage( new SubdivisionListMgrPage( dataStructure ) );
                                                         }
                                                         else if ( dataStructureType.equals( "CLT" ) )
                                                         {
                                                             setResponsePage( new CodedListMgrPage( dataStructure ) );
                                                         }
                                                         else if ( dataStructureType.equals( "ULT" ) )
                                                         {
                                                             setResponsePage( new UpdateOnlyListMgrPage( dataStructure ) );
                                                         }
                                                         else if ( dataStructureType.equals( "ILT" ) )
                                                         {
                                                             setResponsePage( new IntersectionListMgrPage( dataStructure ) );
                                                         }

                                                     }
                                                 }
                                             };

        private final TreeControlPanel treeControlPanel = new TreeControlPanel( "PanelTreeControl" , dataMgrTreeTable );

        /**
         *
         * @param pId
         */
        public DataMgrForm( final String pId )
        {
            super( pId );
            add( submitButton );

            dataMgrTreeTable.setRootLess( true );
            dataMgrTreeTable.getTreeState().expandAll();
            add( dataMgrTreeTable );
            add( treeControlPanel );

            submitButton.setVisible( canConfigureData );
        }

        /**
         *
         * @return
         */
        private IColumn[] getColumns()
        {
            Localizer localizer = getLocalizer();

            List<IColumn> treeTableColumns = new ArrayList<IColumn>();

            treeTableColumns.add( new PropertyTreeColumn( new ColumnLocation( Alignment.MIDDLE , 18 , Unit.PROPORTIONAL ) , localizer.getString( "HeaderDataStructureName" , this ) , "userObject.refdataDescr" ) );

            if ( canConfigureData )
            {
                treeTableColumns.add
                (
                    new PropertyRenderableColumn( new ColumnLocation( Alignment.RIGHT , 8 , Unit.EM ) , localizer.getString( "HeaderMakeEditable" , this ) , "userObject.editable" )
                    {
                        private static final long serialVersionUID = 1L;

                        /**
                         * @see IColumn#newCell( MarkupContainer, String, TreeNode, int )
                         */
                        @Override
                        public Component newCell( final MarkupContainer pParent , final String pId , final TreeNode pNode , final int pLevel )
                        {
                            return new CheckboxPanel( pId , new PropertyModel<Boolean>( pNode , getPropertyExpression() ) );
                        }

                        /**
                         * @see IColumn#newCell( TreeNode, int )
                         */
                        public IRenderable newCell( final TreeNode pNode , final int pLevel )
                        {
                            return null;
                        }
                    }
                );
            }

            return treeTableColumns.toArray( new IColumn [ 0 ] );
        }

        /**
         *
         * @return
         */
        private TreeModel getTreeModel()
        {
            Session session = ( Session ) getSession();

            DataMgrDAO dataService = null;
            TreeModel  treeModel   = null;

            try
            {
                dataService = new DataMgrDAO( session.getUsername() , session.getPassword() );

                treeModel = dataService.getRefdataTree( new DefaultMutableTreeNode( getLocalizer().getString( "LabelRootTreeNode" , this ) ) );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving list of reference data structures -> {}; error code -> {}; sql state -> {}"
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
         */
        @Override
        public void onSubmit()
        {
            Session session = ( Session ) getSession();

            DataMgrDAO dataService = null;

            try
            {
                dataService = new DataMgrDAO( session.getUsername() , session.getPassword() );

                dataService.configRefdata( dataMgrTreeTable.getModelObject() );

                dataService.doCommit();

                dataMgrTreeTable.setModelObject( getTreeModel() );

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
                    "SQL Exception when configuring reference data structures -> {}; error code -> {}; sql state -> {}"
                ,   new Object[]
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
         */
        @Override
        public void onBeforeRender()
        {
            final TreeModel treeModel = dataMgrTreeTable.getModelObject();

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
