package com.github.wicketoracle.app.report.securitymatrixbyrole;

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

    public static final String ROLE_SECURITY_MATRIX_BY_ROLE_REPORT = "ROLE_SECURITY_MATRIX_BY_ROLE";
}
