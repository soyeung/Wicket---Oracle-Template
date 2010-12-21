package com.github.wicketoracle.app.user.standard.mgr;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestStandardUserPasswordMgrPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderStandardUserPasswordMgrPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( new StandardUserPasswordMgrPage( new StandardUser( 4 , "UNIT_TEST" , null , null , null , null , null , null , false ) ) );
        tester.assertRenderedPage( StandardUserPasswordMgrPage.class );
    }
}
