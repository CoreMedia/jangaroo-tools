import {mixin} from '@jangaroo/joo/AS3';
import TestMixin from './TestMixin';


 class TestMixinClient {

   constructor(thing:string) {
    this.mix(thing);
  }
}
interface TestMixinClient extends TestMixin{

  /** @inheritDoc */
    mix(thing:string):string;}

mixin(TestMixinClient, TestMixin);

export default TestMixinClient;
