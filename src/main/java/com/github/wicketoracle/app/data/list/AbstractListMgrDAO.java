package com.github.wicketoracle.app.data.list;

import java.sql.SQLException;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.data.RequiredRoles;
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;


public abstract class AbstractListMgrDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractListMgrDAO.class );

    /**
     * Constructor
     */
    public AbstractListMgrDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    public final ListMetaData getMetaData( final int pDataStructureId ) throws SQLException
    {
        final String dbStatement = " begin "
                                 + "   sys.dbms_application_info.set_module( module_name => ? , action_name => ? ); "
                                 + "   ? := app_refdata.pk_ref_data_mgr.fn_get_list_metadata( p_rds_id => ?);"
                                 + " end; ";

        OracleCallableStatement dbCstmt = null;
        OracleResultSet         dbRs    = null;

        ListMetaData metaData = new ListMetaData();

        try
        {
            LOGGER.debug( "Retrieve list data" );

            setRole( RequiredRoles.ROLE_REF_DATA_MGR );

            LOGGER.debug( "Roles set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_REF_DATA_MGR" );
            dbCstmt.setString( 2 , "FN_GET_LIST_METADATA" );

            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );
            dbCstmt.setInt( 4 , pDataStructureId );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            dbRs.next();

            metaData.setCodeMaxLength( dbRs.getInt( "MAX_CODE_LENGTH" ) );
            metaData.setItemNameMaxLength( dbRs.getInt( "MAX_NAME_LENGTH" ) );

            return metaData;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
           (
                "SQL Exception whilst retrieving list ref data -> {}; error code -> {}; sql state -> {}; pRdsId -> {}"
            ,   new Object [ ]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                ,   pDataStructureId
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
