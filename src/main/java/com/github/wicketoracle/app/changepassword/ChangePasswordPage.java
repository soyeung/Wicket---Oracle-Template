package com.github.wicketoracle.app.changepassword;

import java.sql.SQLException;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Localizer;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.oracle.exception.SQLExceptionCodes;
import com.github.wicketoracle.session.Session;


@RequireHttps
@AuthorizeInstantiation( RequiredRoles.ROLE_CREATE_SESSION )
public final class ChangePasswordPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( ChangePasswordPage.class );

    private Panel              menuPanel          = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private ChangePasswordForm changePasswordForm = new ChangePasswordForm( "changePasswordForm" );

    /**
     *
     */
    public ChangePasswordPage()
    {
        add( menuPanel );
        add( changePasswordForm );
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class ChangePasswordForm extends StatelessForm <UserPassword>
    {
        private static final long serialVersionUID = 1L;

        private static final int MAX_PASSWORD_LENGTH = 30;

        private final UserPassword changePasswordBean = new UserPassword();

        private final PasswordTextField currentPasswordTextField      = new PasswordTextField( "currentPassword"      , new PropertyModel<String>( changePasswordBean , "currentPassword" ) );
        private final PasswordTextField newPasswordTextField          = new PasswordTextField( "newPassword"          , new PropertyModel<String>( changePasswordBean , "newPassword" ) );
        private final PasswordTextField confirmedNewPasswordTextField = new PasswordTextField( "confirmedNewPassword" , new PropertyModel<String>( changePasswordBean , "confirmedNewPassword" ) );

        /**
         *
         * @param pId
         */
        public ChangePasswordForm( final String pId )
        {
            super( pId );
            add( currentPasswordTextField );
            add( newPasswordTextField );
            add( confirmedNewPasswordTextField );

            currentPasswordTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( MAX_PASSWORD_LENGTH ) ) );
            newPasswordTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( MAX_PASSWORD_LENGTH ) ) );
            confirmedNewPasswordTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( MAX_PASSWORD_LENGTH ) ) );

            currentPasswordTextField.add( new LengthBetweenValidator( 1 , MAX_PASSWORD_LENGTH ) );
            newPasswordTextField.add( new LengthBetweenValidator( 1 , MAX_PASSWORD_LENGTH ) );
            confirmedNewPasswordTextField.add( new LengthBetweenValidator( 1 , MAX_PASSWORD_LENGTH ) );
            add( new EqualPasswordInputValidator( newPasswordTextField , confirmedNewPasswordTextField ) );
        }

        /**
         *
         */
        @Override
        protected void onSubmit()
        {
            if ( getIsDebugInfoVisible() )
            {
                info( "Current Password :: " + changePasswordBean.getCurrentPassword() );
                info( "New Password :: " + changePasswordBean.getNewPassword() );
                info( "New password - confimation :: " + changePasswordBean.getConfirmedNewPassword() );
            }

            final Session   session   = ( Session ) getSession();
            final Localizer localiser = getLocalizer();

            ChangePasswordDAO dataService = null;

            try
            {
                dataService = new ChangePasswordDAO( session.getUsername() , session.getPassword() );

                dataService.changePassword( changePasswordBean.getCurrentPassword() , changePasswordBean.getNewPassword() );

                session.setPassword( changePasswordBean.getNewPassword() );

                dataService.setConnectionInvalid();

                info( localiser.getString( "MessageSuccess" , this ) );
            }
            catch ( SQLException sqle )
            {
                switch( sqle.getErrorCode() )
                {
                    case SQLExceptionCodes.INCORRECT_CURRENT_PASSWORD :
                        error( localiser.getString( "MessageIncorrectCurrentPassword" , this ) );
                        break;

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
                            "SQL Exception when changing user password -> {}; error code -> {}; sql state -> {}"
                        ,   new Object[]
                            {
                                sqle.getMessage()
                            ,   sqle.getErrorCode()
                            ,   sqle.getSQLState()
                            }
                        );
                        error( localiser.getString( "MessageChangePasswordError" , this ) );
                }
            }
            finally
            {
                if ( ( dataService != null ) && ( ! dataService.closeConnection() ) )
                {
                    error( localiser.getString( "MessageUnexpectedError" , this ) );
                }
            }
        }
    }
}
