package com.github.wicketoracle.app.report.securitymatrixbyuser;

import org.apache.wicket.util.tester.FormTester;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestSecurityMatrixByUserPage extends TestCase
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
        tester.startPage( SecurityMatrixByUserReportPage.class );
        tester.assertRenderedPage( SecurityMatrixByUserReportPage.class );
        tester.assertNoLeakedConnections();
    }

    public final void testSearchForm()
    {
        tester.doUnitTestLogin();
        tester.startPage( SecurityMatrixByUserReportPage.class );
        FormTester formTester = tester.newFormTester( "searchForm" );
        formTester.submit();
        tester.assertRenderedPage( SecurityMatrixByUserReportPage.class );
        tester.assertNoLeakedConnections();
    }
}
