package com.github.wicketoracle.app.report.securitymatrixbyuser;

import org.apache.wicket.IClusterable;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;


final class UserSearchChoices implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private IntegerSelectChoice userId   = new IntegerSelectChoice( 0 );
    private IntegerSelectChoice dbRoleId = new IntegerSelectChoice( 0 );

    public IntegerSelectChoice getUserId()
    {
        IntegerSelectChoice temp;
        if ( userId == null )
        {
            temp = new IntegerSelectChoice( 0 );
        }
        else
        {
            temp = userId;
        }

        return temp;
    }

    public void setUserId( final IntegerSelectChoice pUserId )
    {
        userId = pUserId;
    }

    public IntegerSelectChoice getDbRoleId()
    {
        IntegerSelectChoice temp;
        if ( dbRoleId == null )
        {
            temp = new IntegerSelectChoice( 0 );
        }
        else
        {
            temp = dbRoleId;
        }

        return temp;
    }

    public void setDbRoleId( final IntegerSelectChoice pDbRoleId )
    {
        dbRoleId = pDbRoleId;
    }
}
