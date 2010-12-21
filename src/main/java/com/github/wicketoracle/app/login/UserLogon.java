package com.github.wicketoracle.app.login;

import org.apache.wicket.IClusterable;
import org.apache.wicket.util.crypt.ICrypt;
import org.apache.wicket.util.crypt.KeyInSessionSunJceCryptFactory;

public class UserLogon implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private ICrypt crypt;
    private String username = "";
    private String password = "";

    public UserLogon()
    {
        crypt = new KeyInSessionSunJceCryptFactory().newCrypt();
    }

    public final String getUsername()
    {
        return username;
    }

    public final void setUsername( final String pUsername )
    {
        username = pUsername;
    }

    public final String getPassword()
    {
        return crypt.decryptUrlSafe( password );
    }

    public final void setPassword( final String pPassword )
    {
        password = crypt.encryptUrlSafe( pPassword );
    }
}
