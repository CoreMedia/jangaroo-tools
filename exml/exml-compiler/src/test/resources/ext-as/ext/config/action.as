package ext.config {

[ExtConfig(target="ext.Action")]
public class action {

  public function action(config:Object = null) {
    super(config);
  }

  /**
   True to disable all components using this action, false to enable them (defaults to false).
   */
  public native function get disabled():Boolean;

  /**
   * @private
   */
  public native function set disabled(value:Boolean):void;

}
}