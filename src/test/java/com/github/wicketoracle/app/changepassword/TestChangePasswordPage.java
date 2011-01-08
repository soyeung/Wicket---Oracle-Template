package com.github.wicketoracle.app.changepassword;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestChangePasswordPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderChangePasswordPage()
    {
        tester.doUnitTestLogin();
        tester.clickLink( "PanelMenu:LinkChangePassword" );
        tester.assertRenderedPage( ChangePasswordPage.class );
        tester.assertNoLeakedConnections();
    }
}
