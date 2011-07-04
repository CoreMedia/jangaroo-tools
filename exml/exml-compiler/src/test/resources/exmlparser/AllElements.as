package exmlparser {

import ext.ComponentMgr;
import ext.MessageBox;
import ext.config.panel;
import ext.config.button;
import ext.config.menuitem;

/**
 * <b>Do not edit. This is an auto-generated class.</b>
 */
public class AllElements extends ext.config.panel {
  public function AllElements(config:Object = null) {
    super(Ext.apply({
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
    }, config);
  }
}
}