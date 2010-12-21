package com.github.wicketoracle.html.form.choice;

import java.io.Serializable;

public abstract class SelectChoice<T extends Serializable> implements Serializable, Comparable<SelectChoice<T>>
{
    private static final long serialVersionUID = 42L;

    private T key;
    private String display;

    public SelectChoice( final T pKey, final String pDisplay )
    {
        key     = pKey;
        display = pDisplay;
    }

    /**
     * @return display value
     */
    public final String getDisplay()
    {
        return display;
    }

    /**
     * Set the display value
     * @param display value to set
     */
    public final void setDisplay( final String pDisplay )
    {
        display = pDisplay;
    }

    /**
     * @return key value
     */
    public final T getKey()
    {
        return key;
    }

    /**
     * Set the key value
     * @param key value to set
     */
    public final void setKey( final T pKey )
    {
        key = pKey;
    }

    /**
     * @return return String representation of the key
     */
    public final String getKeyAsString()
    {
        return key.toString();
    }

    /**
     * Return String version of key so {@link org.apache.wicket.Component#getModelObjectAsString()} returns
     * a sensible value.  Useful if storing the value in {@link org.apache.wicket.PageParameters} .
     *
     * @return {@link #getKeyAsString()}
     */
    @Override
    public final String toString()
    {
        return getKeyAsString();
    }

    /**
     * Implementation of Comparable.
     *
     * @param o the item to compare
     * @return this.key.compareTo( o.key )
     * @throws UnsupportedOperationException if the underlying key class does not implement Comparable
     */
    @SuppressWarnings( { "unchecked" } )
    public final int compareTo( final SelectChoice<T> o )
    {
        if ( ! ( key instanceof Comparable ) )
        {
            throw new UnsupportedOperationException( "Cannot compare non-comparable object: " + key.getClass().getName() );
        }

        Comparable<T> comparableKey = ( Comparable<T> ) key;

        return comparableKey.compareTo( o.key );
    }
}
