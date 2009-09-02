package test{
import ext.Panel;
import my.other.Class;

/**
 * @xtype mypanel
 * @cfg {Boolean/String} propertyOne
 * @cfg {Number} propertyTwo
 */
public class MyPanel extends Panel{
  public function SimpleComponent(config:* = undefined) {
    super(Ext.apply(config, {
      anchor : '100%',
      frame : true,
      collapsible : true,
      draggable : true,
      cls : 'x-portlet'
    }));
  }
}
}