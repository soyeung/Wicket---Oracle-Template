package com.github.wicketoracle.app.report;

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

    public static final String ROLE_REPORT_USER = "ROLE_REPORT_USER";
}
