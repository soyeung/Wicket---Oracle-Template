package com.github.wicketoracle.app.data.list;

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

    public static final String ROLE_REF_DATA_MGR       = "ROLE_REF_DATA_MGR";
    public static final String ROLE_CONFIGURE_REF_DATA = "ROLE_CONFIGURE_REF_DATA";
}
