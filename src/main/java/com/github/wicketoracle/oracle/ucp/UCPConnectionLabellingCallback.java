package com.github.wicketoracle.oracle.ucp;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.ucp.ConnectionLabelingCallback;

/**
 * Allows the application to use Oracle Labelled Connections
 *
 * @author Andrew Hall
 *
 */
final class UCPConnectionLabellingCallback implements ConnectionLabelingCallback
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( UCPConnectionLabellingCallback.class );

    /**
     * Constructor
     */
    UCPConnectionLabellingCallback()
    {

    }

    /**
     * @see oracle.ucp.ConnectionLabelingCallback#cost( Properties, Properties )
     */
    public int cost( final Properties pRequestedLabels, final Properties pCurrentLabels )
    {
        final String currentUsername   = pCurrentLabels.getProperty( UCPConnectionLabelKey.USERNAME.toString() );
        final String requestedUsername = pRequestedLabels.getProperty( UCPConnectionLabelKey.USERNAME.toString() );

        int cost = 0;

        LOGGER.debug( "Current user -> {} ; Requested user -> {}", currentUsername, requestedUsername );

        if ( ! requestedUsername.equals( currentUsername ) )
        {
            cost = Integer.MAX_VALUE;
        }

        LOGGER.debug( "Cost -> {}", cost );

        return cost;
    }

    /**
     * @see oracle.ucp.ConnectionLabelingCallback#configure( Properties, Object )
     */
    public boolean configure( final Properties pRequestedLabels, final Object pConnection )
    {
        boolean success = true;

        if ( pRequestedLabels == null )
        {
            success = false;
        }

        String debugMessage;

        if ( success )
        {
            debugMessage = "Connection was successfully configured";
        }
        else
        {
            debugMessage = "Connection was not successfully configured";
        }

        LOGGER.debug( debugMessage );

        return success;
    }
}
