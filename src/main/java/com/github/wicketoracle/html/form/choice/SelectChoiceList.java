package com.github.wicketoracle.html.form.choice;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class SelectChoiceList<T extends SelectChoice<?>> extends ArrayList<T> implements IChoiceRenderer<T>
{
    private static final long serialVersionUID = 1L;

    /**
     * Create list with initial list of items.
     *
     * @param items initial items
     */
    public SelectChoiceList( final T... pItems )
    {
        Collections.addAll( this , pItems );
    }

    /**
     * Add item to list if not there and then sort.  Returns pre-existing item ( if there ) or item passed in.
     *
     * @param item to add to list
     * @return item added
     */
    public final T addSorted( final T pItem )
    {
        for ( T t : this )
        {
            if ( t.getKey().equals( pItem.getKey() ) )
            {
                return t;
            }
        }

        add( pItem );
        Collections.sort( this );
        return pItem;
    }

    /**
     * Get the display value from the SelectChoice ( what the user sees )
     *
     * @param object a SelectChoice object
     * @return object.getDisplay()
     */
    public final Object getDisplayValue( final T pObject )
    {
        return pObject.getDisplay();
    }

    /**
     * Get key value ( what is returned from browser )
     *
     * @param object a SelectChoice object
     * @param index not used
     * @return object.getKeyAsString()
     */
    public final String getIdValue( final T pObject, final int pIndex )
    {
        return pObject.getKeyAsString();
    }
}
