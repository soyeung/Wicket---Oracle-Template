package com.github.wicketoracle.app.report.tableswithoutpk;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestTablesWithoutPKReportPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderTablesWithoutPKReportPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( TablesWithoutPKReportPage.class );
        tester.assertRenderedPage( TablesWithoutPKReportPage.class );
        tester.assertNoLeakedConnections();
    }
}
