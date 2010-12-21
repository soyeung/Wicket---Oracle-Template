package com.github.wicketoracle.app.user.delegate.mgr;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_DGT_APP_USER_ROLE_MGR )
public final class DelegateUserRoleMgrPage extends StandardPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DelegateUserRoleMgrPage.class );

    private final Panel       menuPanel    = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final RoleMgrForm roleMgrForm;
    private final Label       headerLabel;

    /**
     *
     * @param pDelegateUser
     */
    public DelegateUserRoleMgrPage( final DelegateUser pDelegateUser )
    {
        add( menuPanel );

        roleMgrForm  = new RoleMgrForm( "FormRoleMgr" , pDelegateUser.getUserId() );
        add( roleMgrForm );

        ValueMap headerLabelParams = new ValueMap();
        headerLabelParams.put( "username" , pDelegateUser.getUsername() );
        headerLabel = new Label( "HeaderChangeRoles" , new StringResourceModel( "HeaderChangeRoles" , this , new Model<ValueMap>( headerLabelParams ) ) );
        add( headerLabel );
    }

    private final class RoleMgrForm extends StatelessForm<DelegateUserRole>
    {
        private static final long serialVersionUID = 1L;

        private int userId;

        private List<DelegateUserRole> roleList = new ArrayList<DelegateUserRole>();

        /**
         *
         * @param pId
         * @param pUserId
         */
        public RoleMgrForm( final String pId , final int pUserId )
        {
            super( pId );
            userId = pUserId;

            add
            (
                new RefreshingView<DelegateUserRole>( "RoleList" )
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected Iterator<IModel<DelegateUserRole>> getItemModels()
                    {
                        final Session session = ( Session ) getSession();

                        OracleDelegateUserRoleMgrDAO dataService = null;

                        try
                        {
                            dataService = new OracleDelegateUserRoleMgrDAO( session.getUsername() , session.getPassword() );

                            roleList = dataService.getUserRoles( userId );
                        }
                        catch ( SQLException sqle )
                        {
                            LOGGER.error
                            (
                                "SQL Exception when retrieving roles for user '{}' -> {}; error code -> {}; sql state -> {}"
                            ,   new Object [ ]
                                {
                                    Integer.toString( userId )
                                ,   sqle.getMessage()
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
                                error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
                            }
                        }

                        return new ModelIteratorAdapter<DelegateUserRole>( roleList.iterator() )
                        {
                            @Override
                            protected IModel<DelegateUserRole> model( final DelegateUserRole pObject )
                            {
                                return new CompoundPropertyModel<DelegateUserRole>( pObject );
                            }
                        };
                    }

                    @Override
                    protected void populateItem( final Item<DelegateUserRole> pItem )
                    {
                        pItem.add( new Label( "roleCategory" ) );
                        pItem.add( new Label( "roleDescr" ) );
                        pItem.add( new CheckBox( "assigned" ) );
                    }
                }
            );
        }

        @Override
        public void onSubmit()
        {
            final Session session = ( Session ) getSession();

            OracleDelegateUserRoleMgrDAO dataService = null;

            try
            {
                dataService = new OracleDelegateUserRoleMgrDAO( session.getUsername() , session.getPassword() );

                dataService.setUserRoles( userId , roleList );

                info( getLocalizer().getString( "MessageSuccess" , this ) );
            }
            catch ( NothingToDoException ntde )
            {
                error( getLocalizer().getString( "NoWorkToDo" , this ) );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when modifying roles for user '{}' -> {}; error code -> {}; sql state -> {}"
                ,   new Object [ ]
                    {
                        Integer.toString( userId )
                    ,   sqle.getMessage()
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
                    error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
                }
            }
        }
    }
}
