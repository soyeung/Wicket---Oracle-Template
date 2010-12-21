package com.github.wicketoracle.app.user.delegate.mgr;

import java.util.Date;

import org.apache.wicket.IClusterable;

import com.github.wicketoracle.html.form.choice.StringSelectChoice;

import oracle.sql.TIMESTAMP;


/**
 * Represents the attributes of users presented on the user management screens
 *
 * @author Andrew Hall
 *
 */
public final class DelegateUser implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private int                 userId;
    private String              username;
    private String              profile;
    private StringSelectChoice  isEnabled;
    private StringSelectChoice  isTracingEnabled;
    private Date                dateCreated;
    private TIMESTAMP           aurUpdatedDate;
    private boolean             isModified;

    /**
     * Constructor
     */
    public DelegateUser
    (
        final int                 pUserId
    ,   final String              pUsername
    ,   final String              pProfile
    ,   final StringSelectChoice  pIsEnabled
    ,   final StringSelectChoice  pIsTracingEnabled
    ,   final Date                pDateCreated
    ,   final TIMESTAMP           pAurUpdatedDate
    ,   final boolean             pIsModified
    )
    {
        setUserId( pUserId );
        setUsername( pUsername );
        setProfile( pProfile );
        setIsEnabled( pIsEnabled );
        setIsTracingEnabled( pIsTracingEnabled );
        setDateCreated( pDateCreated );
        setAurUpdatedDate( pAurUpdatedDate );
        setModified( pIsModified );
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId( final int pAurId )
    {
        userId = pAurId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( final String pAurUsername )
    {
        username = pAurUsername;
    }

    public String getProfile()
    {
        return profile;
    }

    public void setProfile( final String pAurProfile )
    {
        profile = pAurProfile;
    }

    public StringSelectChoice getIsEnabled()
    {
        return isEnabled;
    }

    public void setIsEnabled( final StringSelectChoice pIsEnabled )
    {
        if ( pIsEnabled != null )
        {
            if ( isEnabled == null )
            {
                isEnabled = pIsEnabled;
                setModified( true );
            }
            else
            {
                if ( ! pIsEnabled.getKey().equals( isEnabled.getKey() ) )
                {
                    isEnabled = pIsEnabled;
                    setModified( true );
                }
            }
        }
    }

    public StringSelectChoice getIsTracingEnabled()
    {
        return isTracingEnabled;
    }

    public void setIsTracingEnabled( final StringSelectChoice pIsTracingEnabled )
    {
        if ( pIsTracingEnabled != null )
        {
            if ( isTracingEnabled == null )
            {
                isTracingEnabled = pIsTracingEnabled;
                setModified( true );
            }
            else
            {
                if ( ! pIsTracingEnabled.getKey().equals( isTracingEnabled.getKey() ) )
                {
                    isTracingEnabled = pIsTracingEnabled;
                    setModified( true );
                }
            }
        }
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( final Date pDateCreated )
    {
        dateCreated = pDateCreated;
    }

    public TIMESTAMP getAurUpdatedDate()
    {
        return aurUpdatedDate;
    }

    public void setAurUpdatedDate( final TIMESTAMP pAurUpdatedDate )
    {
        aurUpdatedDate = pAurUpdatedDate;
    }

    public boolean isModified()
    {
        return isModified;
    }

    public void setModified( final boolean pIsModified )
    {
        isModified = pIsModified;
    }
}
