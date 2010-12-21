package com.github.wicketoracle.app.data.list.coded.panel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.data.DataStructure;
import com.github.wicketoracle.app.data.list.ListDatum;
import com.github.wicketoracle.app.data.list.ListMetaData;
import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.html.form.choice.StringSelectChoice;
import com.github.wicketoracle.html.form.choice.YesNoChoice;
import com.github.wicketoracle.oracle.exception.SQLExceptionCodes;
import com.github.wicketoracle.session.Session;


public class InsertFormPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger( InsertFormPanel.class );

    private InsertForm insertForm;

    public InsertFormPanel( final String pId , final DataStructure pDataStructure , final ListMetaData pListMetaData )
    {
        super( pId );

        insertForm = new InsertForm( "FormInsert" , pDataStructure , pListMetaData );
        add( insertForm  );
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class InsertForm extends Form<ListDatum>
    {
        private static final long serialVersionUID = 1L;

        private final ListDatum listDatum = new ListDatum();

        private final DataStructure dataStructure;
        private final ListMetaData  listMetaData;

        private final Label maximumCodeLengthLabel;
        private final Label maximumItemLengthLabel;

        private transient Localizer localiser = getLocalizer();

        private final Label itemCodeLabel  = new Label( "LabelItemCode"  , localiser.getString( "LabelItemCode"  , this ) );
        private final Label itemNameLabel  = new Label( "LabelItemName"  , localiser.getString( "LabelItemName"  , this ) );
        private final Label isVisibleLabel = new Label( "LabelIsVisible" , localiser.getString( "LabelIsVisible" , this ) );
        private final Label itemOrderLabel = new Label( "LabelItemOrder" , localiser.getString( "LabelItemOrder" , this ) );

        private final RequiredTextField<String> itemCodeTextField           = new RequiredTextField<String>( "code"      , new PropertyModel<String>( listDatum , "code" ) );
        private final RequiredTextField<String> itemNameTextField           = new RequiredTextField<String>( "itemName"  , new PropertyModel<String>( listDatum , "itemName" ) );
        private final YesNoChoice               itemIsVisibleDropDownChoice = new YesNoChoice( "itemIsVisible" , new PropertyModel<StringSelectChoice>( listDatum , "itemIsVisible" ) );
        private final RequiredTextField<String> itemOrderTextField          = new RequiredTextField<String>( "itemOrder" , new PropertyModel<String>( listDatum , "itemOrder" ) );

        /**
         *
         * @param pId
         * @param pDataStructure
         */
        public InsertForm( final String pId , final DataStructure pDataStructure , final ListMetaData pListMetaData )
        {
            super( pId );
            dataStructure = pDataStructure;
            listMetaData = pListMetaData;

            ValueMap maximumCodeLengthLabelParams = new ValueMap();
            maximumCodeLengthLabelParams.put( "maxLength" , listMetaData.getCodeMaxLength() );
            maximumCodeLengthLabel = new Label( "LabelMaximumCodeLength" , new StringResourceModel( "MessageMaximumCodeLength" , this , new Model<ValueMap>( maximumCodeLengthLabelParams ) ) );
            add( maximumCodeLengthLabel );

            ValueMap maximumItemLengthLabelParams = new ValueMap();
            maximumItemLengthLabelParams.put( "maxLength" , listMetaData.getItemNameMaxLength() );
            maximumItemLengthLabel = new Label( "LabelMaximumItemLength" , new StringResourceModel( "MessageMaximumItemLength" , this , new Model<ValueMap>( maximumItemLengthLabelParams ) ) );
            add( maximumItemLengthLabel );

            add( itemCodeLabel );
            add( itemNameLabel );
            add( isVisibleLabel );
            add( itemOrderLabel );

            add( itemCodeTextField );
            add( itemNameTextField );
            add( itemIsVisibleDropDownChoice );
            add( itemOrderTextField );
        }

        /**
         * Apply changes
         */
        @Override
        public void onSubmit()
        {
            final Session session = ( Session ) getSession();

            CodedListMgrDAO dataService = null;

            try
            {
                dataService = new CodedListMgrDAO( session.getUsername() , session.getPassword() );

                List<ListDatum> listData = new ArrayList<ListDatum>( 1 );
                listData.add( listDatum );

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
    }
}
