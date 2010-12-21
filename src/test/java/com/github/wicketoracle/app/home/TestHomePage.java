package com.github.wicketoracle.app.home;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestHomePage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderHomePage()
    {
        tester.doUnitTestLogin();
        tester.assertRenderedPage( HomePage.class );
    }
}
