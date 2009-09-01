package testpackage{
import ext.Panel;
import my.other.Class;

/**
 * @xtype simplecomponent
 * @cfg {Boolean/String} propertyOne
 * @cfg {Number} propertyTwo
 */
public class SimpleComponent extends Panel{
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