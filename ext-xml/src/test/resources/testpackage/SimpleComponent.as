package testpackage{
import ext.Panel;
import my.other.Class;

/**
 * This is some class documentation
 * with a new
 * line
 */
public class SimpleComponent extends Panel{

  public static const xtype:String = "SimpleComponent";

  /**
   * @cfg {Boolean/String} propertyOne
   * here we also have some documentation
   * @cfg {Number} propertyTwo
   * some other documentation
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