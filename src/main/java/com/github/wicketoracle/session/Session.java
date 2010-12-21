package com.github.wicketoracle.session;

import java.sql.SQLException;
import java.util.Locale;

import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.Request;
import org.apache.wicket.util.crypt.ICrypt;
import org.apache.wicket.util.crypt.KeyInSessionSunJceCryptFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.WicketApplication;
import com.github.wicketoracle.session.user.PersonalDetails;


/**
 * An authenticated session in the application.
 *
 * Because end-to-end authentication( app to db ) is used
 * both the username and password must be stored.
 *
 * @author Andrew Hall
 *
 */

public final class Session extends AuthenticatedWebSession
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger( Session.class );

    private static final String APPLICATION_MODE = WebApplication.get().getConfigurationType();

    private ICrypt crypt;
    private String username;
    private String password;

    private PersonalDetails personalDetails = new PersonalDetails();

   /**
     * Constructor
     *
     * @param request
     */

    public Session( final Request pRequest )
    {
        super( pRequest );
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#authenticate( String, String )
     */
    public boolean authenticate( final String pUsername , final String pPassword )
    {
        boolean isSuccessful = true;

        SessionDAO loginDAO = null;

        try
        {
            LOGGER.debug( "Request new SessionDAO -> username -> {}" , pUsername );

            loginDAO = new SessionDAO( pUsername , pPassword );

            LOGGER.debug( "Built login DAO" );

            setPersonalDetails( loginDAO.getAppUserDetails() );

            LOGGER.debug( "Retrieved and added user details to the session" );

            this.setLocale( new Locale( getPersonalDetails().getLanguageCode() ) );

            LOGGER.debug( "Set user's locale -> {}" , this.getLocale().getCountry() );

            if ( isTemporary() )
            {
                bind();
            }

            LOGGER.debug( "Added this session to the session store" );

            final String ipAddress   = ( ( WebClientInfo ) super.getClientInfo() ).getProperties().getRemoteAddress();
            final String httpSession = getId();

            loginDAO.recordLogon( ipAddress , httpSession );

            LOGGER.debug( "Recorded the details of a successful logon ; IP Address -> {} ; HTTP Session -> {}" , ipAddress , httpSession );

            crypt = new KeyInSessionSunJceCryptFactory().newCrypt();
            setUsername( pUsername );
            setPassword( pPassword );

            LOGGER.debug( "Set username and password" );

        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception in authenticate() -> {}; error code -> {}; sql state -> {};"
            ,   new Object []
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            isSuccessful = false;
        }
        finally
        {
            if ( !( loginDAO == null ) && ! loginDAO.closeConnection() )
            {
                isSuccessful = false;
            }
        }

        return isSuccessful;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
     */
    public Roles getRoles()
    {
        return personalDetails.getRoles();
    }

    /**
     * @return True if the user has signed in
     */
    public boolean isAuthenticated()
    {
        return ( getUsername() != null );
    }

    /**
     *
     * @return
     */
    public boolean isDebugInfoVisible()
    {
        return APPLICATION_MODE.equals( WicketApplication.DEVELOPMENT ) || getRoles().hasRole( RequiredRoles.ROLE_VIEW_DEBUG_INFO );
    }

    /**
     * @return Java bean containing personal details of authenticated user
     */
    public PersonalDetails getPersonalDetails()
    {
        return personalDetails;
    }

    /**
     *
     * @param pPersonalDetails
     *                        personal details of authenticated user
     */
    public void setPersonalDetails( final PersonalDetails pPersonalDetails )
    {
        personalDetails = pPersonalDetails;
    }

    /**
     * @return The username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param pUsername
     *                  The username
     */
    public void setUsername( final String pUsername )
    {
        username = pUsername;
    }

    /**
     *
     * @return the password in encrypted form
     */
    public String getEncryptedPassword()
    {
        return password;
    }

    /**
     * @return The password
     */
    public String getPassword()
    {
        return crypt.decryptUrlSafe( password );
    }

    /**
     * @param pPassword
     *                  The password
     */
    public void setPassword( final String pPassword )
    {
        password = crypt.encryptUrlSafe( pPassword );
    }
}
