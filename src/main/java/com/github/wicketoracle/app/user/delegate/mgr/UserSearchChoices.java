package com.github.wicketoracle.app.user.delegate.mgr;

import org.apache.wicket.IClusterable;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.StringSelectChoice;


/**
 * Capture the search criteria specified by a user, in their efforts to find
 * and manage standard users
 *
 * @author Andrew Hall
 *
 */
final class UserSearchChoices implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private String              username          = "";
    private StringSelectChoice  isEnabled         = new StringSelectChoice( "" );
    private StringSelectChoice  isTracingEnabled  = new StringSelectChoice( "" );
    private IntegerSelectChoice dbRole            = new IntegerSelectChoice( 0 );
    private IntegerSelectChoice userLocale        = new IntegerSelectChoice( 0 );

    /**
     *
     */
    public UserSearchChoices( )
    {

    }

    /**
     *
     * @return
     */
    public String getUsername()
    {
        String temp;
        if ( username == null )
        {
            temp = "";
        }
        else
        {
            temp = username;
        }

        return temp;
    }

    /**
     *
     * @param pUsername
     */
    public void setUsername( final String pUsername )
    {
        username = pUsername;
    }

    /**
     *
     * @return
     */
    public StringSelectChoice getIsEnabled()
    {
        StringSelectChoice temp;
        if ( isEnabled == null )
        {
            temp = new StringSelectChoice( "" );
        }
        else
        {
            temp = isEnabled;
        }

        return temp;
    }

    /**
     *
     * @param pIsEnabled
     */
    public void setIsEnabled( final StringSelectChoice pIsEnabled )
    {
        isEnabled = pIsEnabled;
    }

    /**
     *
     * @return
     */
    public StringSelectChoice getIsTracingEnabled()
    {
        StringSelectChoice temp;
        if ( isTracingEnabled == null )
        {
            temp = new StringSelectChoice( "" );
        }
        else
        {
            temp = isTracingEnabled;
        }

        return temp;
    }

    /**
     *
     * @param pIsTracingEnabled
     */
    public void setIsTracingEnabled( final StringSelectChoice pIsTracingEnabled )
    {
        isTracingEnabled = pIsTracingEnabled;
    }

    /**
     *
     * @return
     */
    public IntegerSelectChoice getDbRole()
    {
        IntegerSelectChoice temp;
        if ( dbRole == null )
        {
            temp = new IntegerSelectChoice( 0 );
        }
        else
        {
            temp = dbRole;
        }

        return temp;
    }

    /**
     *
     * @param pDbRole
     */
    public void setDbRole( final IntegerSelectChoice pDbRole )
    {
        dbRole = pDbRole;
    }

    /**
     *
     * @return
     */
    public IntegerSelectChoice getUserLocale()
    {
        IntegerSelectChoice temp;
        if ( userLocale == null )
        {
            temp = new IntegerSelectChoice( 0 );
        }
        else
        {
            temp = userLocale;
        }

        return temp;
    }

    /**
     *
     * @param pUserLocale
     */
    public void setUserLocale( final IntegerSelectChoice pUserLocale )
    {
        userLocale = pUserLocale;
    }
}
