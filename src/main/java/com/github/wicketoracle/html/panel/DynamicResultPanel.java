package com.github.wicketoracle.html.panel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Panel which allows a result set, whose number of columns is unknown at runtime to be rendered.
 *
 * @author Andrew Hall
 *
 */
public final class DynamicResultPanel extends Panel
{
    /** for serialisation */
    private static final long serialVersionUID = 1L;

    private transient DynamicResult dynamicResult;

    /**
     * Constructor
     */
    public DynamicResultPanel( final String pId )
    {
        super( pId );

        /** data table's column headers */
        add
        (
            new RefreshingView<String>( "HeaderResultTable" )
            {
                private static final long serialVersionUID = 1L;

                @Override
                protected Iterator<IModel<String>> getItemModels()
                {
                    /* convert the list items into models */
                    return new ModelIteratorAdapter<String>( getColumnHeadings().listIterator() )
                    {
                        @Override
                        protected IModel<String> model( final String object )
                        {
                            return new CompoundPropertyModel<String>( object );
                        }
                    };
                }

                @Override
                protected void populateItem( final Item<String> item )
                {
                    item.add( new Label( "LabelHeader", item.getModelObject() ) );
                }
            }
        );

        /* data content */
        add
        (
            new RefreshingView<List<String>>( "DataRow" )
            {
                private static final long serialVersionUID = 1L;

                @Override
                protected Iterator<IModel<List<String>>> getItemModels()
                {
                    /* convert the list items into models */
                    return new ModelIteratorAdapter<List<String>>( getTableContent().listIterator() )
                    {
                        @Override
                        protected IModel<List<String>> model( final List<String> object )
                        {
                            return new CompoundPropertyModel<List<String>>( object );
                        }
                    };
                }

                @Override
                protected void populateItem( final Item<List<String>> item )
                {
                    /* individual cell */
                    item.add
                    (
                        new RefreshingView<String>( "DataRowContent" )
                        {
                            private static final long serialVersionUID = 1L;

                            @Override
                            protected Iterator<IModel<String>> getItemModels()
                            {
                                /* convert the list items into models */
                                return new ModelIteratorAdapter<String>( item.getModelObject().iterator() )
                                {
                                    @Override
                                    protected IModel<String> model( final String object )
                                    {
                                        return new CompoundPropertyModel<String>( object );
                                    }
                                };
                            }

                            @Override
                            protected void populateItem( final Item<String> item )
                            {
                                 item.add( new Label( "DataCell", item.getModelObject() ) );
                            }
                        }
                    );
                }
            }
        );
    }

    /**
     *
     * @return
     */
    public DynamicResult getDynamicResult()
    {
        return dynamicResult;
    }

    /**
     *
     * @param pDynamicResult
     */
    public void setDynamicResult( final DynamicResult pDynamicResult )
    {
        dynamicResult = pDynamicResult;
    }

    /**
     *
     * @return
     */
    public List<String> getColumnHeadings()
    {
        List<String> temp;
        if ( dynamicResult == null )
        {
            temp = new ArrayList<String>();
        }
        else
        {
            temp = dynamicResult.getResultHeaders();
        }

        return temp;
    }

    /**
     *
     * @return
     */
    public List<List<String>> getTableContent()
    {
        List<List<String>> temp;
        if ( dynamicResult == null )
        {
            temp = new ArrayList<List<String>>();
        }
        else
        {
            temp = dynamicResult.getResults();
        }

        return temp;
    }
}
