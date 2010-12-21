package com.github.wicketoracle;

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsRequestCycleProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;

import com.github.wicketoracle.app.login.LoginPage;
import com.github.wicketoracle.session.Session;


/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 *
 * @see uk.co.totemic.Start#main(String[])
 */
public class WicketApplication extends AuthenticatedWebApplication
{
    public static final int HTTP_PORT  = 8082;
    public static final int HTTPS_PORT = 443;

    /**
     * Constructor
     */
    public WicketApplication()
    {

    }

    @Override
    protected final IRequestCycleProcessor newRequestCycleProcessor()
    {
        return new HttpsRequestCycleProcessor( new HttpsConfig( WicketApplication.HTTP_PORT , WicketApplication.HTTPS_PORT ) );
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    public final Class<LoginPage> getHomePage()
    {
        return LoginPage.class;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebApplication#getSignInPageClass()
     * @return The application sign-in page.
     */
    @Override
    protected final Class<LoginPage> getSignInPageClass()
    {
        return LoginPage.class;
    }

    /**
     * @see org.apache.wicket.authentication.AuthenticatedWebApplication#getWebSessionClass()
     * @return An authorised principal.
     */
    @Override
    protected final Class<Session> getWebSessionClass()
    {
        return Session.class;
    }
}
