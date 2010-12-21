package com.github.wicketoracle.html.panel.menu;

import org.apache.wicket.markup.html.panel.Panel;

import com.github.wicketoracle.exception.NotInstantiableException;


public class PostLoginMenuPanelFactory
{
    /**
      * This is a non-instantiable utility class.
      */
    protected PostLoginMenuPanelFactory() throws NotInstantiableException
    {
        throw new NotInstantiableException();
    }

    public static Panel getPostLoginMenuPanel()
    {
        return new PostLoginMenuPanel( "PanelMenu" );
    }
}
