package com.github.wicketoracle.app.report.leastprivileges;

import org.apache.wicket.IClusterable;

final class ReportRecord implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private String privilegeHolder;
    private String objectOwner;
    private String objectName;
    private String grantedPrivilege;

    public ReportRecord( final String pPrivilegeHolder , final String pObjectOwner , final String pObjectName , final String pGrantedPrivilege )
    {
        setPrivilegeHolder( pPrivilegeHolder );
        setObjectOwner( pObjectOwner );
        setObjectName( pObjectName );
        setGrantedPrivilege( pGrantedPrivilege );
    }

    public String getPrivilegeHolder()
    {
        return privilegeHolder;
    }

    public void setPrivilegeHolder( final String pPrivilegeHolder )
    {
        privilegeHolder = pPrivilegeHolder;
    }

    public String getObjectOwner()
    {
        return objectOwner;
    }

    public void setObjectOwner( final String pObjectName )
    {
        objectName = pObjectName;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName( final String pObjectName )
    {
        objectName = pObjectName;
    }

    public String getGrantedPrivilege()
    {
        return grantedPrivilege;
    }

    public void setGrantedPrivilege( final String pGrantedPrivilege )
    {
        grantedPrivilege = pGrantedPrivilege;
    }
}
