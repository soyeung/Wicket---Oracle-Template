package com.github.wicketoracle.app.report.sessionlock;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestSessionLockReportPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderSecurityMatrixByRolePage()
    {
        tester.doUnitTestLogin();
        tester.startPage( SessionLockReportPage.class );
        tester.assertRenderedPage( SessionLockReportPage.class );
        tester.assertNoLeakedConnections();
    }
}
