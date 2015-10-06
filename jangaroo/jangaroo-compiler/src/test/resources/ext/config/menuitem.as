package ext.config {

[ExtConfig(target="ext.MenuItem", xtype="menuitem")]
public class menuitem {

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
