import {cast} from '@jangaroo/joo/AS3';
import Panel from '../ext/Panel';
import PanelEvent from '../ext/events/PanelEvent';


 class TestEventListener {

   #panel:Panel = cast(Panel,{});
  
   #panels:Array<Panel> = [];

  
  //@ts-expect-error 18022
   #getPanels():Array<Panel> {
    return this.#panels;
  }

   constructor() {const this$=this;
    this.#panel.setConfig("title" , "not yet clicked.");
    this.#panel.addEventListener( PanelEvent.FLOPS, (event:PanelEvent):void => {
      this.getThis().getPanel().setConfig("title" , "clicked!");
      this.#panel.layout.getOwner().setConfig("title" , "clicked!");
      this.#panels.push(this.#panel);
      this.#getPanels()[0].setConfig("title" , "yes, clicked!");
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
