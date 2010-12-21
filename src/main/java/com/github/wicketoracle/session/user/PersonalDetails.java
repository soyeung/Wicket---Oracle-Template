package com.github.wicketoracle.session.user;

import java.io.Serializable;

import org.apache.wicket.authorization.strategies.role.Roles;

public class PersonalDetails implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public PersonalDetails()
    {

    }

    private Roles roles = new Roles();

    private String         languageCode;
    private java.util.Date passwordExpiryDate;

    public final Roles getRoles()
    {
        return roles;
    }

    public final void setRoles( final Roles pRoles )
    {
        roles = pRoles;
    }

    public final String getLanguageCode()
    {
        return languageCode;
    }

    public final void setLanguageCode( final String pLanguageCode )
    {
        languageCode = pLanguageCode;
    }

    public final java.util.Date getPasswordExpiryDate()
    {
        return passwordExpiryDate;
    }

    public final void setPasswordExpiryDate( final java.util.Date pPasswordExpiryDate )
    {
        passwordExpiryDate = pPasswordExpiryDate;
    }
}
