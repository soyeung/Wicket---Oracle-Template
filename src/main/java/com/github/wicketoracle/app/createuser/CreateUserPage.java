package com.github.wicketoracle.app.createuser;

import java.sql.SQLException;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Localizer;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.oracle.exception.SQLExceptionCodes;
import com.github.wicketoracle.session.Session;


/**
 * Page which can be used to create new users of the application.
 * The following can be specified:
 *
 * <ul>
 *     <li>Username</li>
 *     <li>Password</li>
 *     <li>User type [essentially a grouping of db roles]</li>
 *     <li>Oracle profile [dictating password management strategy]</li>
 * </ul>
 *
 * @author Andrew Hall
 *
 */

@RequireHttps
@AuthorizeInstantiation( RequiredRoles.ROLE_CREATE_USER )
public final class CreateUserPage extends StandardPage
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( CreateUserPage.class );

    private final Panel          menuPanel      = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final CreateUserForm createUserForm = new CreateUserForm( "createUserForm" );

    /**
     * No arguments constructor.
     */
    public CreateUserPage()
    {
        add( menuPanel );
        add( createUserForm );
    }

    /**
     * Form through which a user can change their own password
     */
    private final class CreateUserForm extends StatelessForm<NewUser>
    {

        private static final long serialVersionUID = 1L;

        private static final int MAX_PASSWORD_LENGTH = 30;
        private static final int MAX_USERNAME_LENGTH = 30;

        private final NewUser createUserBean = new NewUser();

        private final RequiredTextField<String> usernameTextField             = new RequiredTextField<String>( "username"     , new PropertyModel<String>( createUserBean , "username" ) );
        private final PasswordTextField         passwordTextField             = new PasswordTextField( "password"             , new PropertyModel<String>( createUserBean , "password" ) );
        private final PasswordTextField         passwordConfirmationTextField = new PasswordTextField( "passwordConfirmation" , new PropertyModel<String>( createUserBean , "passwordConfirmation" ) );

        private final DropDownChoice<IntegerSelectChoice> userTypeDropDownChoice    = new DropDownChoice<IntegerSelectChoice>( "userType" );
        private final DropDownChoice<IntegerSelectChoice> userProfileDropDownChoice = new DropDownChoice<IntegerSelectChoice>( "userProfile" );
        private final DropDownChoice<IntegerSelectChoice> userLocaleDropDownChoice  = new DropDownChoice<IntegerSelectChoice>( "userLocale" );

        /**
         * Constructor
         */
        public CreateUserForm( final String pId )
        {
            super( pId );

            /* add items to my form & configure them */

            add( usernameTextField );
            add( passwordTextField );
            add( passwordConfirmationTextField );

            usernameTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( MAX_USERNAME_LENGTH ) ) );
            passwordTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( MAX_PASSWORD_LENGTH ) ) );
            passwordConfirmationTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( MAX_PASSWORD_LENGTH ) ) );

            usernameTextField.add( new LengthBetweenValidator( 1 , MAX_USERNAME_LENGTH ) );
            passwordTextField.add( new LengthBetweenValidator( 1 , MAX_PASSWORD_LENGTH ) );
            passwordConfirmationTextField.add( new LengthBetweenValidator( 1 , MAX_PASSWORD_LENGTH ) );
            add( new EqualPasswordInputValidator( passwordTextField , passwordConfirmationTextField ) );

            /* retrieve the data to populate the lists */
            final Session session = ( Session ) getSession();

            CreateUserDAO                                       dataService    = null;
            Map<String , SelectChoiceList<IntegerSelectChoice>> selectListData = null;

            final SelectChoiceList<IntegerSelectChoice> userTypeData;
            final SelectChoiceList<IntegerSelectChoice> userProfileData;
            final SelectChoiceList<IntegerSelectChoice> userLocaleData;

            try
            {
                dataService = new CreateUserDAO( session.getUsername() , session.getPassword() );

                selectListData = dataService.getKeyValueRefData();
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when constructing create new user page -> {}; error code -> {}; sql state -> {}"
                ,   new Object [ ]
                    {
                        sqle.getMessage()
                    ,   sqle.getErrorCode()
                    ,   sqle.getSQLState()
                    }
                );
                error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
            }
            finally
            {
                if ( ! dataService.closeConnection() )
                {
                    final String errmsg = getLocalizer().getString( "MessageUnexpectedError" , this );
                    error( errmsg );
                }
            }

            userTypeData    = selectListData.get( "UTY" );
            userProfileData = selectListData.get( "AURP" );
            userLocaleData  = selectListData.get( "LNG" );

            /* all select lists are required */
            if ( userTypeData != null && userProfileData != null && userLocaleData != null )
            {

                userTypeDropDownChoice.setChoices( userTypeData ).setChoiceRenderer( userTypeData ).setModel( new PropertyModel <IntegerSelectChoice>( createUserBean , "utyId" ) );
                userTypeDropDownChoice.setNullValid( false ).setRequired( true );

                userProfileDropDownChoice.setChoices( userProfileData ).setChoiceRenderer( userProfileData ).setModel( new PropertyModel <IntegerSelectChoice>( createUserBean , "aurpId" ) );
                userProfileDropDownChoice.setNullValid( false ).setRequired( true );

                userLocaleDropDownChoice.setChoices( userLocaleData ).setChoiceRenderer( userLocaleData ).setModel( new PropertyModel <IntegerSelectChoice>( createUserBean , "lngId" ) );
                userLocaleDropDownChoice.setNullValid( false ).setRequired( true );
            }
            else
            {
                error( getLocalizer().getString( "MessageReferenceDataNotSetup" , this ) );
                setVisible( false );
            }

            add( userTypeDropDownChoice );
            add( userProfileDropDownChoice );
            add( userLocaleDropDownChoice );
        }

        /**
         * Submit the form - attempt to change the password
         */
        @Override
        protected void onSubmit()
        {
            if ( getIsDebugInfoVisible() )
            {
                info( "Username :: " + createUserBean.getUsername() );
                info( "Password :: " + createUserBean.getPassword() );
                info( "Password confirmation :: " + createUserBean.getPasswordConfirmation() );
                info( "User type :: " + Integer.toString( createUserBean.getUtyId().getKey() ) );
                info( "User profile :: " + Integer.toString( createUserBean.getAurpId().getKey() ) );
                info( "User locale :: " + Integer.toString( createUserBean.getLngId().getKey() ) );
            }

            final Session   session   = ( Session ) getSession();
            final Localizer localiser = getLocalizer();

            CreateUserDAO dataService = null;

            try
            {
                dataService = new CreateUserDAO( session.getUsername() , session.getPassword() );

                dataService.createUser
                (
                    createUserBean.getUsername()
                ,   createUserBean.getPassword()
                ,   createUserBean.getUtyId().getKey()
                ,   createUserBean.getAurpId().getKey()
                ,   createUserBean.getLngId().getKey()
                );

                dataService.doCommit();

                info( localiser.getString( "MessageSuccess" , this ) );
            }
            catch ( SQLException sqle )
            {
                switch( sqle.getErrorCode() )
                {
                    case SQLExceptionCodes.USER_ALREADY_EXISTS  :
                        error( localiser.getString( "MessageUserAlreadyExists" , this ) );
                        break;

                    case SQLExceptionCodes.DANGEROUS_USERNAME :
                        error( localiser.getString( "MessageDangerousUsername" , this ) );
                        break;

                    case SQLExceptionCodes.DANGEROUS_PASSWORD :
                        error( localiser.getString( "MessageDangerousPassword" , this ) );
                        break;

                    case SQLExceptionCodes.NON_COMPLIANT_PASSWORD :
                        error( localiser.getString( "MessageNonCompliantPassword" , this ) );
                        break;

                    default :
                        LOGGER.error
                        (
                            "SQL Exception when submitting the create new user form -> {}; error code -> {}; sql state -> {}"
                        ,   new Object[]
                            {
                                sqle.getMessage()
                            ,   sqle.getErrorCode()
                            ,   sqle.getSQLState()
                            }
                        );
                        error( localiser.getString( "MessageUnexpectedError" , this ) );
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
