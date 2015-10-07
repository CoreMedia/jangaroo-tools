package ext.config {

[ExtConfig(target="ext.MenuItem", xtype="menuitem")]
public class menuitem {

  public function menuitem(config:Object = null) {
  }

  /**
   * The text of the label
   */
  public native function get text():String;
  /**
   * @private
   */
  public native function set text(text:String):void;
}
}
