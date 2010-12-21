package com.github.wicketoracle.app.report.logonhistory;

import org.apache.wicket.IClusterable;

import com.github.wicketoracle.html.form.choice.IntegerSelectChoice;
import com.github.wicketoracle.html.panel.Paginator;


final class UserSearchChoices extends Paginator implements IClusterable
{
    private static final long serialVersionUID = 1L;

    private IntegerSelectChoice userId         = new IntegerSelectChoice( 0 );
    private java.util.Date      startDate      = null;
    private java.util.Date      endDate        = null;
    private IntegerSelectChoice recordsPerPage = new IntegerSelectChoice( ALL_RECORDS_ON_PAGE );

    /**
     * Constructor
     */
    public UserSearchChoices()
    {

    }

    public IntegerSelectChoice getUserId()
    {
        IntegerSelectChoice temp;
        if ( userId == null )
        {
            temp = new IntegerSelectChoice( 0 );
        }
        else
        {
            temp = userId;
        }

        return temp;
    }

    public void setUserId( final IntegerSelectChoice pUserId )
    {
        userId = pUserId;
    }

    public java.util.Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate( final java.util.Date pStartDate )
    {
        startDate = pStartDate;
    }

    public java.util.Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate( final java.util.Date pEndDate )
    {
        endDate = pEndDate;
    }

    /**
    *
    * @return
    */
   public IntegerSelectChoice getRecordsPerPage()
   {
       IntegerSelectChoice temp;
       if ( recordsPerPage.getKey() == ALL_RECORDS_ON_PAGE )
       {
           temp = new IntegerSelectChoice( ALL_RECORDS_ON_PAGE );
       }
       else
       {
           temp = recordsPerPage;
       }
       return temp;
   }

   public void setRecordsPerPage( final IntegerSelectChoice pRecordsPerPage )
   {
       if ( pRecordsPerPage != null )
       {
           recordsPerPage = pRecordsPerPage;
           setItemsPerPage( recordsPerPage.getKey() );
       }
   }
}
