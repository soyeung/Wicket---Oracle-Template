package com.github.wicketoracle.app.user.delegate.mgr;

import org.apache.wicket.IClusterable;

public class DelegateUserRole implements IClusterable
{
    private static final long serialVersionUID = 1L;

    /** role id */
    private int dbRole;

    /** long role description */
    private String roleDescr;

    /** role category name */
    private String roleCategory;

    /** does the user have this role assigned to them? */
    private boolean isAssigned;

    /** have any modifications been made to this bean? */
    private boolean isModified;

    /**
     * Constructor
     */
    public DelegateUserRole
    (
        final int     pDbRole
    ,   final String  pRoleDescr
    ,   final boolean pIsAssigned
    ,   final String  pRoleCategory
    ,   final boolean pIsModified
    )
    {
        setDbRole( pDbRole );
        setRoleDescr( pRoleDescr );
        setAssigned( pIsAssigned );
        setRoleCategory( pRoleCategory );
        setModified( pIsModified );
    }

    public final int getDbRole()
    {
        return dbRole;
    }

    public final void setDbRole( final int pDbRole )
    {
        dbRole = pDbRole;
    }

    public final String getRoleCategory()
    {
        return roleCategory;
    }

    public final void setRoleCategory( final String pRoleCategory )
    {
        roleCategory = pRoleCategory;
    }

    public final String getRoleDescr()
    {
        return roleDescr;
    }

    public final void setRoleDescr( final String pRoleDescr )
    {
        roleDescr = pRoleDescr;
    }

    public final boolean getAssigned()
    {
        return isAssigned;
    }

    public final void setAssigned( final boolean pIsAssigned )
    {
        if ( isAssigned != pIsAssigned )
        {
            isAssigned = pIsAssigned;
            setModified( true );
        }
    }

    public final boolean isModified()
    {
        return isModified;
    }

    public final void setModified( final boolean pIsModified )
    {
        isModified = pIsModified;
    }
}
