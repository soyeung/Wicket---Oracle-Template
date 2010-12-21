package com.github.wicketoracle.html.form.choice;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

public class YesNoChoice extends DropDownChoice<StringSelectChoice>
{
    private static final long serialVersionUID = 1L;

    /** List of choices to appear in drop down list */
    private static final SelectChoiceList<StringSelectChoice> YES_NO_LIST
      = new SelectChoiceList<StringSelectChoice>( new StringSelectChoice( "Y" , "Yes" ) , new StringSelectChoice( "N" , "No" ) );

    /**
     * Constructor
     */
    public YesNoChoice( final String pId )
    {
        super( pId , YES_NO_LIST, YES_NO_LIST );
    }

    /**
     * Constructor
     */
    public YesNoChoice( final String pId , final IModel<StringSelectChoice> pModel )
    {
        super( pId , pModel , YES_NO_LIST , YES_NO_LIST );
    }
}
