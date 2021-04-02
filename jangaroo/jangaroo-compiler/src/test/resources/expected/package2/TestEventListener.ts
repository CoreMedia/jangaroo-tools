import { asConfig, cast } from "@jangaroo/joo/AS3";
import Panel from "../ext/Panel";
import PanelEvent from "../ext/events/PanelEvent";


class TestEventListener {

  #panel:Panel = cast(Panel,{});
  #panels:Array<Panel> = [];

  #getPanels():Array<Panel> {
    return this.#panels;
  }

  constructor() {const this$=this;
    asConfig(this.#panel).title = "not yet clicked.";
    this.#panel.addEventListener( PanelEvent.FLOPS, (event:PanelEvent):void => {
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
