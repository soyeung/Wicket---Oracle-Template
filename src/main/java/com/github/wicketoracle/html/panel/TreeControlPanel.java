package com.github.wicketoracle.html.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.AbstractTree;

public class TreeControlPanel extends Panel
{
    private static final long serialVersionUID = 1L;

    private AjaxLink<Void> collapseAllLink;
    private AjaxLink<Void> expandAllLink;

    public TreeControlPanel( final String pId , final AbstractTree pTree )
    {
        super( pId );

        collapseAllLink = new AjaxLink<Void>( "LinkCollapseAll" )
                              {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public void onClick( final AjaxRequestTarget pTarget )
                                  {
                                      pTree.getTreeState().collapseAll();
                                      pTree.updateTree();
                                  }
                              };
        expandAllLink   = new AjaxLink<Void>( "LinkExpandAll" )
                              {
                                  private static final long serialVersionUID = 1L;

                                  @Override
                                  public void onClick( final AjaxRequestTarget pTarget )
                                  {
                                      pTree.getTreeState().expandAll();
                                      pTree.updateTree();
                                  }
                              };

        add( collapseAllLink );
        add( expandAllLink );
    }
}
