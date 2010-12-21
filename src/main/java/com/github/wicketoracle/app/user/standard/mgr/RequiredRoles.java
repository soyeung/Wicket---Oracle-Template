package com.github.wicketoracle.app.user.standard.mgr;

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

    public static final String ROLE_STANDARD_APP_USER_MGR = "ROLE_STANDARD_APP_USER_MGR";
    public static final String ROLE_STD_APP_USER_ROLE_MGR = "ROLE_STD_APP_USER_ROLE_MGR";
    public static final String ROLE_STD_USER_PASSWORD_MGR = "ROLE_STD_USER_PASSWORD_MGR";
}
