package com.github.wicketoracle.app.user.standard.mgr;

import org.apache.wicket.util.tester.FormTester;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestStandardUserMgrPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderStandardUserMgrPage()
    {
        tester.doUnitTestLogin();
        tester.clickLink( "PanelMenu:LinkManageStandardUsers" );
        tester.assertRenderedPage( StandardUserMgrPage.class );
        tester.assertNoLeakedConnections();
    }

    public final void testSearchForm()
    {
        tester.doUnitTestLogin();
        tester.startPage( StandardUserMgrPage.class );

        FormTester formTester = tester.newFormTester( "searchUserForm" );
        formTester.submit();
        tester.assertRenderedPage( StandardUserMgrPage.class );
        tester.assertNoLeakedConnections();
    }

    public final void testManagementForm()
    {
        testSearchForm();
        FormTester formTester = tester.newFormTester( "mgrForm" );
        formTester.submit();
        tester.assertRenderedPage( StandardUserMgrPage.class );
        tester.assertNoLeakedConnections();
    }
}
