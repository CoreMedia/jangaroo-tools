import Config from "@jangaroo/runtime/AS3/Config";
import Events from "@jangaroo/ext-ts/Events";
import { mixin } from "@jangaroo/runtime/AS3";
import Base from "../Ext/Base";
import TestMixin from "./TestMixin";
interface TestMixinClientEvents extends Events<Base>, Events<TestMixin> {
}

interface TestMixinClientConfig extends Config<Base>, Config<TestMixin>, Partial<Pick<TestMixinClient,
  "thing"
>> {
  listeners?: TestMixinClientEvents;
}



class TestMixinClient extends Base {
  declare Config: TestMixinClientConfig;

  thing: string = null;

  constructor(config: Config<TestMixinClient> = null) {
    super();
    this.mix(config.thing);
  }
}
interface TestMixinClient extends TestMixin{}

mixin(TestMixinClient, TestMixin);

export default TestMixinClient;
