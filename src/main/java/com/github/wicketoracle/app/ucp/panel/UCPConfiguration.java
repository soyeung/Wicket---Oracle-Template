package com.github.wicketoracle.app.ucp.panel;

import oracle.ucp.jdbc.PoolDataSource;

import org.apache.wicket.IClusterable;

final class UCPConfiguration implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private String connectionfactoryClassName;
    private String connectionPoolName;
    private String url;

    private int maxCachedStatements;
    private int minPoolSize;
    private int maxPoolSize;

    public UCPConfiguration( final PoolDataSource pPoolDataSource )
    {
        setConnectionfactoryClassName( pPoolDataSource.getConnectionFactoryClassName() );
        setConnectionPoolName( pPoolDataSource.getConnectionPoolName() );
        setUrl( pPoolDataSource.getURL() );
        setMaxCachedStatements( pPoolDataSource.getMaxStatements() );
        setMinPoolSize( pPoolDataSource.getMinPoolSize() );
        setMaxPoolSize( pPoolDataSource.getMaxPoolSize() );
    }

    public String getConnectionfactoryClassName()
    {
        return connectionfactoryClassName;
    }

    public void setConnectionfactoryClassName( final String pConnectionfactoryClassName )
    {
        connectionfactoryClassName = pConnectionfactoryClassName;
    }

    public String getConnectionPoolName()
    {
        return connectionPoolName;
    }

    public void setConnectionPoolName( final String pConnectionPoolName )
    {
        connectionPoolName = pConnectionPoolName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl( final String pUrl )
    {
        url = pUrl;
    }

    public int getMaxCachedStatements()
    {
        return maxCachedStatements;
    }

    public void setMaxCachedStatements( final int pMaxCachedStatements )
    {
        maxCachedStatements = pMaxCachedStatements;
    }

    public int getMinPoolSize()
    {
        return minPoolSize;
    }

    public void setMinPoolSize( final int pMinPoolSize )
    {
        minPoolSize = pMinPoolSize;
    }

    public int getMaxPoolSize()
    {
        return maxPoolSize;
    }

    public void setMaxPoolSize( final int pMaxPoolSize )
    {
        maxPoolSize = pMaxPoolSize;
    }
}
