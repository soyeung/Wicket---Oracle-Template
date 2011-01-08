package com.github.wicketoracle.app.report.userprivileges;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestUserPrivilegesReportPage extends TestCase
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
        tester.startPage( UserPrivilegesReportPage.class );
        tester.assertRenderedPage( UserPrivilegesReportPage.class );
        tester.assertNoLeakedConnections();
    }
}
