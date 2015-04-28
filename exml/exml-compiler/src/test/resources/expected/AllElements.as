package exmlparser {

import ext.Ext;
import net.jangaroo.ext.Exml;
import exmlparser.config.allElements;
import ext.MessageBox;
import ext.config.component;
import ext.Panel;

// Do not edit. This is an auto-generated class.

/**
 * This is my <b>TestComponent</b>
 *
 * <p>This component is created by the xtype <code>'exmlparser.config.allElements'</code> / the EXML element <code>&lt;ec:allElements></code>
 * with <code>xmlns:ec="exml:exmlparser.config"</code>.</p>
 * <p>See the config class for details.</p>
 *
 * @see exmlparser.config.allElements
 */
public class AllElements extends ext.Panel {
  /**
   * This is my <b>constant</b>
   */
  public static const SOME_CONSTANT:uint = exmlparser.config.allElements.SOME_CONSTANT;
  /**
   * This is another <b>constant</b>
   */
  public static const ANOTHER_CONSTANT:String = exmlparser.config.allElements.ANOTHER_CONSTANT;
  /**
   * 
   */
  public static const CODE_CONSTANT:int = exmlparser.config.allElements.CODE_CONSTANT;

  /**
   * Create a AllElements.
   * @param config The configuration options. See the config class for details.
   *
   * @see exmlparser.AllElements
   * @see exmlparser.config.allElements
   */
  public function AllElements(config:exmlparser.config.allElements = null) {
    var myVar:String = config.myProperty + '_suffix';
    var myVar2:Object = {
      prop: config.myProperty
    };
    var myVar3:ext.config.component = ext.config.component({
      xtype: "button",
      text: "Foo"
    });
    super(exmlparser.config.allElements(net.jangaroo.ext.Exml.apply({
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
          text: "Save",
          handler: function():void {
          window.alert('gotcha!');
        }
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
