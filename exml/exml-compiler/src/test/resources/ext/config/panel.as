package ext.config {

[ExtConfig(target="Ext.Panel")]
public class panel {
  /**
   * Some Boolean property
   */
  public native function get propertyOne():Boolean;
  /**
   * @private
   */
  public native function set propertyOne(value:Boolean):void;
}
}