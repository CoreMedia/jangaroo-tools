package ext {

[Native("Ext.MenuItem", require)]
public class MenuItem extends Component {

  public function MenuItem(config:Object = null) {
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
