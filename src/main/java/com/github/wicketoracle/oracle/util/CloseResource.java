package com.github.wicketoracle.oracle.util;


import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import com.github.wicketoracle.exception.NotInstantiableException;

/**
 * Helper class aiding db resource management
 *
 * @author Andrew Hall
 *
 */
public final class CloseResource
{
    /**
      * Prevent construction of this utility class.
      */
    protected CloseResource() throws NotInstantiableException
    {
        throw new NotInstantiableException();
    }

    /**
     * Close open db result sets
     *
     * @param pResultSets
     *                   Set of db result sets
     * @throws SQLException
     */
    public static void close( final ResultSet ... pResultSets ) throws SQLException
    {
        if ( pResultSets != null )
        {
            for ( ResultSet resultSet : pResultSets )
            {
                if ( resultSet != null )
                {
                    resultSet.close();
                }
            }
        }
    }

    /**
     * Close open db callable statements
     *
     * @param pCallableStatements
     *                            Set of callable statements
     * @throws SQLException
     */
    public static void close( final CallableStatement ... pCallableStatements ) throws SQLException
    {
        if ( pCallableStatements != null )
        {
            for ( CallableStatement callableStatement : pCallableStatements )
            {
                if ( callableStatement != null )
                {
                    callableStatement.close();
                }
            }
        }
    }
}
