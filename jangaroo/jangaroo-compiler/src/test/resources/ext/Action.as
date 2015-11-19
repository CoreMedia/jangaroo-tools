package ext {
import ext.config.action;

[Native]
[DefaultProperty("config")]
public class Action {

  /**
   * @private
   */
  public native function set config(value:ext.config.action):void;

  /**
   *
   *
   * @param config The configuration options
   * @see ext.config.action
   */
  public function Action(config:ext.config.action = null) {
    super();
  }

  /**
   This Action's initial configuration specification.
   * @see http://docs.sencha.com/ext-js/3-4/#!/api/Ext.Action-property-initialConfig Sencha Docs Ext JS 3.4
   */
  public native function get initialConfig():Object;


}
}
    