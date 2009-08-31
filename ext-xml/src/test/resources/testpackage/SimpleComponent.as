package testpackage{
import ext.Panel;

/**
 * @class testpackage.SimpleComponent
 * @xtype simplecomponent
 * @extends ext.Panel
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