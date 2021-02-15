import {mixin} from '@jangaroo/joo/AS3';
import TestComponent from './TestComponent';
import TestInterface from './TestInterface';
import int from '../../../AS3/int_';

class TestComponentBaseProperties {

   emptyText:string = null;
   letters:Array<any> = null;}

class TestComponentBaseConfigs {}
interface TestComponentBase_ extends Partial<TestComponentBaseProperties>, Partial<TestComponentBaseConfigs> {
}




 class TestComponentBase<Cfg extends TestComponent._ = TestComponent._> implements TestInterface {

   static readonly DEFAULT:string = "_DEFAULT_";

   #property_1:string = null;
   #property_2:int = 0;

   constructor(config:TestComponent._ = null) {
    this.#property_1 = config.property_1 += "_HI";
    this.#property_2 = config.property_2 || 0;
  }

   #component:any = null;

   init(component:any):void {
    this.#component = component;
  }
}
interface TestComponentBase<Cfg extends TestComponent._ = TestComponent._>extends TestComponentBaseProperties{}

mixin(TestComponentBase, TestInterface);

declare namespace TestComponentBase {
  export type _ = TestComponentBase_;
  export const _: { new(config?: _): _; };
}


export default TestComponentBase;
