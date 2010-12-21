package com.github.wicketoracle.app.report;

import org.apache.wicket.IClusterable;

public final class Report implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private String reportName;
    private String dbrlName;
    private String target;

    /**
     *
     * @param pReportName
     *                    name of the report
     * @param pDbrlName
     *                    name of the role required to gain access to the report
     */
    public Report( final String pReportName, final String pDbrlName, final String pTarget )
    {
        setReportName( pReportName );
        setDbrlName( pDbrlName );
        setTarget( pTarget );
    }

    public String getReportName()
    {
        return reportName;
    }

    public void setReportName( final String pReportName )
    {
        reportName = pReportName;
    }

    public String getDbrlName()
    {
        return dbrlName;
    }

    public void setDbrlName( final String pDbrlName )
    {
        dbrlName = pDbrlName;
    }

    public String toString()
    {
        return getReportName();
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget( final String pTarget )
    {
        target = pTarget;
    }
}
