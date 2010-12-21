//To build just Dojo base, you just need an empty file.
dependencies = 
{
   layers :
   [
       {
           name:"../wdojo/wdijit.js"
       ,   dependencies: [
                             "dijit.form.Button"
                         ,   "dijit.form.CheckBox"
                         ,   "dijit.form.FilteringSelect"                         
                         ,   "dijit.form.Form"                         
                         ,   "dijit.form.NumberTextBox"
                         ,   "dijit.form.RadioButton"        
                         ,   "dijit.form.Select"                 
                         ,   "dijit.form.TextBox"
                         ,   "dijit.form.ValidationTextBox"
                         ,   "dijit.layout.BorderContainer"
                         ,   "dijit.layout.ContentPane"
                         ,   "dojox.charting.Chart2D"
                         ,   "dojox.charting.widget.Chart2D"
                         ,   "dojox.charting.themes.PlotKit.red"
                         ,   "dojox.charting.widget.Legend"
                         ]
       }
   ]
   ,
   prefixes :
   [
           [ "dijit", "../dijit" ]
       ,   [ "wdojo", "../wdojo" ]
   ]
}