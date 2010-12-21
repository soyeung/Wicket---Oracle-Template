package com.github.wicketoracle.html.form.choice;

public class StringSelectChoice extends SelectChoice<String>
{
    private static final long serialVersionUID = 1L;

    public StringSelectChoice( final String pKey )
    {
        super( pKey, pKey );
    }

    public StringSelectChoice( final String pKey, final String pDisplay )
    {
        super( pKey, pDisplay );
    }
}
