package ext.config {

[ExtConfig(target="ext.layout.borderlayout.Region")]
public class someMixin {

  public function someMixin(config:Object = null) {
    super(config);
  }


  public native function get someList():Array;

  /**
   * @private
   */
  [ExtConfig(mode="append")]
  public native function set someList(value:Array):void;

}
}
