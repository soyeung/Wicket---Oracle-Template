package com.github.wicketoracle.app.report.useractivity;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestCurrentUserActivityReportPage extends TestCase
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
        tester.startPage( UserActivityReportPage.class );
        tester.assertRenderedPage( UserActivityReportPage.class );
    }
}
