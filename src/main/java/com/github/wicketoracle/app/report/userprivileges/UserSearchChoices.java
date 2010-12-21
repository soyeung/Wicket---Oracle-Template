package com.github.wicketoracle.app.report.userprivileges;

import org.apache.wicket.IClusterable;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;


final class UserSearchChoices implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private IntegerSelectChoice userId = new IntegerSelectChoice( 0 );

    /**
     * Constructor
     */
    public UserSearchChoices()
    {

    }

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
}
