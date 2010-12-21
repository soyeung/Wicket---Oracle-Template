package com.github.wicketoracle.app.report.logonhistory;

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

    public static final String ROLE_AUTHENTICATION_REPORT = "ROLE_RPT_AUTHENTICATE";
}
