import Config from "@jangaroo/runtime/Config";
import { asConfig } from "@jangaroo/runtime";
import Component from "../Ext/Component";
import Panel from "../Ext/Panel";


class TestEventListener {

  #panel:Config<Panel> = Config(Panel);
  #panels:Array<Panel> = [];

  #getPanels():Array<Panel> {
    return this.#panels;
  }

  constructor() {
    this.#panel.title = "not yet clicked.";
    this.#panel.addListener("flipflop", (source: Component):void => {
      const event = { source };
      asConfig(this.getThis().getPanel()).title = "clicked!";
      asConfig(this.#panel.layout.getOwner()).title = "clicked!";
      this.#panels.push(event.source);
      asConfig(this.#getPanels()[0]).title = "yes, clicked!";
    } );
  }

  getThis():TestEventListener {
    return this;
  }

  getPanel():Panel {
    return this.#panel;
  }
}
export default TestEventListener;
