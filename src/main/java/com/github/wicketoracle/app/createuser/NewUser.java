package com.github.wicketoracle.app.createuser;

import org.apache.wicket.IClusterable;
import org.apache.wicket.util.crypt.ICrypt;
import org.apache.wicket.util.crypt.KeyInSessionSunJceCryptFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;


class NewUser implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private transient ICrypt crypt = new KeyInSessionSunJceCryptFactory().newCrypt();

    private String              username;
    private String              password              = "";
    private String              passwordConfirmation  = "";
    private IntegerSelectChoice utyId                 = new IntegerSelectChoice( 0 );
    private IntegerSelectChoice aurpId                = new IntegerSelectChoice( 0 );
    private IntegerSelectChoice lngId                 = new IntegerSelectChoice( 0 );

    /**
     * Constructor
     */
    public NewUser()
    {

    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( final String pUsername )
    {
        username = pUsername;
    }

    public String getPassword()
    {
        return crypt.decryptUrlSafe( password );
    }

    public void setPassword( final String pPassword )
    {
        password = crypt.encryptUrlSafe( pPassword );
    }

    public IntegerSelectChoice getUtyId()
    {
        return utyId;
    }

    public void setUtyId( final IntegerSelectChoice pUtyId )
    {
        utyId = pUtyId;
    }

    public String getPasswordConfirmation()
    {
        return crypt.decryptUrlSafe( passwordConfirmation );
    }

    public void setPasswordConfirmation( final String pPasswordConfirmation )
    {
        passwordConfirmation = crypt.encryptUrlSafe( pPasswordConfirmation );
    }

    public IntegerSelectChoice getAurpId()
    {
        return aurpId;
    }

    public void setAurpId( final IntegerSelectChoice pAurpId )
    {
        aurpId = pAurpId;
    }

    public IntegerSelectChoice getLngId()
    {
        return lngId;
    }

    public void setLngId( final IntegerSelectChoice pLngId )
    {
        lngId = pLngId;
    }
}
