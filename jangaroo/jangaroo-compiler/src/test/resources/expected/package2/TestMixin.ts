import Config from "@jangaroo/runtime/Config";
import Base from "../Ext/Base";
interface TestMixinEvents {

  /**
   * Test mixin event.
   */
  firedByMixin?():any;
}

interface TestMixinConfig extends Config<Base>, Partial<Pick<TestMixin,
  "foo"
>> {
  listeners?: TestMixinEvents;
}


/**
 * This is a test mixin.
 */
class TestMixin extends Base {
  declare Config: TestMixinConfig;
  /**
   * An accessor.
   */
  
   get foo():number {
    return 42;
  }

  /**
   * The setter.
   */
  
   set foo(value:number) {
    // do nothing. 42 is perfect.
  }

  /**
   * This method is mixed in.
   * @param thing from the swamp
   * @return mud
   */
  mix(thing:string):string {
    return "Mixed " + thing + "!";
  }
}
export default TestMixin;
