package com.github.wicketoracle.app.data.list.coded.panel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.data.list.AbstractListMgrDAO;
import com.github.wicketoracle.app.data.list.ListDatum;
import com.github.wicketoracle.app.data.list.RequiredRoles;
import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.html.form.choice.StringSelectChoice;
import com.github.wicketoracle.oracle.util.CloseResource;


final class CodedListMgrDAO extends AbstractListMgrDAO
{
    private static final Logger LOGGER = LoggerFactory.getLogger( CodedListMgrDAO.class );

    /**
     * Constructor
     */
    public CodedListMgrDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     *
     * @param pDataStructureId
     * @param pDbRoleName
     * @return
     */
    public List <ListDatum> getData( final int pDataStructureId , final String pDbRoleName ) throws SQLException
    {
        final String dbStatement = " BEGIN "
                                 + "   SYS.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   ? := app_refdata.pk_coded_list_mgr.fn_get_data ( p_rds_id => ? );"
                                 + " END; ";

        OracleCallableStatement dbCstmt = null;
        OracleResultSet         dbRs    = null;

        List <ListDatum> listItems = new ArrayList<ListDatum>();

        try
        {
            String [ ] dbRoles = { RequiredRoles.ROLE_REF_DATA_MGR , pDbRoleName };

            setRoles( dbRoles );

            LOGGER.debug( "Roles set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PK_CODED_LIST_MGR" );
            dbCstmt.setString( 2 , "FN_GET_DATA" );
            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );
            dbCstmt.setInt( 4 , pDataStructureId );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            while ( dbRs.next() )
            {
                listItems.add
                (
                    new ListDatum
                    (
                        dbRs.getInt( "ID" )
                    ,   dbRs.getString( "CODE" )
                    ,   dbRs.getString( "NAME" )
                    ,   new StringSelectChoice( dbRs.getString( "IS_USER_VISIBLE" ) )
                    ,   dbRs.getInt( "ORDERING" )
                    ,   dbRs.getTIMESTAMP( "UPDATED_DATE" )
                    ,   false
                    )
                );
            }

            return listItems;
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst retrieving list ref data -> {}; error code -> {}; sql state -> {}; pRdsId -> {}; pDbrlName -> {}"
            ,   new Object [ ]
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
     *
     * @param pDataStructureId
     * @param pDbRoleName
     * @param pListData
     */
    public void updateData( final int pDataStructureId , final String pDbRoleName , final List <ListDatum> pListData ) throws SQLException , NothingToDoException
    {
        final String dbStatement = " BEGIN "
                                 + "   SYS.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                 + "   app_refdata.pk_coded_list_mgr.pr_update ( p_rds_id => ?, p_dataset => ? );"
                                 + " END; ";

        OracleCallableStatement dbCstmt = null;

        try
        {
            /* retrieve data */

            LOGGER.debug( "Update coded list of reference data" );

            String [ ] dbRoles = { RequiredRoles.ROLE_REF_DATA_MGR , pDbRoleName };

            setRoles( dbRoles );

            LOGGER.debug( "Roles set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            /* check that some modifications have taken place */
            List < Object [ ] > moddedListData = new ArrayList < Object [ ] >();

            for ( ListDatum listDataBean : pListData )
            {
                if ( listDataBean.isModified() )
                {
                    Object tempId = null;
                    if ( listDataBean.getId() != 0 )
                    {
                        tempId = Integer.valueOf( listDataBean.getId() );
                    }

                    moddedListData.add
                    (
                        new Object [ ]
                        {
                            tempId
                        ,   listDataBean.getCode()
                        ,   listDataBean.getItemName()
                        ,   listDataBean.getItemIsVisible()
                        ,   listDataBean.getItemOrder()
                        ,   listDataBean.getUpdatedDate()
                        }
                    );
                }
            }

            /* if moddedUsers is empty then nothing needs to be done */
            if ( moddedListData.size() == 0 )
            {
                throw new NothingToDoException();
            }

            LOGGER.debug( "{} user mod(s) specified" , moddedListData.size() );

            STRUCT [ ] moddedDbListData = new STRUCT [ moddedListData.size() ];

            LOGGER.debug( "User STRUCT[] built : size -> {}" , moddedListData.size() );

            StructDescriptor tyCodedListRefData = StructDescriptor.createDescriptor( "APP_REFDATA.TY_CODED_LIST_REF_DATA" , getConnection() );

            LOGGER.debug( "ty_coded_list_ref_data structure retrieved" );

            ArrayDescriptor ttyCodedListRefData = ArrayDescriptor.createDescriptor( "APP_REFDATA.TTY_CODED_LIST_REF_DATA" , getConnection() );

            LOGGER.debug( "tty_coded_list_ref_data structure retrieved" );

            for ( int i = 0 ; i < moddedDbListData.length ; i++ )
            {
                moddedDbListData [ i ] = new STRUCT( tyCodedListRefData , getConnection() , moddedListData.get( i ) );
            }

            /* if we have reached this stage then changes have been specified, and now an ARRAY can be built, and our sp. executed */

            dbCstmt.setString( 1 , "PK_CODED_LIST_MGR" );
            dbCstmt.setString( 2 , "PR_UPDATE" );
            dbCstmt.setInt( 3 , pDataStructureId );
            dbCstmt.setARRAY( 4 , new ARRAY( ttyCodedListRefData , getConnection() , moddedDbListData ) );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst updating coded list ref data -> {}; error code -> {}; sql state -> {}; pRdsId -> {}; pDbrlName -> {}"
            ,   new Object [ ]
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
            CloseResource.close( dbCstmt );
        }
    }
}
