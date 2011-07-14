package exmlparser {

import ext.Ext;
import ext.ComponentMgr;
import ext.MessageBox;
import ext.Panel;

// Do not edit. This is an auto-generated class.

/**
 * This is my <b>TestCompoent</b>
 *
 * <p>This component is created by the xtype 'exmlparser.config.AllElements' / the EXML element &lt;exmlparser.config:AllElements>.</p>
 * <p>See the config class for details.</p>
 *
 * @see exmlparser.config.AllElements
 */
public class AllElements extends ext.Panel {
  public static const xtype:String = "exmlparser.config.AllElements";
  ext.ComponentMgr.registerType(xtype, AllElements);

  /**
   * @param config The configuration options. See the config class for details.
   *
   * @see exmlparser.config.AllElements
   */
  public function AllElements(config:Object = null) {
    super(ext.Ext.apply({
      layout: config.myLayout,
      title: "I am a panel",
      defaults: {layout: "border"},
      layoutConfig: {
        bla: "blub",
        anchor: {style: "test"},
        border: {type: "solid"}
      },
      items: [
        {
          xtype: "button",
          text: "Save"
        },
        {xtype: "editortreepanel"}
      ],
      menu: [
        {
          xtype: "menuitem",
          text: "juhu1"
        },
        {
          xtype: "menuitem",
          text: "juhu2"
        },
        {
          xtype: "menuitem",
          text: "juhu3"
        }
      ],
      tools: [{
        handler: function(x){return ''+x;},
        id: "gear"
      }],
      plugins: [
        {ptype: "aplugin"},
        {ptype: "aplugin"}
      ],
      layout2: {type: "a"}
    }, config));
  }
}
}