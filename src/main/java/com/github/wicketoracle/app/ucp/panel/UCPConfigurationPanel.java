package com.github.wicketoracle.app.ucp.panel;

import java.sql.SQLException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.github.wicketoracle.oracle.exception.SQLExceptionCodes;
import com.github.wicketoracle.oracle.ucp.UCPMgr;


public class UCPConfigurationPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private UCPConfigurationForm ucpConfigurationForm = new UCPConfigurationForm( "ucpConfigurationForm" );

    /**
     *
     * @param pId
     */
    public UCPConfigurationPanel( final String pId )
    {
        super( pId );
        add( ucpConfigurationForm );
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class UCPConfigurationForm extends Form<UCPConfiguration>
    {
        private static final long serialVersionUID = 1L;

        private UCPConfiguration ucpConfiguration = new UCPConfiguration( UCPMgr.getUCPDataSource() );

        private Label urlLabel                    = new Label( "LabelURLValue"                    , ucpConfiguration.getUrl() );
        private Label poolNameLabel               = new Label( "LabelPoolNameValue"               , ucpConfiguration.getConnectionPoolName() );
        private Label connectionFactoryClassLabel = new Label( "LabelConnectionFactoryClassValue" , ucpConfiguration.getConnectionfactoryClassName() );

        private TextField<Integer> maxCachedStatements  = new TextField<Integer>( "maxCachedStatements" , new PropertyModel<Integer>( ucpConfiguration, "maxCachedStatements" ) );
        private TextField<Integer> minPoolSizeTextField = new TextField<Integer>( "minPoolSize" , new PropertyModel<Integer>( ucpConfiguration, "minPoolSize" ) );
        private TextField<Integer> maxPoolSizeTextField = new TextField<Integer>( "maxPoolSize" , new PropertyModel<Integer>( ucpConfiguration, "maxPoolSize" ) );

        /**
         *
         * @param pId
         */
        public UCPConfigurationForm( final String pId )
        {
            super( pId );
            add( urlLabel );
            add( poolNameLabel );
            add( connectionFactoryClassLabel );
            add( maxCachedStatements );
            add( minPoolSizeTextField );
            add( maxPoolSizeTextField );
        }

        /**
         *
         */
        @Override
        public void onSubmit()
        {
            try
            {
                UCPMgr.configure
                (
                    ucpConfiguration.getMinPoolSize()
                ,   ucpConfiguration.getMaxPoolSize()
                ,   ucpConfiguration.getMaxCachedStatements()
                );

                info( getLocalizer().getString( "MessageSuccess" , this ) );
            }
            catch ( SQLException sqle )
            {
                ucpConfiguration = new UCPConfiguration( UCPMgr.getUCPDataSource() );

                switch( sqle.getErrorCode() )
                {
                    case SQLExceptionCodes.UCP_ILLEGAL_MIN_POOL_SIZE :
                        error( getLocalizer().getString( "MessageIllegalMinPoolSize" , this ) );
                        break;

                    case SQLExceptionCodes.UCP_ILLEGAL_MAX_POOL_SIZE :
                        error( getLocalizer().getString( "MessageIllegalMaxPoolSize" , this ) );
                        break;

                    case SQLExceptionCodes.UCP_ILLEGAL_MAX_CACHED_STATEMENTS :
                        error( getLocalizer().getString( "MessageIllegalMaxCachedStatements" , this ) );
                        break;

                    default :
                        error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
                        break;
                }
            }
        }
    }
}
