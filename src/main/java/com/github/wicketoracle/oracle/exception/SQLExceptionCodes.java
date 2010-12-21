package com.github.wicketoracle.oracle.exception;

import com.github.wicketoracle.exception.NotInstantiableException;

public final class SQLExceptionCodes
{

    /**
      * This is a non-instantiable utility class.
      */
    protected SQLExceptionCodes() throws NotInstantiableException
    {
        throw new NotInstantiableException();
    }

    public static final int CONNECTION_TIMED_OUT                         = 17410;
    public static final int DANGEROUS_PASSWORD                           = 20003;
    public static final int DANGEROUS_USERNAME                           = 20002;
    public static final int INCORRECT_CURRENT_PASSWORD                   = 28008;
    public static final int NON_COMPLIANT_PASSWORD                       = 28003;
    public static final int NON_REUSABLE_PASSWORD                        = 28007;
    public static final int OPTIMISTIC_LOCKING_VIOLATION                 = 20006;
    public static final int UNIQUE_CONSTRAINT_VIOLATION                  = 1;
    public static final int UCP_ILLEGAL_ABANDONED_CONNECTION_TIMEOUT     = 7;
    public static final int UCP_ILLEGAL_CONNECTION_HARVEST_MAX_COUNT     = 13;
    public static final int UCP_ILLEGAL_CONNECTION_HARVEST_TRIGGER_COUNT = 12;
    public static final int UCP_ILLEGAL_CONNECTION_WAIT_TIMEOUT          = 5;
    public static final int UCP_ILLEGAL_INITIAL_POOL_SIZE                = 6;
    public static final int UCP_ILLEGAL_MAX_CACHED_STATEMENTS            = 22;
    public static final int UCP_ILLEGAL_MAX_CONNECTION_REUSE_COUNT       = 57;
    public static final int UCP_ILLEGAL_MAX_IDLE_TIME                    = 4;
    public static final int UCP_ILLEGAL_MIN_POOL_SIZE                    = 2;
    public static final int UCP_ILLEGAL_MAX_POOL_SIZE                    = 3;
    public static final int UCP_ILLEGAL_TIMEOUT_CHECK_INTERVAL           = 8;
    public static final int USER_ALREADY_EXISTS                          = 1920;
}
