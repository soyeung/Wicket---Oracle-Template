package com.github.wicketoracle.app.user.standard.mgr;

import junit.framework.TestCase;

import org.apache.wicket.util.tester.FormTester;

import com.github.wicketoracle.AppTester;

public class TestStandardUserRoleMgrPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderStandardUserPasswordMgrPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( new StandardUserRoleMgrPage( new StandardUser( 4 , "UNIT_TEST" , null , null , null , null , null , null , false ) ) );
        tester.assertRenderedPage( StandardUserRoleMgrPage.class );
    }

    public final void testSubmitForm()
    {
        testRenderStandardUserPasswordMgrPage();
        FormTester formTester = tester.newFormTester( "FormRoleMgr" );
        formTester.submit();
        tester.assertRenderedPage( StandardUserRoleMgrPage.class );
    }
}
