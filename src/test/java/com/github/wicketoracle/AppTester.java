package com.github.wicketoracle;


import java.sql.SQLException;

import oracle.ucp.UniversalConnectionPoolException;

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import com.github.wicketoracle.app.login.LoginPage;
import com.github.wicketoracle.oracle.ucp.UCPMgr;
import com.github.wicketoracle.session.Session;

public class AppTester extends WicketTester
{
    private static boolean isUCPSetup = false;

    private static final int    ABANDONED_CONNECTION_TIMEOUT  = Integer.parseInt( System.getProperty( "unit.test.ucp.abandoned.connection.timeout" ) );
    private static final String CONNECTION_FACTORY_CLASS_NAME = System.getProperty( "unit.test.connection.pool.class" );
    private static final String CONNECTION_POOL_NAME          = System.getProperty( "unit.test.connection.pool.name" );
    private static final String CONNECTION_POOL_URL           = System.getProperty( "unit.test.connection.pool.url" );
    private static final int    INITIAL_POOL_SIZE             = Integer.parseInt( System.getProperty( "unit.test.ucp.initial.pool.size" ) );
    private static final int    MAX_CACHED_STATEMENTS         = Integer.parseInt( System.getProperty( "unit.test.ucp.max.cached.statements" ) );
    private static final String UNIT_TEST_PASSWORD            = System.getProperty( "unit.test.password" );
    private static final String UNIT_TEST_USER                = System.getProperty( "unit.test.user" );

    /**
     *
     */
    public AppTester()
    {
        super
        (
            /*application*/
            new AuthenticatedWebApplication()
            {
                @Override
                protected final IRequestCycleProcessor newRequestCycleProcessor()
                {
                    return new WebRequestCycleProcessor();
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
        );

        setupUCP();
    }

    /**
     *
     */
    private void setupUCP()
    {
        if ( ! AppTester.isUCPSetup )
        {
            System.out.println( "Connection Factory Class Name -> " + CONNECTION_FACTORY_CLASS_NAME );
            System.out.println( "Connection Pool Name -> "          + CONNECTION_POOL_NAME );
            System.out.println( "Connection Pool URL -> "           + CONNECTION_POOL_URL );
            System.out.println( "Initial Pool Size -> "             + INITIAL_POOL_SIZE );
            System.out.println( "Max Cached Statements -> "         + MAX_CACHED_STATEMENTS );

            try
            {
                UCPMgr.setupUCP
                (
                    ABANDONED_CONNECTION_TIMEOUT
                ,   CONNECTION_FACTORY_CLASS_NAME
                ,   CONNECTION_POOL_NAME
                ,   CONNECTION_POOL_URL
                ,   INITIAL_POOL_SIZE
                ,   MAX_CACHED_STATEMENTS
                );

                AppTester.isUCPSetup = true;

                System.out.println( "Setup of UCP successful" );
            }
            catch ( UniversalConnectionPoolException ucpe )
            {
                System.err.println( "Exception when configuring Universal Connection Pool -> " + ucpe.getClass().toString() + " -> " + ucpe.getLocalizedMessage() );
            }
            catch ( SQLException sqle )
            {
                System.err.println( "Exception when configuring Universal Connection Pool -> " + sqle.getClass().toString() + " -> " + sqle.getLocalizedMessage() );
            }
        }
    }

    /**
     * Authenticate using the specified credentials
     *
     * @param pUsername
     * @param pPassword
     */
    public final void doLogin( final String pUsername , final String pPassword )
    {
        startPage( LoginPage.class );
        FormTester formTester = newFormTester( "loginForm" );

        formTester.setValue( "username" , pUsername );
        formTester.setValue( "password" , pPassword );
        formTester.submit();
    }

    /**
     * Authenticate using the user created for unit testing
     */
    public final void doUnitTestLogin()
    {
        if ( this.getWicketSession() != null )
        {
            doLogin( UNIT_TEST_USER , UNIT_TEST_PASSWORD );
        }
    }
}
