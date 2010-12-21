package com.github.wicketoracle.app.user.delegate.mgr;

import org.apache.wicket.util.tester.FormTester;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestDelegateUserMgrPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderDelegateUserMgrPage()
    {
        tester.doUnitTestLogin();
        tester.clickLink( "PanelMenu:LinkManageDelegateUsers" );
        tester.assertRenderedPage( DelegateUserMgrPage.class );
    }

    public final void testSearchForm()
    {
        tester.doUnitTestLogin();
        tester.startPage( DelegateUserMgrPage.class );

        FormTester formTester = tester.newFormTester( "searchUserForm" );
        formTester.submit();
        tester.assertRenderedPage( DelegateUserMgrPage.class );
    }

    public final void testManagementForm()
    {
        testSearchForm();
        FormTester formTester = tester.newFormTester( "mgrForm" );
        formTester.submit();
        tester.assertRenderedPage( DelegateUserMgrPage.class );
    }
}
