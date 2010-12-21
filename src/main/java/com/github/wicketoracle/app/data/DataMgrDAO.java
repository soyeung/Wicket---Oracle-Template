package com.github.wicketoracle.app.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class DataMgrDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( DataMgrDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public DataMgrDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     *
     * @return a tree representation of the available reports.
     * root nodes represent report categories; leaves represent actual reports
     *
     */
    public TreeModel getRefdataTree( final TreeNode pRootNode ) throws SQLException
    {
        final String dbStatement =  " begin "
                                  + "   SYS.dbms_application_info.set_module( module_name => ? , action_name => ? ); "
                                  + "   ? := app_refdata.pk_ref_data_mgr.fn_get_data_structure_list;"
                                  + " end;  ";

        OracleCallableStatement dbCstmt = null;
        OracleResultSet         dbRs    = null;

        try
        {
            /* retrieve data */

            LOGGER.debug( "Get refdata tree" );

            setRole( RequiredRoles.ROLE_REF_DATA_MGR );

            LOGGER.debug( "Role set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_REF_DATA_MGR" );
            dbCstmt.setString( 2 , "FN_GET_DATA_STRUCTURE_LIST" );
            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            /* build tree - each row in the result set represents a category */

            DefaultMutableTreeNode rootNode        = ( DefaultMutableTreeNode ) pRootNode;
            DefaultMutableTreeNode prevRefdataNode = null;
            DefaultMutableTreeNode currRefdataNode = null;

            int prevRefdataLevel = -1;
            int currRefdataLevel = -1;

            while ( dbRs.next() )
            {
                boolean tempIsEditable = false;
                if ( dbRs.getString( "IS_EDITABLE" ).equals( "Y" ) )
                {
                    tempIsEditable = true;
                }

                currRefdataLevel = dbRs.getInt( "HLEVEL" );
                currRefdataNode  = new DefaultMutableTreeNode
                                      (
                                           new DataStructure
                                              (
                                                   dbRs.getInt( "ID" )
                                               ,   dbRs.getString( "CODE" )
                                               ,   dbRs.getString( "DESCR" )
                                               ,   tempIsEditable
                                               ,   dbRs.getString( "RDT_CODE" )
                                               ,   false
                                               )
                                       );

                /* add new report category to the tree */

                if ( currRefdataLevel == 1 )
                {
                    rootNode.add( currRefdataNode );
                }
                else
                {
                    if ( currRefdataLevel == prevRefdataLevel )
                    {
                        ( ( DefaultMutableTreeNode ) prevRefdataNode.getParent() ).add( currRefdataNode );
                    }
                    else if ( currRefdataLevel > prevRefdataLevel )
                    {
                        prevRefdataNode.add( currRefdataNode );
                    }
                    else if ( currRefdataLevel < prevRefdataLevel )
                    {
                        // find tree node with level < currReportCategoryLevel and make the current node a child of it
                        DefaultMutableTreeNode searchNode = ( DefaultMutableTreeNode ) prevRefdataNode.getParent();

                        do
                        {
                            if ( searchNode.getLevel() < currRefdataLevel )
                            {
                                searchNode.add( currRefdataNode );
                                break;
                            }
                            else
                            {
                                searchNode = ( DefaultMutableTreeNode ) searchNode.getParent();
                            }
                        }
                        while ( true );
                    }
                }

                /* store references to the just processed node */
                prevRefdataNode  = currRefdataNode;
                prevRefdataLevel = currRefdataLevel;
            }

            return new DefaultTreeModel( pRootNode );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
           (
                "SQL Exception whilst creating getting report tree -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            throw sqle;
        }
        finally
        {
            CloseResource.close( dbRs );
            CloseResource.close( dbCstmt );
        }
    }

    /**
     *
     * @throws SQLException
     */
    public void configRefdata( final TreeModel pRefdataTree ) throws SQLException, NothingToDoException
    {
        final String dbStatement =  " begin "
                                  + "   SYS.dbms_application_info.set_module( module_name => ? , action_name => ? ); "
                                  + "   app_refdata.pk_configure_ref_data.pr_configure_ref_data( p_data_structure => ? );"
                                  + " end;  ";

        OracleCallableStatement dbCstmt = null;

        try
        {
            setRole( RequiredRoles.ROLE_CONFIGURE_REF_DATA );

            LOGGER.debug( "Role set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            /* retrieve list of reference data structures */
            Enumeration<DefaultMutableTreeNode> treeNodes = ( ( DefaultMutableTreeNode ) pRefdataTree.getRoot() ).preorderEnumeration();

            /* check that some modifications have taken place */
            List <Object[]> moddedNodes = new ArrayList<Object[]>();

            while ( treeNodes.hasMoreElements() )
            {
                Object node = treeNodes.nextElement().getUserObject();

                if ( node instanceof DataStructure )
                {
                    DataStructure refDataNode = ( DataStructure ) node;

                    if ( refDataNode.isModified() )
                    {
                        Object tempId = null;
                        if ( refDataNode.getRdsId() != 0 )
                        {
                            tempId = Integer.valueOf( refDataNode.getRdsId() );
                        }

                        String tempIsEditable = "N";
                        if ( refDataNode.getEditable() )
                        {
                            tempIsEditable = "Y";
                        }

                        moddedNodes.add
                        (
                            new Object [ ]
                            {
                                tempId
                            ,   tempIsEditable
                            }
                        );
                    }
                }
            }

            /* if moddedNodes is empty then nothing needs to be done */
            if ( moddedNodes.size() == 0 )
            {
                throw new NothingToDoException();
            }

            LOGGER.debug( "{} ref data structures mod(s) specified" , moddedNodes.size() );

            STRUCT [ ] moddedDbNodes = new STRUCT [ moddedNodes.size() ];

            LOGGER.debug( "User STRUCT[] built : size -> {}" , moddedNodes.size() );

            StructDescriptor tyRefDataStructure = StructDescriptor.createDescriptor( "APP_REFDATA.TY_REF_DATA_STRUCTURE" , getConnection() );

            LOGGER.debug( "ty_ref_data_structure structure retrieved" );

            ArrayDescriptor ttyRefDataStructure = ArrayDescriptor.createDescriptor( "APP_REFDATA.TTY_REF_DATA_STRUCTURE" , getConnection() );

            LOGGER.debug( "tty_ref_data_structure structure retrieved" );

            for ( int i = 0 ; i < moddedDbNodes.length ; i++ )
            {
                moddedDbNodes [ i ] = new STRUCT( tyRefDataStructure , getConnection() , moddedNodes.get( i ) );
            }

            /* if we have reached this stage then changes have been specified, and now an ARRAY can be built, and our sp. executed */

            dbCstmt.setString( 1 , "PK_CONFIGURE_REF_DATA" );
            dbCstmt.setString( 2 , "PR_CONFIGURE_REF_DATA" );
            dbCstmt.setARRAY( 3 , new ARRAY( ttyRefDataStructure , getConnection() , moddedDbNodes ) );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
           (
                "SQL Exception whilst configuring reference data -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            throw sqle;
        }
        finally
        {
            CloseResource.close( dbCstmt );
        }
    }
}
