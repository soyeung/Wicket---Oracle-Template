package com.github.wicketoracle.app.report.sessionlock;

import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class SessionLockReportDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( SessionLockReportDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public SessionLockReportDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
    *
    * @return A list of unindexed foreign keys
    */
    public TreeModel getReport( final TreeNode pRootNode ) throws SQLException
    {
       final String dbStatement = " begin "
                                + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                + "   ? := app_report.pk_session_lock_report.fn_get_report; "
                                + " end;  ";

       OracleCallableStatement dbCstmt  = null;
       OracleResultSet         dbRs     = null;

       try
       {
           LOGGER.debug( "Run unindexed foreign key report" );

           setRole( RequiredRoles.ROLE_SESSION_LOCK_REPORT );

           LOGGER.debug( "Role set" );

           /* retrieve data */

           dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

           LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

           dbCstmt.setString( 1 , "PK_SESSION_LOCK_REPORT" );
           dbCstmt.setString( 2 , "FN_GET_REPORT" );
           dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );

           dbCstmt.execute();

           LOGGER.debug( "DB statement executed" );

           dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

           /* build report data */

           DefaultMutableTreeNode rootNode = ( DefaultMutableTreeNode ) pRootNode;
           DefaultMutableTreeNode prevNode = null;
           DefaultMutableTreeNode currNode = null;

           int prevLevel = -1;
           int currLevel = -1;

           while ( dbRs.next() )
           {
               currLevel = dbRs.getInt( "HLEVEL" );
               currNode  = new DefaultMutableTreeNode
                               (
                                   new ReportRecord
                                       (
                                           dbRs.getInt( "SESSION_ID" )
                                       ,   dbRs.getString( "USERNAME" )
                                       ,   dbRs.getString( "LOCK_TYPE" )
                                       ,   dbRs.getString( "MODE_HELD" )
                                       ,   dbRs.getString( "MODE_REQUESTED" )
                                       ,   dbRs.getString( "LOCK_ID1" )
                                       ,   dbRs.getString( "LOCK_ID2" )
                                       ,   dbRs.getString( "CURR_SQL_TEXT" )
                                       ,   dbRs.getString( "PREV_SQL_TEXT" )
                                       )
                               );

               LOGGER.debug
               (
                   "Lock Retrieved :: Session Id -> {} ; Username -> {} ; current level -> {} ; previous level -> {} "
               ,   new Object[]
                   {
                       ( ( ReportRecord ) currNode.getUserObject() ).getSessionId()
                   ,   ( ( ReportRecord ) currNode.getUserObject() ).getUsername()
                   ,   currLevel
                   ,   prevLevel
                   }
               );

               /* add new report category to the tree */

               if ( currLevel == 1 )
               {
                   rootNode.add( currNode );
               }
               else
               {
                   if ( currLevel == prevLevel )
                   {
                       ( ( DefaultMutableTreeNode ) prevNode.getParent() ).add( currNode );
                   }
                   else if ( currLevel > prevLevel )
                   {
                       prevNode.add( currNode );
                   }
                   else if ( currLevel < prevLevel )
                   {
                       // find tree node with level < currReportCategoryLevel and make the current node a child of it
                       DefaultMutableTreeNode searchNode = ( DefaultMutableTreeNode ) prevNode.getParent();

                       do
                       {
                           if ( searchNode.getLevel() < currLevel )
                           {
                               searchNode.add( currNode );
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
               prevNode  = currNode;
               prevLevel = currLevel;
           }

           return new DefaultTreeModel( pRootNode );
       }
       catch ( SQLException sqle )
       {
           LOGGER.error
           (
               "SQL Exception whilst running the session lock report -> {}; error code -> {}; sql state -> {}"
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
}
