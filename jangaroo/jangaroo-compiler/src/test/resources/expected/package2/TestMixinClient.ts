import { mixin } from "@jangaroo/joo/AS3";
import TestMixin from "./TestMixin";


@mixin(TestMixin)
class TestMixinClient {

  constructor(thing:string) {
    this.mix(thing);
  }
}
interface TestMixinClient extends TestMixin{}

export default TestMixinClient;
