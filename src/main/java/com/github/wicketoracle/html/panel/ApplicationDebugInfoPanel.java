package com.github.wicketoracle.html.panel;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;

import com.github.wicketoracle.session.Session;

/**
 *
 * @author Andrew Hall
 *
 */
public class ApplicationDebugInfoPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private final Label applicationModeLabel       = new Label( "LabelApplicationMode" );
    private final Label pageClassLabel             = new Label( "LabelPageClass" );
    private final Label userLocaleLabel            = new Label( "LabelUserLocale" );
    private final Label userEncryptedPasswordLabel = new Label( "LabelUserEncryptedPassword" );

    /**
     *
     * @param pId
     * @param pClass
     */
    public ApplicationDebugInfoPanel( final String pId , final Class<? extends WebPage> pClass )
    {
        super( pId );
        add( pageClassLabel.setDefaultModel( new Model<String>( pClass.getName() ) ) );

        Session session = ( Session ) getSession();

        add( applicationModeLabel.setDefaultModel( new Model<String>( WebApplication.get().getConfigurationType() ) ) );
        add( userLocaleLabel.setDefaultModel( new Model<String>( session.getPersonalDetails().getLanguageCode() ) ) );
        add( userEncryptedPasswordLabel.setDefaultModel( new Model<String>( session.getEncryptedPassword() ) ) );
    }
}
