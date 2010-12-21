package com.github.wicketoracle.app.report.unindexedforeignkey;

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

    public static final String ROLE_UNINDEXED_FK_REPORT = "ROLE_UNINDEXED_FK_REPORT";
}
