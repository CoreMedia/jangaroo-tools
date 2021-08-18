package ext {

[Native]
public class Action extends Base {

  /**
   True to disable all components using this action, false to enable them (defaults to false).
   */
  public native function get disabled():Boolean;

  /**
   * @private
   */
  public native function set disabled(value:Boolean):void;

  /**
   *
   *
   * @param config The configuration options
   */
  public function Action(config:Action = null) {
    super();
  }

  /**
   This Action's initial configuration specification.
   * @see http://docs.sencha.com/ext-js/3-4/#!/api/Ext.Action-property-initialConfig Sencha Docs Ext JS 3.4
   */
  public native function get initialConfig():Action;


}
}
    