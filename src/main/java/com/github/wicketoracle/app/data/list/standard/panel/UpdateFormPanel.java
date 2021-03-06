package com.github.wicketoracle.app.data.list.standard.panel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.data.DataStructure;
import com.github.wicketoracle.app.data.list.ListDatum;
import com.github.wicketoracle.app.data.list.ListMetaData;
import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.html.form.choice.YesNoChoice;
import com.github.wicketoracle.oracle.exception.SQLExceptionCodes;
import com.github.wicketoracle.session.Session;


public class UpdateFormPanel extends Panel
{
    private static final Logger LOGGER = LoggerFactory.getLogger( UpdateFormPanel.class );

    private static final long serialVersionUID = 1L;

    private UpdateListForm updateListForm;

    public UpdateFormPanel( final String pId , final DataStructure pDataStructure , final ListMetaData pListMetaData )
    {
        super( pId );

        updateListForm = new UpdateListForm( "FormUpdateList" , pDataStructure , pListMetaData );
        add( updateListForm );
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class UpdateListForm extends Form<ListDatum>
    {
        private static final long serialVersionUID = 1L;

        private final DataStructure dataStructure;
        private final ListMetaData  listMetaData;

        private transient Localizer localiser = getLocalizer();

        private final Label maximumItemLengthLabel;

        private final Label itemNameLabel  = new Label( "LabelItemName"  , localiser.getString( "LabelItemName"  , this ) );
        private final Label isVisibleLabel = new Label( "LabelIsVisible" , localiser.getString( "LabelIsVisible" , this ) );
        private final Label itemOrderLabel = new Label( "LabelItemOrder" , localiser.getString( "LabelItemOrder" , this ) );

        private List<ListDatum> listData = new ArrayList<ListDatum>();

        /**
         * Refresh the contents of the list from the data store
         */
        private void refreshListData()
        {
            final Session session = ( Session ) getSession();

            StandardListMgrDAO dataService = null;

            try
            {
                dataService = new StandardListMgrDAO( session.getUsername() , session.getPassword() );

                listData = dataService.getData( dataStructure.getRdsId() , dataStructure.getDbrlCode() );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving update only list data -> {}; error code -> {}; sql state -> {}"
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
                    error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
                }
            }

            if ( listData == null || listData.size() == 0 )
            {
                info( getLocalizer().getString( "MessageNoDataFound" , this ) );
                listData = new ArrayList<ListDatum>();
                setVisible( false );
            }
        }

        /**
         *
         * @param pId
         * @param pDataStructure
         */
        public UpdateListForm( final String pId , final DataStructure pDataStructure , final ListMetaData pListMetaData )
        {
            super( pId );
            dataStructure = pDataStructure;
            listMetaData = pListMetaData;

            ValueMap maximumItemLengthLabelParams = new ValueMap();
            maximumItemLengthLabelParams.put( "maxLength" , listMetaData.getItemNameMaxLength() );
            maximumItemLengthLabel = new Label( "LabelMaximumItemLength" , new StringResourceModel( "MessageMaximumItemLength" , this , new Model<ValueMap>( maximumItemLengthLabelParams ) ) );
            add( maximumItemLengthLabel );

            add( itemNameLabel );
            add( isVisibleLabel );
            add( itemOrderLabel );

            add
            (
                new RefreshingView<ListDatum>( "DataList" )
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected Iterator<IModel<ListDatum>> getItemModels()
                    {
                        return new ModelIteratorAdapter<ListDatum>( listData.iterator() )
                        {
                            @Override
                            protected IModel<ListDatum> model( final ListDatum pObject )
                            {
                                return new CompoundPropertyModel<ListDatum>( pObject );
                            }
                        };
                    }

                    @Override
                    protected void populateItem( final Item<ListDatum> pItem )
                    {
                        RequiredTextField<String> itemNameTextField = new RequiredTextField<String>( "itemName" );
                        itemNameTextField.add( new AttributeModifier( "maxlength" , true , new Model<Integer>( listMetaData.getItemNameMaxLength() ) ) );
                        itemNameTextField.add( new LengthBetweenValidator( 1 , listMetaData.getItemNameMaxLength() ) );

                        pItem.add( itemNameTextField );
                        pItem.add( new YesNoChoice( "itemIsVisible" ) );
                        pItem.add( new RequiredTextField<String>( "itemOrder" ) );
                    }
                }
            );
        }

        /**
         * Apply changes
         */
        @Override
        public void onSubmit()
        {
            final Session session = ( Session ) getSession();

            StandardListMgrDAO dataService = null;

            try
            {
                dataService = new StandardListMgrDAO( session.getUsername() , session.getPassword() );

                dataService.updateData( dataStructure.getRdsId() , dataStructure.getDbrlCode() , listData );

                dataService.doCommit();

                info( getLocalizer().getString( "MessageSuccess" , this ) );
            }
            catch ( NothingToDoException ntde )
            {
                error( getLocalizer().getString( "NoWorkToDo" , this ) );
            }
            catch ( SQLException sqle )
            {
                switch( sqle.getErrorCode() )
                {
                    case SQLExceptionCodes.OPTIMISTIC_LOCKING_VIOLATION:
                        error( getLocalizer().getString( "MessageOptimisticLockingError" , this ) );
                        break;

                    case SQLExceptionCodes.UNIQUE_CONSTRAINT_VIOLATION :
                        error( getLocalizer().getString( "MessageDuplicateItem" , this ) );
                        break;

                    default :
                        LOGGER.error
                        (
                            "SQL Exception when retrieving update only list data -> {}; error code -> {}; sql state -> {}"
                        ,   new Object [ ]
                            {
                                sqle.getMessage()
                            ,   sqle.getErrorCode()
                            ,   sqle.getSQLState()
                            }
                        );
                        error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
                        break;
                }
            }
            finally
            {
                if ( ! dataService.closeConnection() )
                {
                    error( getLocalizer().getString( "MessageUnexpectedError" , this ) );
                }
            }
        }

        /**
         * Refresh the data prior to loading the page
         */
        @Override
        public void onBeforeRender()
        {
            refreshListData();
            super.onBeforeRender();
        }
    }
}
