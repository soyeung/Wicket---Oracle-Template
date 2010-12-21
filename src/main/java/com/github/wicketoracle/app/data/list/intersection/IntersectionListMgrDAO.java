package com.github.wicketoracle.app.data.list.intersection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.data.RequiredRoles;
import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;


final class IntersectionListMgrDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( IntersectionListMgrDAO.class );

    /**
     * Constructor
     */
    public IntersectionListMgrDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     * return the list of parent list items
     */
    public SelectChoiceList<IntegerSelectChoice> getParentData( final int pDataStructureId , final String pDbRoleName ) throws SQLException
    {
        final String dbStatement =   " BEGIN  "
                                   + "   SYS.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                   + "   ? := app_refdata.pk_intersection_list_mgr.fn_get_parent_data( p_rds_id => ? );"
                                   + " END;   ";

        OracleCallableStatement dbCstmt = null;
        OracleResultSet         dbRs    = null;

        SelectChoiceList<IntegerSelectChoice> parentData = new SelectChoiceList<IntegerSelectChoice>();

        try
        {
            LOGGER.debug( "Retrieve intersection parent list data" );

            String[] dbRoles = { RequiredRoles.ROLE_REF_DATA_MGR, pDbRoleName};

            setRoles( dbRoles );

            LOGGER.debug( "Roles set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1, "PK_INTERSECTION_LIST_MGR" );
            dbCstmt.setString( 2, "FN_GET_PARENT_DATA" );
            dbCstmt.registerOutParameter( 3, OracleTypes.CURSOR );
            dbCstmt.setInt( 4, pDataStructureId );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            while ( dbRs.next() )
            {
                parentData.add( new IntegerSelectChoice( dbRs.getInt( "ID" ), dbRs.getString( "NAME" ) ) );
            }

            return parentData;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst retrieving intersection parent data -> {}; error code -> {}; sql state -> {}; pRdsId -> {}; pDbrlName -> {}"
            ,   new Object []
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pDataStructureId
                ,   pDbRoleName
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
     * Retrieve the child data, split into selected and available lists
     * @param pDataStructureId
     * @param pDbRoleName
     * @param pParentItemId
     * @return
     * @throws SQLException
     */
    public Map<String , SelectChoiceList<IntegerSelectChoice>> getChildData( final int pDataStructureId , final String pDbRoleName , final int pParentItemId ) throws SQLException
    {
        final String dbStatement =   " BEGIN  "
                                   + "   SYS.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                   + "   ? := app_refdata.pk_intersection_list_mgr.fn_get_child_data ( p_rds_id => ? , p_parent_id => ? );"
                                   + " END;   ";

        OracleCallableStatement dbCstmt = null;
        OracleResultSet         dbRs    = null;

        try
        {
            LOGGER.debug( "Retrieve intersection parent list data" );

            String[] dbRoles = { RequiredRoles.ROLE_REF_DATA_MGR, pDbRoleName};

            setRoles( dbRoles );

            LOGGER.debug( "Roles set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1 , "PK_INTERSECTION_LIST_MGR" );
            dbCstmt.setString( 2 , "FN_GET_CHILD_DATA" );
            dbCstmt.registerOutParameter( 3, OracleTypes.CURSOR );
            dbCstmt.setInt( 4 , pDataStructureId );
            dbCstmt.setInt( 5 , pParentItemId );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            return getKeyValueRefData( dbRs );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst retrieving intersection child data -> {}; error code -> {}; sql state -> {}; pRdsId -> {}; pDbrlName -> {}; pItemId -> {}"
            ,   new Object []
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pDataStructureId
                ,   pDbRoleName
                ,   pParentItemId
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
     * Apply updates
     */
    public void updateData( final int pRdsId , final String pDbrlName , final int pParentItemId , final SelectChoiceList<IntegerSelectChoice> pListData ) throws SQLException
    {
        final String dbStatement =   " BEGIN  "
                                   + "   SYS.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                   + "   app_refdata.pk_intersection_list_mgr.pr_update( p_rds_id => ? , p_parent_id => ? , p_dataset => ?);"
                                   + " END;   ";

        OracleCallableStatement dbCstmt = null;

        try
        {
            /* retrieve data */

            LOGGER.debug( "Update list of subdivision list data" );

            String[] dbRoles = { RequiredRoles.ROLE_REF_DATA_MGR, pDbrlName};

            setRoles( dbRoles );

            LOGGER.debug( "Roles set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            /* check that some modifications have taken place */
            List<Object[]> moddedListData = new ArrayList<Object[]>();

            for ( IntegerSelectChoice listItem : pListData )
            {
                moddedListData.add
                (
                    new Object[]
                    {
                        listItem.getKey()
                    }
                );
            }

            STRUCT[] moddedDbListData = new STRUCT[moddedListData.size()];

            LOGGER.debug( "User STRUCT[] built : size -> {}", moddedListData.size() );

            StructDescriptor tyIntersectionListRefData = StructDescriptor.createDescriptor( "APP_REFDATA.TY_INTERSECTION_LIST_REF_DATA", getConnection() );

            LOGGER.debug( "ty_intersection_list_ref_data structure retrieved" );

            ArrayDescriptor ttyIntersectionListRefData = ArrayDescriptor.createDescriptor( "APP_REFDATA.TTY_INTERSECTION_LIST_REF_DATA", getConnection() );

            LOGGER.debug( "tty_intersection_list_ref_data structure retrieved" );

            for ( int i = 0 ; i < moddedDbListData.length ; i++ )
            {
                moddedDbListData[i] = new STRUCT( tyIntersectionListRefData , getConnection(), moddedListData.get( i ) );
            }

            /* if we have reached this stage then changes have been specified, and now an ARRAY can be built, and our sp. executed */

            dbCstmt.setString( 1 , "PK_INTERSECTION_LIST_MGR" );
            dbCstmt.setString( 2 , "PR_UPDATE" );
            dbCstmt.setInt( 3 , pRdsId );
            dbCstmt.setInt( 4 , pParentItemId );
            dbCstmt.setARRAY( 5 , new ARRAY( ttyIntersectionListRefData , getConnection() , moddedDbListData ) );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst updating list intersection ref data -> {}; error code -> {}; sql state -> {}; pRdsId -> {}; pDbrlName -> {}; pParentItemId -> {}"
            ,   new Object []
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pRdsId
                ,   pDbrlName
                ,   pParentItemId
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
