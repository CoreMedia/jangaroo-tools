import Config from "@jangaroo/runtime/Config";
import { asConfig, mixin } from "@jangaroo/runtime";
import int from "../../../AS3/int_";
import Component from "../../../Ext/Component";
import TestComponent from "./TestComponent";
import TestInterface from "./TestInterface";
interface TestComponentBaseConfig extends Config<Component>, Partial<Pick<TestComponentBase,
  "emptyText" |
  "letters"
>> {
}




class TestComponentBase extends Component implements TestInterface {
  declare Config: TestComponentBaseConfig;

  static readonly DEFAULT:string = "_DEFAULT_";

  emptyText:string = null;
  letters:Array<any> = null;

  #property_1:string = null;
  #property_2:int = 0;

  constructor(config:Config<TestComponent> = null) {
    super(config);
    this.#property_1 = config.property_1 + "_HI";
    this.#property_2 = config.property_2 || 0;
  }

  #component:any = null;

  init(component:any):void {
    this.#component = component;
  }
}
mixin(TestComponentBase, TestInterface);

export default TestComponentBase;
