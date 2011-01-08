package com.github.wicketoracle.app.user.delegate.mgr;

import org.apache.wicket.util.tester.FormTester;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;

public class TestDelegateUserRoleMgrPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderDelegateUserRoleMgrPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( new DelegateUserRoleMgrPage( new DelegateUser( 2 , "UNIT_TEST" , null , null , null , null , null , false ) ) );
        tester.assertRenderedPage( DelegateUserRoleMgrPage.class );
        tester.assertNoLeakedConnections();
    }

    public final void testSubmitForm()
    {
        testRenderDelegateUserRoleMgrPage();
        FormTester formTester = tester.newFormTester( "FormRoleMgr" );
        formTester.submit();
        tester.assertRenderedPage( DelegateUserRoleMgrPage.class );
        tester.assertNoLeakedConnections();
    }
}
