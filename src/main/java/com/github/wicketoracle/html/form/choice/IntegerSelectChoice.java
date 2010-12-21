package com.github.wicketoracle.html.form.choice;

public class IntegerSelectChoice extends SelectChoice<Integer>
{
    private static final long serialVersionUID = 42L;

    public IntegerSelectChoice( final Integer pKey )
    {
        super( pKey, pKey.toString() );
    }

    public IntegerSelectChoice( final Integer pKey, final String pDisplay )
    {
        super( pKey, pDisplay );
    }
}
