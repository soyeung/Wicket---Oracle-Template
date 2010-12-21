package com.github.wicketoracle.app.report.usage;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestUsageDataReport extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderCurrentUserActivityReportPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( UsageReportPage.class );
        tester.assertRenderedPage( UsageReportPage.class );
    }
}
