package testpackage{
import ext.Panel;
import my.other.Class;

/**
 * This is some class documentation
 */
public class SimpleComponent extends Panel{

  public static const xtype:String = "SimpleComponent";

  /**
   * @cfg {Boolean/String} propertyOne
   * @cfg {Number} propertyTwo
   * @param config
   */
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