import {mixin} from '@jangaroo/joo/AS3';
interface TestMixin_ {
}


 class TestMixin<Cfg extends TestMixin._ = TestMixin._> {

   mix(thing:string):string {
    return "Mixed " + thing + "!";
  }
}
declare namespace TestMixin {
  export type _ = TestMixin_;
  export const _: { new(config?: _): _; };
}


export default TestMixin;
