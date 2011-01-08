package com.github.wicketoracle.app.data.list.updateonly;

import com.github.wicketoracle.AppTester;
import com.github.wicketoracle.app.data.DataStructure;

import junit.framework.TestCase;

public class TestUpdateOnlyListMgrPage extends TestCase
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
        tester.startPage( new UpdateOnlyListMgrPage( new DataStructure( 1 , "" , "" , true , "X" , true ) ) );
        tester.assertRenderedPage( UpdateOnlyListMgrPage.class );
        tester.assertNoLeakedConnections();
    }
}
