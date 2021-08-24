package ext {

[Native("Ext.Label", require)]
public class Label {

  public function Label(config:Object = null) {
  }

  /**
   * The text of the label
   */
  public native function get text():String;
  /**
   * @private
   */
  public native function set items(text:String):void;
}
}
