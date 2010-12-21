package com.github.wicketoracle.app.data.list;

import java.sql.SQLException;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.data.RequiredRoles;
import com.github.wicketoracle.html.page.StandardPage;
import com.github.wicketoracle.session.Session;


@AuthorizeInstantiation( RequiredRoles.ROLE_REF_DATA_MGR )
public abstract class AbstractListMgrPage extends StandardPage
{

    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractListMgrPage.class );

    public final ListMetaData getListMetaData( final int pRdsId )
    {
        ListMetaData           metaData    = null;
        AbstractListMgrDAO dataService = null;

        try
        {
            final Session session = ( Session ) getSession();
            dataService = new AbstractListMgrDAO( session.getUsername() , session.getPassword() ) { };

            metaData = dataService.getMetaData( pRdsId );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception while retrieving list meta data -> {}; error code -> {}; sql state -> {}"
            ,   new Object[]
                {
                    sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );
        }
        finally
        {
            if ( ! dataService.closeConnection() )
            {
                error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
            }
        }

        return metaData;
    }
}
