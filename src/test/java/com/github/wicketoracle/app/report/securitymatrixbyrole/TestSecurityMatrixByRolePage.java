package com.github.wicketoracle.app.report.securitymatrixbyrole;

import org.apache.wicket.util.tester.FormTester;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestSecurityMatrixByRolePage extends TestCase
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
        tester.startPage( SecurityMatrixByRoleReportPage.class );
        tester.assertRenderedPage( SecurityMatrixByRoleReportPage.class );
        tester.assertNoLeakedConnections();
    }

    public final void testSearchForm()
    {
        tester.doUnitTestLogin();
        tester.startPage( SecurityMatrixByRoleReportPage.class );
        FormTester formTester = tester.newFormTester( "searchForm" );
        formTester.submit();
        tester.assertRenderedPage( SecurityMatrixByRoleReportPage.class );
        tester.assertNoLeakedConnections();
    }
}
