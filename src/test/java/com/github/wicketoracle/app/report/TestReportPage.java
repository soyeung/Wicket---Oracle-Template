package com.github.wicketoracle.app.report;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestReportPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderReportPage()
    {
        tester.doUnitTestLogin();
        tester.clickLink( "PanelMenu:LinkReports" );
        tester.assertRenderedPage( ReportPage.class );
        tester.assertNoLeakedConnections();
    }
}
