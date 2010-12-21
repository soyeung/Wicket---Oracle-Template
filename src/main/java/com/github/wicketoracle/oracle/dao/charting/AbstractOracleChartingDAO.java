package com.github.wicketoracle.oracle.dao.charting;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;


public abstract class AbstractOracleChartingDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractOracleChartingDAO.class );

    /**
     *
     * @param pUsername
     * @param pPassword
     * @throws SQLException
     */
    protected AbstractOracleChartingDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     *
     * @param pDbRs
     * @return
     * @throws SQLException
     */
    protected final Map<String , List<ChartDatum>> getChartData( final OracleResultSet pDbRs ) throws SQLException
    {
        Map<String , List<ChartDatum>> mapChartData  = new HashMap <String , List<ChartDatum>>();
        List<ChartDatum>               listChartData = new ArrayList <ChartDatum>();

        int count = 0;

        String prevKey = "";
        String currKey = "";

        while ( pDbRs.next() )
        {
            currKey = pDbRs.getString( "DATA_SET_KEY" );

            if ( count == 0 )
            {
                prevKey = currKey;
            }

            if ( ! currKey.equals( prevKey ) )
            {
                if ( count > 0 )
                {
                    mapChartData.put( prevKey , listChartData );
                }

                listChartData = new ArrayList <ChartDatum>();

                prevKey = currKey;
            }

            listChartData.add( new ChartDatum( pDbRs.getInt( "DATA_KEY" ) , pDbRs.getString( "DATA_VALUE" ) ) );

            count++;
        }

        if ( listChartData.size() > 0 )
        {
            mapChartData.put( currKey , listChartData );
        }

        LOGGER.debug( "Reference data built : #sets of data -> {}" , mapChartData.size() );

        /* finished building reference data map */

        return mapChartData;
    }

    /**
     *
     * @param pDbUser
     * @param pPackage
     * @param pFunction
     * @return
     * @throws SQLException
     */
    protected final Map<String , List<ChartDatum>> getChartData( final String pDbUser , final String pPackage , final String pFunction ) throws SQLException
    {
        final String dbStatement =   " begin  "
                                   + "   sys.dbms_application_info.set_module( module_name => ? , action_name => ? ); "
                                   + "   ? := " + pDbUser + "." + pPackage + "." + pFunction + ";"
                                   + " end;   ";

        OracleCallableStatement dbCstmt  = null;
        OracleResultSet         dbRs     = null;

        try
        {
            /* retrieve data */

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}", dbStatement );

            dbCstmt.setString( 1 , pPackage.toUpperCase() );
            dbCstmt.setString( 2 , pFunction.toUpperCase() );
            dbCstmt.registerOutParameter( 3 , OracleTypes.CURSOR );

            LOGGER.debug( "DB params registered" );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );

            dbRs = ( OracleResultSet ) dbCstmt.getCursor( 3 );

            /* build reference data map [a map of lists] */

            return getChartData( dbRs );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst retrieving chart data -> {}; db user -> {}; package -> {}; function -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]{
                                  pDbUser
                              ,   pPackage
                              ,   pFunction
                              ,   sqle.getMessage()
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
