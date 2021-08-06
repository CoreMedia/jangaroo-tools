import Config from "@jangaroo/runtime/AS3/Config";
import { cast } from "@jangaroo/runtime/AS3";
import int from "../../../AS3/int_";
import Exml from "../../../net/jangaroo/ext/Exml";
import TestComponentBase from "./TestComponentBase";
interface TestComponentConfig extends Config<TestComponentBase>, Partial<Pick<TestComponent,
  "property_1" |
  "property_2"
>> {
}


class TestComponent extends TestComponentBase{
  declare Config: TestComponentConfig;

  constructor(config:Config<TestComponent> = null){
    config = Exml.apply({
    property_1: "withDefault"
    },config);
    super( Exml.apply(Config(TestComponent, {
        emptyText:  "<div class='widget-content-list-empty'>" + TestComponentBase.DEFAULT + "</div>",
        letters: [
              "a",
              "b",
              "c"
             ]

}),config));
  }

  #property_1:string = null;

  get property_1():string { return this.#property_1; }
  set property_1(value:string) { this.#property_1 = value; }

  #property_2:int = 0;


  get property_2():int { return this.#property_2; }
  set property_2(value:int) { this.#property_2 = value; }}
export default TestComponent;
