package com.github.wicketoracle.app.report.sessionlock;

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

    public static final String ROLE_SESSION_LOCK_REPORT = "ROLE_SESSION_LOCK_REPORT";
}
