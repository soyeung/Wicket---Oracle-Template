package com.github.wicketoracle.app.report.useractivity;

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

    public static final String ROLE_CURRENT_USER_ACTIVITY_REPORT = "ROLE_CURR_USER_ACTIVITY_REPORT";
}
