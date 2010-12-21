package com.github.wicketoracle.html.panel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Hall
 *
 */
public final class DynamicResult
{
    private List<String>       resultHeaders = new ArrayList<String>();
    private List<List<String>> results       = new ArrayList<List<String>>();

    /**
     * Constructor
     */
    public DynamicResult( final ResultSet pResultSet ) throws SQLException
    {
        final ResultSetMetaData resultSetMetaData = pResultSet.getMetaData();
        final int numCols = resultSetMetaData.getColumnCount();

        for ( int i = 1 ; i <= numCols ; i++ )
        {
            final String columnName = resultSetMetaData.getColumnName( i );
            resultHeaders.add( columnName );
        }

        while ( pResultSet.next() )
        {
            List<String> resultRow = new ArrayList<String>();

            for ( int j = 0 ; j < numCols ; j++ )
            {
                resultRow.add( pResultSet.getString( j + 1 ) );
            }

            results.add( resultRow );
        }
    }

    public List<String> getResultHeaders()
    {
        return resultHeaders;
    }

    public List<List<String>> getResults()
    {
        return results;
    }
}
