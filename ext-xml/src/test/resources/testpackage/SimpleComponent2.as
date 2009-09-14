package testpackage{
import ext.Panel;
import my.other.Class;

/**
 * @cfg {Boolean/String} propertyOne
 * @cfg {Number} propertyTwo
 */
public class SimpleComponent2 extends Panel{

  public static const xtype:String = "SimpleComponent2";

  public function SimpleComponent2(config:* = undefined) {
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