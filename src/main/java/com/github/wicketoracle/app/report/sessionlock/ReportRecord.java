package com.github.wicketoracle.app.report.sessionlock;

import org.apache.wicket.IClusterable;

final class ReportRecord implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private int    sessionId;
    private String username;
    private String lockType;
    private String modeHeld;
    private String modeRequested;
    private String lockId1;
    private String lockId2;
    private String currSql;
    private String prevSql;

    public ReportRecord
    (
        final int    pSessionId
    ,   final String pUsername
    ,   final String pLockType
    ,   final String pModeHeld
    ,   final String pModeRequested
    ,   final String pLockId1
    ,   final String pLockId2
    ,   final String pCurrSql
    ,   final String pPrevSql
    )
    {
        sessionId             = pSessionId;
        username              = pUsername;
        lockType              = pLockType;
        modeHeld              = pModeHeld;
        modeRequested         = pModeRequested;
        lockId1               = pLockId1;
        lockId2               = pLockId2;
        currSql               = pCurrSql;
        prevSql               = pPrevSql;
    }

    public int getSessionId()
    {
        return sessionId;
    }

    public void setSessionId( final int pSessionId )
    {
        sessionId = pSessionId;
    }

    public String getLockType()
    {
        return lockType;
    }

    public void setLockType( final String pLockType )
    {
        lockType = pLockType;
    }

    public String getModeHeld()
    {
        return modeHeld;
    }

    public void setModeHeld( final String pModeHeld )
    {
        modeHeld = pModeHeld;
    }

    public String getModeRequested()
    {
        return modeRequested;
    }

    public void setModeRequested( final String pModeRequested )
    {
        modeRequested = pModeRequested;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( final String pUsername )
    {
        username = pUsername;
    }

    public String getLockId1()
    {
        return lockId1;
    }

    public void setLockId1( final String pLockId1 )
    {
        lockId1 = pLockId1;
    }

    public String getLockId2()
    {
        return lockId2;
    }

    public void setLockId2( final String pLockId2 )
    {
        lockId2 = pLockId2;
    }

    public String getCurrSql()
    {
        return currSql;
    }

    public void setCurrSql( final String pCurrSql )
    {
        currSql = pCurrSql;
    }

    public String getPrevSql()
    {
        return prevSql;
    }

    public void setPrevSql( final String pPrevSql )
    {
        prevSql = pPrevSql;
    }
}
