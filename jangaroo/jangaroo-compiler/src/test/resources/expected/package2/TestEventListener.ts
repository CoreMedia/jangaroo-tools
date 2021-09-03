import Config from "@jangaroo/runtime/AS3/Config";
import { asConfig } from "@jangaroo/runtime/AS3";
import Panel from "../Ext/Panel";


class TestEventListener {

  #panel:Config<Panel> = Config(Panel);
  #panels:Array<Panel> = [];

  #getPanels():Array<Panel> {
    return this.#panels;
  }

  constructor() {
    this.#panel.title = "not yet clicked.";
    this.#panel.addListener("flipflop", ():void => {
      asConfig(this.getThis().getPanel()).title = "clicked!";
      asConfig(this.#panel.layout.getOwner()).title = "clicked!";
      this.#panels.push(this.#panel);
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
