package com.github.wicketoracle.app.user.standard.mgr;

import java.sql.SQLException;

import org.apache.wicket.Localizer;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.util.value.ValueMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.oracle.exception.SQLExceptionCodes;
import com.github.wicketoracle.session.Session;


@RequireHttps
@AuthorizeInstantiation( RequiredRoles.ROLE_STD_USER_PASSWORD_MGR )
public final class StandardUserPasswordMgrPage extends StandardPage
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( StandardUserPasswordMgrPage.class );

    private final Panel           menuPanel       = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final Label           headerLabel;
    private final PasswordMgrForm passwordMgrForm;

    /**
     *
     * @param pStandardUser
     */
    public StandardUserPasswordMgrPage( final StandardUser pStandardUser )
    {
        add( menuPanel );

        ValueMap headerLabelParams = new ValueMap();
        headerLabelParams.put( "username" , pStandardUser.getUsername() );
        headerLabel = new Label( "HeaderChangePassword" , new StringResourceModel( "HeaderChangePassword" , this , new Model<ValueMap>( headerLabelParams ) ) );
        add( headerLabel );

        passwordMgrForm = new PasswordMgrForm( "changePasswordForm" , pStandardUser.getUserId() );
        add( passwordMgrForm );
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class PasswordMgrForm extends StatelessForm<StandardUserPassword>
    {
        private static final long serialVersionUID = 1L;

        private int userId = 0;

        private final StandardUserPassword standardUserPassword = new StandardUserPassword();

        private final PasswordTextField passwordTextField             = new PasswordTextField( "password"             , new PropertyModel<String>( standardUserPassword , "password" ) );
        private final PasswordTextField passwordConfirmationTextField = new PasswordTextField( "passwordConfirmation" , new PropertyModel<String>( standardUserPassword , "passwordConfirmation" ) );

        public PasswordMgrForm( final String pId , final int pUserId )
        {
            super( pId );
            userId = pUserId;

            add( passwordTextField );
            add( passwordConfirmationTextField );

            add( new EqualPasswordInputValidator( passwordTextField , passwordConfirmationTextField ) );
        }

        /**
         *
         */
        @Override
        public void onSubmit()
        {
            if ( getIsDebugInfoVisible() )
            {
                info( "Change user password :: User id :: " + userId );
                info( "Change user password :: Password :: " + standardUserPassword.getPassword() );
                info( "Change user password :: Password Confirmation :: " + standardUserPassword.getPasswordConfirmation() );
            }

            final Session session = ( Session ) getSession();
            final Localizer localiser = getLocalizer();

            StandardUserPasswordMgrDAO dataService = null;

            try
            {
                dataService = new StandardUserPasswordMgrDAO( session.getUsername() , session.getPassword() );

                dataService.changePassword( userId , standardUserPassword.getPassword() );

                info( localiser.getString( "MessageSuccess" , this ) );
            }
            catch ( SQLException sqle )
            {
                switch( sqle.getErrorCode() )
                {
                    case SQLExceptionCodes.DANGEROUS_PASSWORD :
                        error( localiser.getString( "MessageDangerousPassword" , this ) );
                        break;

                    case SQLExceptionCodes.NON_COMPLIANT_PASSWORD :
                        error( localiser.getString( "MessageNonCompliantPassword" , this ) );
                        break;

                    case SQLExceptionCodes.NON_REUSABLE_PASSWORD :
                        error( localiser.getString( "MessageNonReusablePassword" , this ) );
                        break;

                    default :
                        LOGGER.error
                        (
                            "SQL Exception when changing the password of user '{}' -> {}; error code -> {}; sql state -> {}"
                        ,   new Object[]
                            {
                                Integer.toString( userId )
                            ,   sqle.getMessage()
                            ,   sqle.getErrorCode()
                            ,   sqle.getSQLState()
                            }
                        );
                        error( localiser.getString( "MessageUnexpectedError" , this ) );
                        break;
                }
            }
            finally
            {
                if ( ! dataService.closeConnection() )
                {
                    error( localiser.getString( "MessageUnexpectedError" , this ) );
                }
            }
        }
    }
}
