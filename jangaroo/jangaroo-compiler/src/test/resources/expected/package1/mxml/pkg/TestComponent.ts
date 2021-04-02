import { cast } from "@jangaroo/joo/AS3";
import Exml from "../../../net/jangaroo/ext/Exml";
import TestComponentBase from "./TestComponentBase";
import int from "../../../AS3/int_";
interface TestComponent_ extends TestComponentBase._, Partial<Pick<TestComponent,
  "property_1" |
  "property_2"
>> {
}


class TestComponent<Cfg extends TestComponent._ = TestComponent._> extends TestComponentBase<Cfg>{

  constructor(config:TestComponent._ = null){
    config = Exml.apply({
    property_1: "withDefault"
    },config);
    super( Exml.apply(new TestComponent._({
        emptyText: Exml.asString( "<div class='widget-content-list-empty'>" + TestComponentBase.DEFAULT + "</div>"),
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
declare namespace TestComponent {
  export type _ = TestComponent_;
  export const _: { new(config?: _): _; };
}


export default TestComponent;
