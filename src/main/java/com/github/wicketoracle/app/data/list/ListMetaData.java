package com.github.wicketoracle.app.data.list;

import org.apache.wicket.IClusterable;

public class ListMetaData implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private int codeMaxLength;
    private int itemNameMaxLength;

    public ListMetaData()
    {

    }

    public final int getCodeMaxLength()
    {
        return codeMaxLength;
    }

    public final void setCodeMaxLength( final int pCodeMaxLength )
    {
        codeMaxLength = pCodeMaxLength;
    }

    public final int getItemNameMaxLength()
    {
        return itemNameMaxLength;
    }

    public final void setItemNameMaxLength( final int pItemNameMaxLength )
    {
        itemNameMaxLength = pItemNameMaxLength;
    }
}
