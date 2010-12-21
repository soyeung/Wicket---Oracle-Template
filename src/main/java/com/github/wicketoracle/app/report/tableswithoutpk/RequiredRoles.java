package com.github.wicketoracle.app.report.tableswithoutpk;

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

    public static final String ROLE_TABLES_WITHOUT_PK_REPORT = "ROLE_TABLES_WITHOUT_PK_REPORT";
}
