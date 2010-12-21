package com.github.wicketoracle.app.user.delegate.mgr;

import com.github.wicketoracle.exception.NotInstantiableException;

public final class RequiredRoles
{
    /**
      * This is a non-instantiable utility class.
      */
    protected RequiredRoles() throws NotInstantiableException
    {
        throw new NotInstantiableException();
    }

    public static final String ROLE_DELEGATE_APP_USER_MGR = "ROLE_DELEGATE_APP_USER_MGR";
    public static final String ROLE_DGT_APP_USER_ROLE_MGR = "ROLE_DGT_APP_USER_ROLE_MGR";
}
