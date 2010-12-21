package com.github.wicketoracle.app.ucp.panel;

import oracle.ucp.UniversalConnectionPoolException;

import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.oracle.ucp.UCPMgr;


public class UCPStateMgrPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger( UCPStateMgrPanel.class );

    private UCPStateMgrForm ucpStateMgrForm = new UCPStateMgrForm( "ucpStateMgrForm" );

    public UCPStateMgrPanel( final String pId )
    {
        super( pId );
        add( ucpStateMgrForm );
    }

    private final class UCPStateMgrForm extends Form<UCPStateChoice>
    {
        private static final long serialVersionUID = 1L;

        private UCPStateChoice ucpStateBean = new UCPStateChoice();

        private final RadioGroup<UCPState> ucpStateRadioGroup = new RadioGroup<UCPState>( "ucpStateRadioGroup" , new PropertyModel<UCPState>( ucpStateBean , "ucpState" ) );
        private final Radio<UCPState>      purgeRadio         = new Radio<UCPState>( "purgeRadio"   , new Model<UCPState>( UCPState.PURGE ) );
        private final Radio<UCPState>      recycleRadio       = new Radio<UCPState>( "recycleRadio" , new Model<UCPState>( UCPState.RECYCLE ) );
        private final Radio<UCPState>      refreshRadio       = new Radio<UCPState>( "refreshRadio" , new Model<UCPState>( UCPState.REFRESH ) );

        public UCPStateMgrForm( final String pId )
        {
            super( pId );
            ucpStateRadioGroup.add( purgeRadio );
            ucpStateRadioGroup.add( recycleRadio );
            ucpStateRadioGroup.add( refreshRadio );
            add( ucpStateRadioGroup );
        }

        public void onSubmit()
        {
            final Localizer localiser = getLocalizer();

            try
            {
                switch( ucpStateBean.getUcpState() )
                {
                    case PURGE:
                        UCPMgr.getUCPPoolMgr().purgeConnectionPool( UCPMgr.getUCPDataSource().getConnectionPoolName() );
                    case REFRESH:
                        UCPMgr.getUCPPoolMgr().refreshConnectionPool( UCPMgr.getUCPDataSource().getConnectionPoolName() );
                    case RECYCLE:
                        UCPMgr.getUCPPoolMgr().recycleConnectionPool( UCPMgr.getUCPDataSource().getConnectionPoolName() );
                    default:
                        assert ( false );
                }

                info( localiser.getString( "MessageSuccess" , this ) );
            }
            catch ( UniversalConnectionPoolException ucpe )
            {
                LOGGER.error
                (
                    "Universal Connection Pool Exception when changing the UCP state -> {} ; error message -> {} ; error code -> {}"
                ,   new Object[]
                    {
                        ucpe.getMessage()
                    ,   ucpe.getErrorCode()
                    }
                );
                info( localiser.getString( "MessageUnexpectedError" , this ) );
            }
        }
    }
}
