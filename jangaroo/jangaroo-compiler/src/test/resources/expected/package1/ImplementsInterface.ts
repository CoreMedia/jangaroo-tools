import { asConfig, mixin } from "@jangaroo/runtime/AS3";
import Panel from "../ext/Panel";
import Interface from "./Interface";


class ImplementsInterface implements Interface {

  /**
   * Field with ASDoc.
   * Second line.
   */
  foo;
  
  /**
   * Annotated field with ASDoc.
   */
  bar:Array<Array<Panel>> = null;

  constructor() {
    // nothing really
  }

  doSomething():string {
    this.bar = new Array();
    var panels = new Array();
    panels.push(new Panel({}));
    this.bar.push(panels);
    asConfig(this.bar[0][0]).title = "Gotcha!";
  }

   get property():string {
    return "prefix" + this.foo;
  }

   set property(value:string) {
    this.foo = value.substr("prefix".length);
  }
}
mixin(ImplementsInterface, Interface);

export default ImplementsInterface;
