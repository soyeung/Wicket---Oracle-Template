package com.github.wicketoracle.app.login;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;
import org.apache.wicket.protocol.https.RequireHttps;

import com.github.wicketoracle.app.home.HomePage;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PreLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


/**
 * Login page - used for user authentication
 *
 * @author Andrew Hall
 */
@RequireHttps
public final class LoginPage extends StandardPage
{
    private Panel     menuPanel = PreLoginMenuPanelFactory.getPreLoginMenuPanel();
    private LoginForm loginForm = new LoginForm( "loginForm" );

    /**
     * Constructor
     */
    public LoginPage( final PageParameters pPageParameters )
    {
        add( menuPanel );
        add( loginForm );
    }

    /**
     * Form for collecting authentication credentials from end users
     *
     * @author Andrew Hall
     *
     */
    private final class LoginForm extends StatelessForm<UserLogon>
    {
        private static final long serialVersionUID = 1L;

        private static final int MAX_PASSWORD_LENGTH = 30;
        private static final int MAX_USERNAME_LENGTH = 30;

        private final UserLogon logonBean = new UserLogon();

        private final RequiredTextField<String> usernameTextField   = new RequiredTextField<String>( "username" , new PropertyModel<String>( logonBean , "username" ) );
        private final PasswordTextField         passwordTextField   = new PasswordTextField( "password" , new PropertyModel<String>( logonBean , "password" ) );

        /**
         * Constructor
         *
         * @param pId
         */
        public LoginForm( final String pId )
        {
            super( pId );
            add( usernameTextField );
            add( passwordTextField );

            usernameTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( MAX_USERNAME_LENGTH ) ) );
            passwordTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( MAX_PASSWORD_LENGTH ) ) );

            usernameTextField.add( new LengthBetweenValidator( 1 , MAX_USERNAME_LENGTH ) );
            passwordTextField.add( new LengthBetweenValidator( 1 , MAX_PASSWORD_LENGTH ) );
        }

        /**
         * Try to authenticate the user based on the username / password supplied
         */
        @Override
        protected void onSubmit()
        {
            Session session = ( Session ) getSession();

            if ( session.authenticate( logonBean.getUsername() , logonBean.getPassword() ) )
            {
                setResponsePage( HomePage.class );
            }
            else
            {
                if ( getIsDebugInfoVisible() )
                {
                    info( "Login :: Username :: " + logonBean.getUsername() );
                    info( "Login :: Password :: " + logonBean.getPassword() );
                }

                error( getLocalizer().getString( "MessageLoginFailure", this ) );
            }
        }
    }
}
