package com.github.wicketoracle.app.report.logonhistory;

import java.io.Serializable;
import java.util.Date;

final class ReportRecord implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String username;
    private Date   logonDate;
    private String ipAddress;
    private String httpSession;

    /**
     * Constructor
     */
    public ReportRecord( final String pUsername , final Date pLogonDate , final String pIpAddress , final String pHTTPSession )
    {
        super();
        username    = pUsername;
        logonDate   = pLogonDate;
        ipAddress   = pIpAddress;
        httpSession = pHTTPSession;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( final String pUsername )
    {
        username = pUsername;
    }

    public Date getLogonDate()
    {
        return logonDate;
    }

    public void setLogonDate( final Date pLogonDate )
    {
        logonDate = pLogonDate;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress( final String pIpAddress )
    {
        ipAddress = pIpAddress;
    }

    public String getHttpSession()
    {
        return httpSession;
    }

    public void setHttpSession( final String pHttpSession )
    {
        httpSession = pHttpSession;
    }
}
