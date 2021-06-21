import { asConfig, mixin } from "@jangaroo/runtime/AS3";
import int from "../../../AS3/int_";
import TestComponent from "./TestComponent";
import TestInterface from "./TestInterface";
interface TestComponentBase_ extends Partial<Pick<TestComponentBase,
  "emptyText" |
  "letters"
>> {
}




class TestComponentBase implements TestInterface {
  declare readonly initialConfig: TestComponent._;

  static readonly DEFAULT:string = "_DEFAULT_";

  emptyText:string = null;
  letters:Array<any> = null;

  #property_1:string = null;
  #property_2:int = 0;

  constructor(config:TestComponent._ = null) {
    this.#property_1 = config.property_1 + "_HI";
    this.#property_2 = config.property_2 || 0;
  }

  #component:any = null;

  init(component:any):void {
    this.#component = component;
  }
}
mixin(TestComponentBase, TestInterface);

declare namespace TestComponentBase {
  export type _ = TestComponentBase_;
  export const _: (config?: _) => _;
}


export default TestComponentBase;
