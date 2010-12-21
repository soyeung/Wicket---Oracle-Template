package com.github.wicketoracle.app.report.tableswithoutpk;

import org.apache.wicket.IClusterable;

final class ReportRecord implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private String tableOwner;
    private String tableName;

    public ReportRecord
    (
        final String pTableOwner
    ,   final String pTableName
    )
    {
        super();
        tableOwner = pTableOwner;
        tableName  = pTableName;
    }

    public String getTableOwner()
    {
        return tableOwner;
    }

    public void setTableOwner( final String pTableOwner )
    {
        tableOwner = pTableOwner;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName( final String pTableName )
    {
        tableName = pTableName;
    }
}
