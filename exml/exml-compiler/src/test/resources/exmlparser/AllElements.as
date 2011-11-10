package exmlparser {

import ext.Ext;
import ext.ComponentMgr;
import exmlparser.config.allElements;
import ext.MessageBox;
import ext.Panel;

// Do not edit. This is an auto-generated class.

/**
 * This is my <b>TestCompoent</b>
 *
 * <p>This component is created by the xtype 'exmlparser.config.allElements' / the EXML element &lt;exmlparser.config:allElements>.</p>
 * <p>See the config class for details.</p>
 *
 * @see exmlparser.config.allElements
 */
public class AllElements extends ext.Panel {
  /**
   * This is my <b>constant</b>
   */
  public static const SOME_CONSTANT:String = exmlparser.config.allElements.SOME_CONSTANT;

  /**
   * Create a AllElements.
   * @param config The configuration options. See the config class for details.
   *
   * @see exmlparser.AllElements
   * @see exmlparser.config.allElements
   */
  public function AllElements(config:exmlparser.config.allElements = null) {
    super(exmlparser.config.allElements(ext.Ext.apply({
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
    }, config)));
  }

  /**
   * Create a AllElements.
   * @param config The configuration options. See the config class for details.
   *
   * @see exmlparser.AllElements
   * @see exmlparser.config.allElements
   */
  public static function main(config:exmlparser.config.allElements = null):void {
    new exmlparser.AllElements(config);
  }
}
}
