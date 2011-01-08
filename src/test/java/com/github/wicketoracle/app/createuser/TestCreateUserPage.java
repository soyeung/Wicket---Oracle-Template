package com.github.wicketoracle.app.createuser;

import org.apache.wicket.util.tester.FormTester;

import com.github.wicketoracle.AppTester;

import junit.framework.TestCase;


public class TestCreateUserPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderCreateUserPage()
    {
        tester.doUnitTestLogin();
        tester.clickLink( "PanelMenu:LinkCreateUser" );
        tester.assertRenderedPage( CreateUserPage.class );
        tester.assertNoLeakedConnections();
    }

    private void testCreateUser
    (
        final String pUsername
    ,   final String pPassword
    ,   final String pConfirmationPassword
    )
    {
        testRenderCreateUserPage();
        FormTester formTester = tester.newFormTester( "createUserForm" );
        formTester.setValue( "username"             , pUsername );
        formTester.setValue( "password"             , pPassword );
        formTester.setValue( "passwordConfirmation" , pConfirmationPassword );
        formTester.select( "userType"    , 0 );
        formTester.select( "userProfile" , 0 );
        formTester.select( "userLocale"  , 0 );
        formTester.submit();
        tester.assertRenderedPage( CreateUserPage.class );
        tester.assertNoLeakedConnections();
    }

    public final void testNonMatchingPasswords()
    {
        testCreateUser( "UNIT_TEST_USER1" , "sup3r!2" , "sup3r!3" );
    }

    public final void testNonCompliantPassword()
    {
        testCreateUser( "UNIT_TEST_USER" , "pisspoorpassword" , "pisspoorpassword" );
    }
}
