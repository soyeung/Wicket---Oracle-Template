package com.github.wicketoracle.app.ucp;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestUCPConfigPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderUCPConfigPage()
    {
        tester.doUnitTestLogin();
        tester.clickLink( "PanelMenu:LinkManageUCP" );
        tester.assertRenderedPage( UCPConfigPage.class );
    }
}
