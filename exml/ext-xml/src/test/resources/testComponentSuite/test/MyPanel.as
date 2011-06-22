package test{
import ext.Panel;
import my.other.Class;

/**
 * Class documentation.
 */
public class MyPanel extends Panel{

  /**
   * xtype documentation.
   */
  public static const xtype:String = "mypanel";

  /**
   * Constructor documentation.
   * @cfg {Boolean/String} propertyOne The first property.
   *   With a second documentation line.
   * @cfg {Number} propertyTwo The second property.
   * @cfg {String} propertyThree The third property, immediately following the second.
   */
  public function MyPanel(config:* = undefined) {
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