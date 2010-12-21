package com.github.wicketoracle.app.changepassword;

import org.apache.wicket.IClusterable;
import org.apache.wicket.util.crypt.ICrypt;
import org.apache.wicket.util.crypt.KeyInSessionSunJceCryptFactory;

class UserPassword implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private transient ICrypt crypt = new KeyInSessionSunJceCryptFactory().newCrypt();

    private String currentPassword      = "";
    private String newPassword          = "";
    private String confirmedNewpassword = "";

    /**
     * Constructor
     */
    public UserPassword()
    {

    }

    public String getCurrentPassword()
    {
        return crypt.decryptUrlSafe( currentPassword );
    }

    public void setCurrentPassword( final String pCurrentPassword )
    {
        currentPassword = crypt.encryptUrlSafe( pCurrentPassword );
    }

    public String getNewPassword()
    {
        return crypt.decryptUrlSafe( newPassword );
    }

    public void setNewPassword( final String pNewPassword )
    {
        newPassword = crypt.encryptUrlSafe( pNewPassword );
    }

    public String getConfirmedNewPassword()
    {
        return crypt.decryptUrlSafe( confirmedNewpassword );
    }

    public void setConfirmedNewPassword( final String pConfirmedNewPassword )
    {
        confirmedNewpassword = crypt.encryptUrlSafe( pConfirmedNewPassword );
    }
}
