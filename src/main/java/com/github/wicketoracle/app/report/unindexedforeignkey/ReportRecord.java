package com.github.wicketoracle.app.report.unindexedforeignkey;

import org.apache.wicket.IClusterable;

final class ReportRecord implements IClusterable
{
    public static final long serialVersionUID = 1L;

    private String tableOwner;
    private String tableName;
    private String constraintName;
    private String columnNames;

    public ReportRecord( final String pTableOwner, final String pTableName, final String pConstraintName, final String pColumnNames )
    {
        super();
        tableOwner     = pTableOwner;
        tableName      = pTableName;
        constraintName = pConstraintName;
        columnNames    = pColumnNames;
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

    public String getConstraintName()
    {
        return constraintName;
    }

    public void setConstraintName( final String pConstraintName )
    {
        constraintName = pConstraintName;
    }

    public String getColumnNames()
    {
        return columnNames;
    }

    public void setColumnNames( final String pColumnNames )
    {
        columnNames = pColumnNames;
    }
}
