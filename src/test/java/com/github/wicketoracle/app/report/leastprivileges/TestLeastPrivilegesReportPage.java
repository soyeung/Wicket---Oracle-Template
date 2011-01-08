package com.github.wicketoracle.app.report.leastprivileges;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestLeastPrivilegesReportPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderLeastPrivilegesReportPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( LeastPrivilegesReportPage.class );
        tester.assertRenderedPage( LeastPrivilegesReportPage.class );
        tester.assertNoLeakedConnections();
    }
}
