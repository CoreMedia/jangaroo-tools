package exmlparser {

import ext.Ext;
import ext.ComponentMgr;
import ext.MessageBox;
import ext.Panel;
import ext.config.button;
import ext.config.menuitem;

//Do not edit. This is an auto-generated class.

/**
 * <p>This component is created by the xtype 'exmlparser.config.AllElements' / the EXML element &lt;exmlparser.config:AllElements>.</p>
 * <p>See the config class for details.</p>
 *
 * @see exmlparser.config.AllElements
 */
public class AllElements extends ext.Panel {
  public static const xtype:String = "exmlparser.config.AllElements";
  ext.ComponentMgr.registerType(xtype, AllElements);

  /**
   * @param config <p>The configuration options. See the config class for details.</p>
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
        new ext.config.button({text: "Save"}),
        {xtype: "editortreepanel"}
      ],
      menu: [
        new ext.config.menuitem({text: "juhu1"}),
        new ext.config.menuitem({text: "juhu2"}),
        new ext.config.menuitem({text: "juhu3"})
      ],
      tools: [{
        handler: function(x){return ''+x;},
        id: "gear"
      }]
    }, config));
  }
}
}