package com.github.wicketoracle.app.createuser;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.oracle.dao.AbstractOracleDAO;
import com.github.wicketoracle.oracle.util.CloseResource;

import oracle.jdbc.OracleCallableStatement;


/**
 * Allow a new user to be created via the application.
 *
 * @author Andrew Hall
 *
 */
final class CreateUserDAO extends AbstractOracleDAO
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( CreateUserDAO.class );

    /**
     * Constructor
     *
     * @param pUsername
     *                  The username
     * @param pPassword
     *                  The password
     * @throws SQLException
     */
    public CreateUserDAO( final String pUsername , final String pPassword ) throws SQLException
    {
        super( pUsername , pPassword );
    }

    /**
     *
     * @return reference data required to define a new user
     * @throws SQLException
     */
    public Map <String , SelectChoiceList <IntegerSelectChoice>> getKeyValueRefData() throws SQLException
    {
        setRole( RequiredRoles.ROLE_CREATE_USER );

        return getKeyValueRefData( "app_user" , "pk_standard_user_creation" , "fn_get_list_ref_data" );
    }

    /**
     * Create a new db user registered with the application
     *
     * @param pUsername
     *                  New user's username
     * @param pPassword
     *                  New user's password
     * @param pUtyId
     *                  New user's user type [collection of roles]
     * @param pAurpId
     *                  New user's db profile
     * @throws SQLException
     */
    public void createUser( final String pUsername , final String pPassword , final int pUtyId , final int pAurpId , final int pLngId ) throws SQLException
    {
        final String dbStatement =  " begin "
                                  + "   sys.dbms_application_info.set_module ( module_name => ? , action_name => ? ); "
                                  + "   app_user.pk_standard_user_creation.pr_create_standard_user ( p_username => ? , p_password => ? , p_uty_id => ? , p_aurp_id => ? , p_lng_id => ? ); "
                                  + " end;  ";

        CallableStatement dbCstmt = null;

        try
        {
            LOGGER.debug( "Create new user" );

            setRole( RequiredRoles.ROLE_CREATE_USER );

            LOGGER.debug( "Role set" );

            dbCstmt = ( OracleCallableStatement ) getConnection().prepareCall( dbStatement );

            LOGGER.debug( "DB statement prepared -> {}" , dbStatement );

            dbCstmt.setString( 1 , "PKSTANDARDAPPUSERCREATION" );

            dbCstmt.setString( 2 , "PRCREATESTANDARDUSER" );

            dbCstmt.setString( 3 , pUsername );

            dbCstmt.setString( 4 , pPassword );

            dbCstmt.setInt( 5 , pUtyId );

            dbCstmt.setInt( 6 , pAurpId );

            dbCstmt.setInt( 7 , pLngId );

            LOGGER.debug( "DB params registered : Username -> {} ; UtyId -> {} ; AurpId -> {}" , new Object [ ] { pUsername , pUtyId , pAurpId } );

            dbCstmt.execute();

            LOGGER.debug( "DB statement executed" );
        }
        catch ( SQLException sqle )
        {
            LOGGER.error
            (
                "SQL Exception whilst creating new user -> {}; username -> {}; utyId -> {}; aurpId -> {}; error code -> {}; sql state -> {}"
            ,   new Object [ ]
                {
                    pUsername
                ,   pUtyId
                ,   pAurpId
                ,   sqle.getMessage()
                ,   sqle.getErrorCode()
                ,   sqle.getSQLState()
                }
            );

            throw sqle;
        }
        finally
        {
            CloseResource.close( dbCstmt );
        }
    }
}
