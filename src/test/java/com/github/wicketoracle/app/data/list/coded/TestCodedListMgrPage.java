package com.github.wicketoracle.app.data.list.coded;

import com.github.wicketoracle.AppTester;
import com.github.wicketoracle.app.data.DataStructure;

import junit.framework.TestCase;


public class TestCodedListMgrPage extends TestCase
{
    private AppTester tester;

    @Override
    public final void setUp()
    {
        tester = new AppTester();
    }

    public final void testRenderCodedListMgrPage()
    {
        tester.doUnitTestLogin();
        tester.startPage( new CodedListMgrPage( new DataStructure( 1 , "" , "" , true , "X" , true ) ) );
        tester.assertRenderedPage( CodedListMgrPage.class );
    }
}
