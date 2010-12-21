package com.github.wicketoracle.app.data.list.subdivision;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
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

import com.github.wicketoracle.app.data.DataStructure;
import com.github.wicketoracle.app.data.list.AbstractListMgrPage;
import com.github.wicketoracle.app.data.list.ListDatum;
import com.github.wicketoracle.app.exception.NothingToDoException;
import com.github.wicketoracle.html.form.choice.YesNoChoice;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


public class SubdivisionListMgrPage extends AbstractListMgrPage
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SubdivisionListMgrPage.class );

    private final Panel               menuPanel           = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final Label               headerLabel;
    private final SubdivisionListForm subdivisionListForm;

    public SubdivisionListMgrPage( final DataStructure pDataStructure )
    {
        add( menuPanel );

        ValueMap headerLabelParams = new ValueMap();
        headerLabelParams.put( "dataStructure" , pDataStructure.getRefdataDescr() );
        headerLabel = new Label( "HeaderSubdivisionListMgr" , new StringResourceModel( "HeaderSubdivisionListMgr" , this , new Model<ValueMap>( headerLabelParams ) ) );
        add( headerLabel );

        subdivisionListForm = new SubdivisionListForm( "FormSubdivisionList" , pDataStructure );
        add( subdivisionListForm );
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class SubdivisionListForm extends Form<ListDatum>
    {
        private static final long serialVersionUID = 1L;

        private transient Localizer localiser = getLocalizer();

        private final DataStructure dataStructure;

        private final Label itemNameLabel    = new Label( "LabelItemName"    , localiser.getString( "LabelItemName"    , this ) );
        private final Label includeItemLabel = new Label( "LabelIncludeItem" , localiser.getString( "LabelIncludeItem" , this ) );

        private List<SubdivisionDatum> listData = new ArrayList<SubdivisionDatum>();

        /**
         * Refresh the contents of the list from the data store
         */
        private void refreshListData()
        {
            final Session session = ( Session ) getSession();

            SubdivisionListMgrDAO dataService = null;

            try
            {
                dataService = new SubdivisionListMgrDAO( session.getUsername() , session.getPassword() );

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
                listData = new ArrayList<SubdivisionDatum>();
                setVisible( false );
            }
        }

        /**
         *
         * @param pId
         * @param pDataStructure
         */
        public SubdivisionListForm( final String pId , final DataStructure pDataStructure )
        {
            super( pId );
            dataStructure = pDataStructure;

            add( itemNameLabel );
            add( includeItemLabel );

            add
            (
                new RefreshingView<SubdivisionDatum>( "DataList" )
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected Iterator<IModel<SubdivisionDatum>> getItemModels()
                    {
                        return new ModelIteratorAdapter<SubdivisionDatum>( listData.iterator() )
                        {
                            @Override
                            protected IModel<SubdivisionDatum> model( final SubdivisionDatum pObject )
                            {
                                return new CompoundPropertyModel<SubdivisionDatum>( pObject );
                            }
                        };
                    }

                    @Override
                    protected void populateItem( final Item<SubdivisionDatum> pItem )
                    {
                        pItem.add( new Label( "itemName" ) );
                        pItem.add( new YesNoChoice( "includeItem" ) );
                    }
                }
            );
        }

        @Override
        public void onSubmit()
        {
            final Session session = ( Session ) getSession();

            SubdivisionListMgrDAO dataService = null;

            try
            {
                LOGGER.debug( "Began the form submission process " );

                dataService = new SubdivisionListMgrDAO( session.getUsername() , session.getPassword() );

                LOGGER.debug( "SubdivisionListMgrDAO constructed" );

                dataService.updateData( dataStructure.getRdsId() , dataStructure.getDbrlCode() , listData );

                LOGGER.debug( "Data updated" );

                dataService.doCommit();

                LOGGER.debug( "Changes committed" );

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
                     "SQL Exception when changing subdivision data -> {}; error code -> {}; sql state -> {}"
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
