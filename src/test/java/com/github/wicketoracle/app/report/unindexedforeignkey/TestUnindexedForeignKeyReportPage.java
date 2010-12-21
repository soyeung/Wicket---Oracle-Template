package com.github.wicketoracle.app.report.unindexedforeignkey;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestUnindexedForeignKeyReportPage extends TestCase
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
        tester.startPage( UnindexedForeignKeyReportPage.class );
        tester.assertRenderedPage( UnindexedForeignKeyReportPage.class );
    }
}
