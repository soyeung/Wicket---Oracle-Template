package com.github.wicketoracle.app.report.userprivileges;

import java.sql.SQLException;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class UserPrivilegesReportDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( UserPrivilegesReportDAO.class );

    /**
     *
     * @param pUsername
     * @param pPassword
     * @throws SQLException
     */
    public UserPrivilegesReportDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     * @return The reference data lists required by the user privileges report
     * @throws SQLException
     */
    public Map <String , SelectChoiceList<IntegerSelectChoice>> getKeyValueRefData() throws SQLException
    {
        setRole( RequiredRoles.ROLE_USER_PRIVILEGES_REPORT );

        return getKeyValueRefData( "app_report" , "pk_user_privileges_report" , "fn_get_list_ref_data" );
    }

    /**
     *
     * @param pRootNode
     * @return
     * @throws SQLException
     */
    public TreeModel getSecurityTree( final TreeNode pRootNode , final int pUserId ) throws SQLException
    {
        final String dbStatement =  " begin "
                                  + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                  + "   ? := app_report.pk_user_privileges_report.fn_get_report( p_aur_id => ? );"
                                  + " end;  ";

        OracleCallableStatement dbCstmt = null;
        OracleResultSet         dbRs    = null;

        try
        {
            /* retrieve data */

            LOGGER.debug( "Get report tree" );

            setRole( RequiredRoles.ROLE_USER_PRIVILEGES_REPORT );

            LOGGER.debug( "Role set -> {}" , RequiredRoles.ROLE_USER_PRIVILEGES_REPORT );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1, "PK_USER_PRIVILEGES_REPORT" );
            dbCstmt.setString( 2, "FN_GET_REPORT" );
            dbCstmt.registerOutParameter( 3, OracleTypes.CURSOR );
            dbCstmt.setInt( 4, pUserId );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            /* build tree - each row in the result set represents a category */

            DefaultMutableTreeNode rootNode               = ( DefaultMutableTreeNode ) pRootNode;
            DefaultMutableTreeNode prevReportCategoryNode = null;
            DefaultMutableTreeNode currReportCategoryNode = null;

            int prevReportCategoryLevel = -1;
            int currReportCategoryLevel = -1;

            while ( dbRs.next() )
            {
                currReportCategoryLevel = dbRs.getInt( "HLEVEL" );
                currReportCategoryNode  = new DefaultMutableTreeNode( dbRs.getString( "OBJECT_PRIVILEGE" ).toLowerCase() );

                /* add new object privilege to the tree */

                if ( currReportCategoryLevel == 1 )
                {
                    rootNode.add( currReportCategoryNode );
                }
                else
                {
                    if ( currReportCategoryLevel == prevReportCategoryLevel )
                    {
                        ( ( DefaultMutableTreeNode ) prevReportCategoryNode.getParent() ).add( currReportCategoryNode );
                    }
                    else if ( currReportCategoryLevel > prevReportCategoryLevel )
                    {
                        prevReportCategoryNode.add( currReportCategoryNode );
                    }
                    else if ( currReportCategoryLevel < prevReportCategoryLevel )
                    {
                        // find tree node with level < currReportCategoryLevel and make the current node a child of it
                        DefaultMutableTreeNode searchNode = ( DefaultMutableTreeNode ) prevReportCategoryNode.getParent();

                        do
                        {
                            if ( searchNode.getLevel() < currReportCategoryLevel )
                            {
                                searchNode.add( currReportCategoryNode );
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
                prevReportCategoryNode  = currReportCategoryNode;
                prevReportCategoryLevel = currReportCategoryLevel;
            }

            return new DefaultTreeModel( pRootNode );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst creating getting security model tree -> {}; error code -> {}; sql state -> {}"
            ,   new Object []
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
}
