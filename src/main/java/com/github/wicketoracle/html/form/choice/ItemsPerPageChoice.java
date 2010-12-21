package com.github.wicketoracle.html.form.choice;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

public class ItemsPerPageChoice extends DropDownChoice<IntegerSelectChoice>
{
    /** Serializable interface :: file version number.*/
    private static final long serialVersionUID = 1L;

    private static SelectChoiceList<IntegerSelectChoice> getChoices( final int pStart , final int pEnd , final int pIncrement )
    {
        List<IntegerSelectChoice> choices = new ArrayList<IntegerSelectChoice>();

        for ( int i = pStart ; i <= pEnd ; i += pIncrement )
        {
            choices.add( new IntegerSelectChoice( i , Integer.toString( i ) ) );
        }

        return new SelectChoiceList<IntegerSelectChoice>( choices.toArray( new IntegerSelectChoice[ ( pEnd - pStart ) / pIncrement ] ) );
    }

    /**
     * Construct a list intended to indicate how many
     * records per page a user would like to see at once.
     * This is useful for pagination.
     *
     *  @param pId    wicket id
     *  @param pModel model object to bound to the RecordPerPageChoice
     */
    public ItemsPerPageChoice( final String pId , final IModel<IntegerSelectChoice> pModel , final int pStart , final int pEnd, final int pIncrement )
    {
        super( pId , pModel , getChoices( pStart , pEnd , pIncrement ) );
    }
}
