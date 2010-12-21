package com.github.wicketoracle.app.createuser;

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

    public static final String ROLE_CREATE_USER = "ROLE_STANDARD_APP_USER_CREATOR";
}
