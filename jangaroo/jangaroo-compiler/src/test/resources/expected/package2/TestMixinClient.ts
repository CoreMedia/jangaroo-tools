import Config from "@jangaroo/runtime/AS3/Config";
import { mixin } from "@jangaroo/runtime/AS3";
import TestMixin from "./TestMixin";
interface TestMixinClientConfig extends Config<TestMixin>, Partial<Pick<TestMixinClient,
  "thing"
>> {
}



class TestMixinClient {
  declare Config: TestMixinClientConfig;

  thing: string = null;

  constructor(config: Config<TestMixinClient> = null) {
    this.mix(config.thing);
  }
}
interface TestMixinClient extends TestMixin{}

mixin(TestMixinClient, TestMixin);

export default TestMixinClient;
