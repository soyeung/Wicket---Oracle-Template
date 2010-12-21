package com.github.wicketoracle.app.data.list.intersection;

import java.sql.SQLException;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wicketoracle.app.data.DataStructure;
import com.github.wicketoracle.app.data.list.AbstractListMgrPage;
import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.form.choice.SelectChoiceList;
import com.github.wicketoracle.html.panel.menu.PostLoginMenuPanelFactory;
import com.github.wicketoracle.session.Session;


public class IntersectionListMgrPage extends AbstractListMgrPage
{
    /** Log */
    private static final Logger LOGGER = LoggerFactory.getLogger( IntersectionListMgrPage.class );

    private final Panel                menuPanel            = PostLoginMenuPanelFactory.getPostLoginMenuPanel();
    private final IntersectionListForm intersectionListForm;
    private final Label                headerLabel;

    public IntersectionListMgrPage( final DataStructure pDataStructure )
    {
        add( menuPanel );

        intersectionListForm = new IntersectionListForm( "FormIntersectionList" , pDataStructure );
        add( intersectionListForm );

        ValueMap headerLabelParams = new ValueMap();
        headerLabelParams.put( "dataStructure" , pDataStructure.getRefdataDescr() );
        headerLabel = new Label( "HeaderIntersectionListMgr" , new StringResourceModel( "HeaderIntersectionListMgr" , this , new Model<ValueMap>( headerLabelParams ) ) );
        add( headerLabel );
    }

    /**
     *
     * @author Andrew Hall
     *
     */
    private final class IntersectionListForm extends Form<Void>
    {
        private static final long serialVersionUID = 1L;

        private final DataStructure dataStructure;

        private final DropDownChoice<IntegerSelectChoice> parentDataDropDownChoice = new DropDownChoice<IntegerSelectChoice>( "parentData" );

        private Palette<IntegerSelectChoice> childDataPalette;

        private SelectChoiceList<IntegerSelectChoice> selectedChildData  = null;
        private SelectChoiceList<IntegerSelectChoice> availableChildData = null;

        /**
         * Retrieve the data used to populate the 'parent' drop down choice
         */
        private void setParentDataDropDownChoice()
        {
            /* Retrieve data to populate the parent data drop down choice */
            final Session session = ( Session ) getSession();

            IntersectionListMgrDAO                dataService = null;
            SelectChoiceList<IntegerSelectChoice> parentData  = null;

            try
            {
                dataService = new IntersectionListMgrDAO( session.getUsername() , session.getPassword() );

                parentData = dataService.getParentData( dataStructure.getRdsId() , dataStructure.getDbrlCode() );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving the parent data for intersection list '{}' -> {}; error code -> {}; sql state -> {}"
                ,   new Object [ ]
                    {
                        dataStructure.getRdsId()
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
                    final String errmsg = getLocalizer().getString( "MessageUnexpectedError" , this );
                    error( errmsg );
                }
            }

            if ( parentData == null )
            {
                parentDataDropDownChoice.setChoices( new SelectChoiceList<IntegerSelectChoice>() );
                parentDataDropDownChoice.setEnabled( false );
            }
            else
            {
                parentDataDropDownChoice.setChoices( parentData ).setChoiceRenderer( parentData ).setModel( new Model<IntegerSelectChoice>( parentData.get( 0 ) ) );
                parentDataDropDownChoice.setNullValid( false ).setRequired( true );
            }
        }

        /**
         * For a given 'parent data' choice, set the current 'available' and 'selected' data sets
         */
        private void refreshAvailableAndSelectedData()
        {
            /* Retrieve data to populate the parent data drop down choice */
            final Session session = ( Session ) getSession();

            IntersectionListMgrDAO                              dataService = null;
            Map<String , SelectChoiceList<IntegerSelectChoice>> childData   = null;

            try
            {
                dataService = new IntersectionListMgrDAO( session.getUsername() , session.getPassword() );

                childData = dataService.getChildData( dataStructure.getRdsId() , dataStructure.getDbrlCode() , parentDataDropDownChoice.getModelObject().getKey() );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving the child data for intersection list '{}' -> {}; error code -> {}; sql state -> {}"
                ,   new Object [ ]
                    {
                        dataStructure.getRdsId()
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
                    final String errmsg = getLocalizer().getString( "MessageUnexpectedError" , this );
                    error( errmsg );
                }
            }

            availableChildData = childData.get( "AVAILABLE" );
            selectedChildData  = childData.get( "SELECTED" );

            if ( availableChildData == null )
            {
                availableChildData = new SelectChoiceList<IntegerSelectChoice>();
            }

            if ( selectedChildData == null )
            {
                selectedChildData = new SelectChoiceList<IntegerSelectChoice>();
            }
        }

        /**
         * Calculate a good height for the 'data palette' based on how much data has to be displayed
         * @return
         */
        private int getPaletteHeight()
        {
            int paletteHeight = 10;
            if ( availableChildData.size() > paletteHeight )
            {
                paletteHeight = availableChildData.size();
            }

            return paletteHeight;
        }

        /**
         * Refresh the palette - this is going to be called when 'parent' choices change, or after changes to available/selected data are made.
         */
        private void refreshChildDataPalette()
        {
            refreshAvailableAndSelectedData();
            childDataPalette.setDefaultModel( new CollectionModel<IntegerSelectChoice>( availableChildData ) );
            childDataPalette.setDefaultModelObject( selectedChildData );
        }

        /**
         *
         * @param pId
         * @param pDataStructure
         */
        public IntersectionListForm( final String pId , final DataStructure pDataStructure )
        {
            super( pId );
            dataStructure = pDataStructure;

            setParentDataDropDownChoice();
            add( parentDataDropDownChoice );

            refreshAvailableAndSelectedData();
            childDataPalette = new Palette<IntegerSelectChoice>
                                   (
                                       "PaletteChildData"
                                   ,   new ListModel<IntegerSelectChoice>( selectedChildData )
                                   ,   new CollectionModel<IntegerSelectChoice>( availableChildData )
                                   ,   new SelectChoiceList<IntegerSelectChoice>()
                                   ,   getPaletteHeight()
                                   ,   true
                                   );
            childDataPalette.setOutputMarkupId( true );
            add( childDataPalette );

            parentDataDropDownChoice.add
            (
                new AjaxFormComponentUpdatingBehavior( "onchange" )
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    protected void onUpdate( final AjaxRequestTarget target )
                    {
                        refreshChildDataPalette();
                        target.addComponent( childDataPalette );
                    }
                }
            );
        }

        /**
         * Apply the changes made on the screen to the database itself
         */
        @Override
        public void onSubmit()
        {
            if ( getIsDebugInfoVisible() )
            {
                info( "Parent data :: Model Value :: " + parentDataDropDownChoice.getModelObject().getKey() );
            }

            final Session session = ( Session ) getSession();

            IntersectionListMgrDAO dataService = null;

            try
            {
                dataService = new IntersectionListMgrDAO( session.getUsername() , session.getPassword() );

                dataService.updateData
                (
                    dataStructure.getRdsId()
                ,   dataStructure.getDbrlCode()
                ,   parentDataDropDownChoice.getModelObject().getKey()
                ,   ( com.github.wicketoracle.html.form.choice.SelectChoiceList<IntegerSelectChoice> ) childDataPalette.getDefaultModelObject()
                );

                dataService.doCommit();

                info( getLocalizer().getString( "MessageSuccess" , this ) );
            }
            catch ( SQLException sqle )
            {
                LOGGER.error
                (
                    "SQL Exception when retrieving the child data for intersection list '{}' -> {}; error code -> {}; sql state -> {}"
                ,   new Object [ ]
                    {
                        dataStructure.getRdsId()
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
                    final String errmsg = getLocalizer().getString( "MessageUnexpectedError" , this );
                    error( errmsg );
                }
            }
        }
    }
}
