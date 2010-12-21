package com.github.wicketoracle.app.data;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;


public class TestDataMgrPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderRefDataMgrPage()
    {
        tester.doUnitTestLogin();
        tester.clickLink( "PanelMenu:LinkManageData" );
        tester.assertRenderedPage( DataMgrPage.class );
    }
}
