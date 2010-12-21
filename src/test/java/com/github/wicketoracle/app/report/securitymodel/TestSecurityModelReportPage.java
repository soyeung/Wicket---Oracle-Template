package com.github.wicketoracle.app.report.securitymodel;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestSecurityModelReportPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderSecurityModelPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( SecurityModelReportPage.class );
        tester.assertRenderedPage( SecurityModelReportPage.class );
    }
}
