package com.github.wicketoracle.app.user.standard.mgr;

import org.apache.wicket.IClusterable;
import org.apache.wicket.util.crypt.ICrypt;
import org.apache.wicket.util.crypt.KeyInSessionSunJceCryptFactory;

public class StandardUserPassword implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private transient ICrypt crypt = new KeyInSessionSunJceCryptFactory().newCrypt();

    private String password             = "";
    private String passwordConfirmation = "";

    public StandardUserPassword()
    {

    }

    public final String getPassword()
    {
        return crypt.decryptUrlSafe( password );
    }

    public final void setPassword( final String pPassword )
    {
        password = crypt.encryptUrlSafe( pPassword );
    }

    public final String getPasswordConfirmation()
    {
        return crypt.decryptUrlSafe( passwordConfirmation );
    }

    public final void setPasswordConfirmation( final String pPasswordConfirmation )
    {
        passwordConfirmation = crypt.encryptUrlSafe( pPasswordConfirmation );
    }
}
