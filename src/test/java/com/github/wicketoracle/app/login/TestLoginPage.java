package com.github.wicketoracle.app.login;

import com.github.wicketoracle.AppTester;
import com.github.wicketoracle.app.home.HomePage;

import junit.framework.TestCase;

/**
 * Simple test using the WicketTester
 */
public class TestLoginPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderLoginPage()
    {
        tester.startPage( LoginPage.class );
        tester.assertRenderedPage( LoginPage.class );
    }

    public final void testSuccessfulLogin()
    {
        tester.doUnitTestLogin();
        tester.assertRenderedPage( HomePage.class );
    }

    public final void testUnsuccessfulLogin()
    {
        tester.doLogin( "NON_EXISTENT_USER" , "xxx" );
        tester.assertRenderedPage( LoginPage.class );
    }
}
