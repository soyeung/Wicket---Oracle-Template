package com.github.wicketoracle.html.panel.menu;


import org.apache.wicket.markup.html.panel.Panel;

import com.github.wicketoracle.exception.NotInstantiableException;

public class PreLoginMenuPanelFactory
{
    /**
      * This is a non-instantiable utility class.
      */
    protected PreLoginMenuPanelFactory() throws NotInstantiableException
    {
        throw new NotInstantiableException();
    }

    public static Panel getPreLoginMenuPanel()
    {
        return new PreLoginMenuPanel( "PanelMenu" );
    }
}
