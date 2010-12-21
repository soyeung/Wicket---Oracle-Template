package com.github.wicketoracle.app.report.leastprivileges;

import com.github.wicketoracle.exception.NotInstantiableException;

final class RequiredRoles
{
    /**
      * This is a non-instantiable utility class.
      */
    protected RequiredRoles() throws NotInstantiableException
    {
        throw new NotInstantiableException();
    }

    public static final String ROLE_LEAST_PRIVILEGES_REPORT = "ROLE_LEAST_PRIVILEGES_REPORT";
}
