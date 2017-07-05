package ext {

[Native]
public class Action {

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
    