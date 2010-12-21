package com.github.wicketoracle.exception;

/**
 * This is thrown when an attempt is made to invoke the.
 * constructor of a class that is not intended to be instantiated - such as
 * a utility - helper class which contains only static methods.
 *
 * @author Andrew Hall
 *
 */
public final class NotInstantiableException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public NotInstantiableException()
    {
        super();
    }
}
