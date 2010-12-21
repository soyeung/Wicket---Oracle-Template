package com.github.wicketoracle.app.ucp;

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

    public static final String ROLE_UCP_MGR = "ROLE_UCP_MGR";
}
