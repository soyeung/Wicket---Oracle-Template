package com.github.wicketoracle.app.report;

import java.sql.SQLException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.STRUCT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


final class ReportDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( ReportDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public ReportDAO( final String pUsername, final String pPassword ) throws SQLException
    {
        super( pUsername, pPassword );
    }

    /**
     *
     * @return a tree representation of the available reports.
     * root nodes represent report categories; leaves represent actual reports
     *
     */
    public TreeModel getReportTree( final TreeNode pRootNode ) throws SQLException
    {
        final String dbStatement =  " begin "
                                  + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                  + "   ? := app_report.pk_reporting.fn_get_report_list;"
                                  + " end;  ";

        OracleCallableStatement dbCstmt = null;
        OracleResultSet         dbRs    = null;

        try
        {
            /* retrieve data */

            LOGGER.debug( "Get report tree" );

            setRole( RequiredRoles.ROLE_REPORT_USER );

            LOGGER.debug( "Role set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1, "PK_REPORTING" );
            dbCstmt.setString( 2, "FN_GET_REPORT_LIST" );
            dbCstmt.registerOutParameter( 3, OracleTypes.CURSOR );

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
                currReportCategoryNode  = new DefaultMutableTreeNode( dbRs.getString( "NAME" ) );

                /* add new report category to the tree */

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

                OracleResultSet dbRepRs = ( OracleResultSet ) dbRs.getARRAY( "RPT_SET" ).getResultSet();

                /* add reports to the current report category */
                try
                {
                    while ( dbRepRs.next() )
                    {
                        STRUCT report = dbRepRs.getSTRUCT( 2 );
                        currReportCategoryNode.add( new DefaultMutableTreeNode( new Report( ( report.getAttributes() )[0].toString(), ( report.getAttributes() )[1].toString(), ( report.getAttributes() )[2].toString() ) ) );
                    }
                }
                catch ( SQLException sqle )
                {
                    LOGGER.error
                    (
                        "SQL Exception whilst retrieving report list -> {}; error code -> {}; sql state -> {}"
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
                    dbRepRs.close();
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
                "SQL Exception whilst creating getting report tree -> {}; error code -> {}; sql state -> {}"
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
