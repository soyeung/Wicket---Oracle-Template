package com.github.wicketoracle.html.panel.menu;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;

import com.github.wicketoracle.app.changepassword.ChangePasswordPage;
import com.github.wicketoracle.app.createuser.CreateUserPage;
import com.github.wicketoracle.app.data.DataMgrPage;
import com.github.wicketoracle.app.home.HomePage;
import com.github.wicketoracle.app.login.LoginPage;
import com.github.wicketoracle.app.report.ReportPage;
import com.github.wicketoracle.app.ucp.UCPConfigPage;
import com.github.wicketoracle.app.user.delegate.mgr.DelegateUserMgrPage;
import com.github.wicketoracle.app.user.standard.mgr.StandardUserMgrPage;
import com.github.wicketoracle.session.Session;


/**
 * Panel modelling the navigation menu that is presented <i>after</i> users
 * have successfully authenticated with the application
 *
 * @author Andrew Hall
 *
 */
class PostLoginMenuPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private transient Session session = ( Session ) getSession();

    private Label         usernameLabel           = new Label( "LabelCurrentUsername" , session.getUsername() );
    private DateLabel     passwordExpiryDateLabel = new DateLabel( "LabelPasswordExpiryDate" , new Model<java.util.Date>( session.getPersonalDetails().getPasswordExpiryDate() ) , new PatternDateConverter( "dd-MM-yyyy", false ) );
    private StatelessLink logoutLink              = new StatelessLink( "LinkLogout" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            ( ( Session ) getSession() ).invalidateNow();

                                                            setResponsePage( LoginPage.class );
                                                        }
                                                    };
    private StatelessLink homePageLink            = new StatelessLink( "LinkHomePage" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            setResponsePage( HomePage.class );
                                                        }
                                                    };
    private StatelessLink changePasswordLink      = new StatelessLink( "LinkChangePassword" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            setResponsePage( ChangePasswordPage.class );
                                                        }
                                                    };
    private StatelessLink createUserLink          = new StatelessLink( "LinkCreateUser" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            setResponsePage( CreateUserPage.class );
                                                        }
                                                    };
    private StatelessLink manageUCPLink           = new StatelessLink( "LinkManageUCP" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            setResponsePage( UCPConfigPage.class );
                                                        }
                                                    };
    private StatelessLink manageDelegateUsersLink = new StatelessLink( "LinkManageDelegateUsers" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            setResponsePage( DelegateUserMgrPage.class );
                                                        }
                                                    };
    private StatelessLink manageStandardUsersLink = new StatelessLink( "LinkManageStandardUsers" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            setResponsePage( StandardUserMgrPage.class );
                                                        }
                                                    };
    private StatelessLink manageDataLink          = new StatelessLink( "LinkManageData" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            setResponsePage( DataMgrPage.class );
                                                        }
                                                    };
    private StatelessLink reportsLink             = new StatelessLink( "LinkReports" )
                                                    {
                                                        private static final long serialVersionUID = 1L;

                                                        public void onClick()
                                                        {
                                                            setResponsePage( ReportPage.class );
                                                        }
                                                    };
    /**
     * Construct.
     *
     * @param pId
     *            component id
     */
    PostLoginMenuPanel( final String pId )
    {
        super( pId );

        final Roles roles = ( ( Session ) getSession() ).getRoles();

        add( usernameLabel );
        add( passwordExpiryDateLabel );
        add( logoutLink );
        add( homePageLink );
        add( changePasswordLink );
        add( createUserLink.setVisible( roles.hasRole( com.github.wicketoracle.app.createuser.RequiredRoles.ROLE_CREATE_USER ) ) );
        add( manageUCPLink.setVisible( roles.hasRole( com.github.wicketoracle.app.ucp.RequiredRoles.ROLE_UCP_MGR ) ) );
        add( manageDelegateUsersLink.setVisible( roles.hasRole( com.github.wicketoracle.app.user.delegate.mgr.RequiredRoles.ROLE_DELEGATE_APP_USER_MGR ) ) );
        add( manageStandardUsersLink.setVisible( roles.hasRole( com.github.wicketoracle.app.user.standard.mgr.RequiredRoles.ROLE_STANDARD_APP_USER_MGR ) ) );
        add( manageDataLink.setVisible( roles.hasRole( com.github.wicketoracle.app.data.RequiredRoles.ROLE_REF_DATA_MGR ) ) );
        add( reportsLink.setVisible( roles.hasRole( com.github.wicketoracle.app.report.RequiredRoles.ROLE_REPORT_USER ) ) );
    }
}
