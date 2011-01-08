package com.github.wicketoracle.app.report.logonhistory;

import org.apache.wicket.util.tester.FormTester;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestLogonHistoryReportPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderAuthenticationReportPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( LogonHistoryReportPage.class );
        tester.assertRenderedPage( LogonHistoryReportPage.class );
        tester.assertNoLeakedConnections();
    }

    public final void testSearchForm()
    {
        tester.doUnitTestLogin();
        tester.startPage( LogonHistoryReportPage.class );
        FormTester formTester = tester.newFormTester( "searchForm" );
        formTester.submit();
        tester.assertRenderedPage( LogonHistoryReportPage.class );
        tester.assertNoLeakedConnections();
    }
}
